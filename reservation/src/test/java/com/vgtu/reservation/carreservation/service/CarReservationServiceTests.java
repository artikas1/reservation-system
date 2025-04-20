package com.vgtu.reservation.carreservation.service;

import com.vgtu.reservation.auth.service.authentication.AuthenticationService;
import com.vgtu.reservation.car.entity.Car;
import com.vgtu.reservation.car.integrity.CarDataIntegrity;
import com.vgtu.reservation.car.service.CarService;
import com.vgtu.reservation.carreservation.dao.CarReservationDao;
import com.vgtu.reservation.carreservation.dto.CarReservationResponseDto;
import com.vgtu.reservation.carreservation.entity.CarReservation;
import com.vgtu.reservation.carreservation.integrity.CarReservationDataIntegrity;
import com.vgtu.reservation.carreservation.mapper.CarReservationMapper;
import com.vgtu.reservation.common.exception.CarReservationBadRequestException;
import com.vgtu.reservation.common.type.ReservationStatus;
import com.vgtu.reservation.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(value = MockitoExtension.class)
class CarReservationServiceTests {

    @Mock
    private CarReservationMapper carReservationMapper;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private CarReservationDao carReservationDao;
    @Mock
    private CarService carService;
    @Mock
    private CarReservationDataIntegrity carReservationDataIntegrity;
    @Mock
    private CarDataIntegrity carDataIntegrity;

    @InjectMocks
    private CarReservationService carReservationService;

    // shared test data
    private User testUser;
    private Car testCar;
    private CarReservation testReservation;

    @BeforeEach
    void setUp() {
        testUser = User.builder().id(UUID.randomUUID()).build();
        testCar = Car.builder().id(UUID.randomUUID()).build();
        testReservation = CarReservation.builder()
                .id(UUID.randomUUID())
                .user(testUser)
                .car(testCar)
                .reservedFrom(LocalDateTime.now().plusDays(1))
                .reservedTo(LocalDateTime.now().plusDays(2))
                .reservationStatus(ReservationStatus.AKTYVI)
                .build();
    }

    @Test
    void reserveCar_shouldReturnResponseDto_whenReservationIsSuccessful() {
        // Given
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(2);

        when(authenticationService.getAuthenticatedUser()).thenReturn(testUser);
        when(carService.getCarById(testCar.getId())).thenReturn(testCar);
        when(carReservationDao.save(any(CarReservation.class))).thenReturn(testReservation);

        CarReservationResponseDto expectedDto = CarReservationResponseDto.builder()
                .id(testReservation.getId())
                .userId(testUser.getId())
                .reservedFrom(start)
                .reservedTo(end)
                .reservationStatus(ReservationStatus.AKTYVI)
                .build();

        when(carReservationMapper.toEntity(eq(testCar), eq(testUser), eq(start), eq(end))).thenReturn(testReservation);
        when(carReservationMapper.toCarResponseDto(any())).thenReturn(expectedDto);

        // When
        CarReservationResponseDto result = carReservationService.reserveCar(testCar.getId(), start, end);

        // Then
        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
        assertEquals(expectedDto.getUserId(), result.getUserId());
        assertEquals(expectedDto.getReservationStatus(), result.getReservationStatus());

        verify(carReservationDataIntegrity).validateTimeRange(start, end);
        verify(carReservationDataIntegrity).checkForConflictingReservations(testCar, start, end);
    }

    @Test
    void updateCarReservation_shouldUpdateTimesAndReturnDto() {
        UUID reservationId = testReservation.getId();
        LocalDateTime newStart = LocalDateTime.now().plusDays(3);
        LocalDateTime newEnd = newStart.plusHours(4);

        when(carReservationDao.findReservationByCarReservationId(reservationId)).thenReturn(testReservation);
        when(authenticationService.getAuthenticatedUser()).thenReturn(testUser);

        // simulate save and mapping
        when(carReservationDao.save(any())).thenReturn(testReservation);
        when(carReservationMapper.toCarResponseDto(any())).thenReturn(
                CarReservationResponseDto.builder().id(reservationId).build()
        );

        CarReservationResponseDto result = carReservationService.updateCarReservation(reservationId, newStart, newEnd);

        assertNotNull(result);
        assertEquals(reservationId, result.getId());

        verify(carReservationDataIntegrity).validateTimeRange(newStart, newEnd);
        verify(carReservationDataIntegrity).checkForConflictingReservationsExceptSelf(
                testCar, newStart, newEnd, reservationId
        );
    }

    @Test
    void updateCarReservation_shouldThrow_whenReservationNotFound() {
        UUID reservationId = UUID.randomUUID();
        when(carReservationDao.findReservationByCarReservationId(reservationId))
                .thenThrow(new CarReservationBadRequestException("Not found"));

        assertThrows(CarReservationBadRequestException.class, () ->
                carReservationService.updateCarReservation(reservationId, LocalDateTime.now(), LocalDateTime.now().plusHours(1))
        );
    }

    @Test
    void updateCarReservation_shouldThrow_whenUserIsUnauthorized() {
        UUID reservationId = testReservation.getId();
        when(carReservationDao.findReservationByCarReservationId(reservationId)).thenReturn(testReservation);
        when(authenticationService.getAuthenticatedUser()).thenReturn(User.builder().id(UUID.randomUUID()).build()); // wrong user

        doThrow(new SecurityException("Unauthorized"))
                .when(authenticationService).checkAuthorizationBetweenUserAndCarReservation(any(), any());

        assertThrows(SecurityException.class, () ->
                carReservationService.updateCarReservation(reservationId, LocalDateTime.now(), LocalDateTime.now().plusHours(1))
        );
    }

    @Test
    void updateCarReservation_shouldUseOldTimes_whenNewOnesAreNull() {
        UUID reservationId = testReservation.getId();
        when(carReservationDao.findReservationByCarReservationId(reservationId)).thenReturn(testReservation);
        when(authenticationService.getAuthenticatedUser()).thenReturn(testUser);

        when(carReservationDao.save(any())).thenReturn(testReservation);
        when(carReservationMapper.toCarResponseDto(any())).thenReturn(
                CarReservationResponseDto.builder().id(reservationId).build()
        );

        var result = carReservationService.updateCarReservation(reservationId, null, null);

        assertNotNull(result);
        verify(carReservationDataIntegrity).validateTimeRange(testReservation.getReservedFrom(), testReservation.getReservedTo());
    }

    @Test
    void deleteReservation_shouldSoftDelete_whenUserIsAuthorized() {
        UUID reservationId = testReservation.getId();

        when(authenticationService.getAuthenticatedUser()).thenReturn(testUser);
        when(carReservationDao.findReservationByCarReservationId(reservationId)).thenReturn(testReservation);

        carReservationService.deleteReservationByCarReservationId(reservationId);

        assertNotNull(testReservation.getDeletedAt());
        assertEquals(ReservationStatus.ATŠAUKTA, testReservation.getReservationStatus());
        verify(carReservationDao).save(testReservation);
    }

    @Test
    void findAllUserReservations_shouldReturnDtos_andUpdateExpiredStatus() {
        CarReservation expired = CarReservation.builder()
                .id(UUID.randomUUID())
                .user(testUser)
                .car(testCar)
                .reservedFrom(LocalDateTime.now().minusDays(2))
                .reservedTo(LocalDateTime.now().minusDays(1))
                .reservationStatus(ReservationStatus.AKTYVI)
                .build();

        when(authenticationService.getAuthenticatedUser()).thenReturn(testUser);
        when(carReservationDao.findUserReservationsByFilters(any(), any(), any(), any()))
                .thenReturn(List.of(testReservation, expired));

        when(carReservationMapper.toCarResponseDto(any())).thenReturn(
                CarReservationResponseDto.builder().id(testReservation.getId()).build(),
                CarReservationResponseDto.builder().id(expired.getId()).build()
        );

        var result = carReservationService.findAllUserReservations(null, null, null);

        assertEquals(2, result.size());
        assertEquals(ReservationStatus.PASIBAIGUSI, expired.getReservationStatus());
    }

    @Test
    void findAllActiveUserReservations_shouldReturnDtos() {
        when(authenticationService.getAuthenticatedUser()).thenReturn(testUser);
        when(carReservationDao.findAllActiveUserReservations(testUser.getId()))
                .thenReturn(List.of(testReservation));
        when(carReservationMapper.toCarResponseDto(testReservation))
                .thenReturn(CarReservationResponseDto.builder().id(testReservation.getId()).build());

        var result = carReservationService.findAllActiveUserReservations();

        assertEquals(1, result.size());
        assertEquals(testReservation.getId(), result.get(0).getId());
    }

    @Test
    void findReservationsEndingBetween_shouldReturnList() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(2);
        when(carReservationDao.findReservationsEndingBetween(start, end))
                .thenReturn(List.of(testReservation));

        var result = carReservationService.findReservationsEndingBetween(start, end);
        assertEquals(1, result.size());
    }

    @Test
    void getReservedTimeRangesForCar_shouldExcludeSpecifiedReservationId() {
        when(carReservationDao.findByCarId(testCar.getId())).thenReturn(List.of(testReservation));

        var result = carReservationService.getReservedTimeRangesForCar(testCar.getId(), testReservation.getId());

        assertEquals(0, result.size());
    }

}

