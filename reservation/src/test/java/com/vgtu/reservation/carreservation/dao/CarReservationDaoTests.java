package com.vgtu.reservation.carreservation.dao;

import com.vgtu.reservation.car.entity.Car;
import com.vgtu.reservation.car.integrity.CarDataIntegrity;
import com.vgtu.reservation.carreservation.entity.CarReservation;
import com.vgtu.reservation.carreservation.integrity.CarReservationDataIntegrity;
import com.vgtu.reservation.carreservation.repository.CarReservationRepository;
import com.vgtu.reservation.common.exception.CarReservationBadRequestException;
import com.vgtu.reservation.common.type.ReservationStatus;
import com.vgtu.reservation.user.entity.User;
import com.vgtu.reservation.user.integrity.UserDataIntegrity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarReservationDaoTests {

    @Mock
    private UserDataIntegrity userDataIntegrity;
    @Mock
    private CarReservationRepository carReservationRepository;
    @Mock
    private CarReservationDataIntegrity carReservationDataIntegrity;
    @Mock
    private CarDataIntegrity carDataIntegrity;

    @InjectMocks
    private CarReservationDao carReservationDao;

    private CarReservation reservation;
    private UUID userId;
    private UUID carId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        carId = UUID.randomUUID();

        User testUser = User.builder().id(userId).build();
        Car testCar = Car.builder().id(carId).build();

        reservation = CarReservation.builder()
                .id(UUID.randomUUID())
                .user(testUser)
                .car(testCar)
                .reservedFrom(LocalDateTime.now())
                .reservedTo(LocalDateTime.now().plusHours(2))
                .reservationStatus(ReservationStatus.AKTYVI)
                .build();
    }

    @Test
    void save_shouldCallIntegrityAndSave() {
        when(carReservationRepository.save(reservation)).thenReturn(reservation);

        CarReservation saved = carReservationDao.save(reservation);

        verify(carReservationDataIntegrity).validateCarReservation(reservation);
        assertEquals(reservation, saved);
    }

    @Test
    void findAllUserReservations_shouldCallValidationAndReturnResults() {
        when(carReservationRepository.findByUserId(userId)).thenReturn(List.of(reservation));

        List<CarReservation> results = carReservationDao.findAllUserReservations(userId);

        verify(userDataIntegrity).validateId(userId);
        assertEquals(1, results.size());
    }

    @Test
    void findAllActiveUserReservations_shouldReturnActive() {
        when(carReservationRepository.findActiveByUserId(userId)).thenReturn(List.of(reservation));

        List<CarReservation> results = carReservationDao.findAllActiveUserReservations(userId);

        verify(userDataIntegrity).validateId(userId);
        assertEquals(1, results.size());
    }

    @Test
    void findReservationByCarReservationId_shouldReturnReservation() {
        when(carReservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));

        CarReservation found = carReservationDao.findReservationByCarReservationId(reservation.getId());

        verify(carReservationDataIntegrity).validateId(reservation.getId());
        assertEquals(reservation, found);
    }

    @Test
    void findReservationByCarReservationId_shouldThrow_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(carReservationRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CarReservationBadRequestException.class, () -> carReservationDao.findReservationByCarReservationId(id));
    }

    @Test
    void deleteCarReservationByCarReservationId_shouldDelete() {
        UUID id = UUID.randomUUID();

        carReservationDao.deleteCarReservationByCarReservationId(id);

        verify(carReservationDataIntegrity).validateId(id);
        verify(carReservationRepository).deleteById(id);
    }

    @Test
    void findUserReservationsByFilters_shouldReturnFilteredList() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        when(carReservationRepository.findUserReservationsByFilters(userId, ReservationStatus.AKTYVI, start, end))
                .thenReturn(List.of(reservation));

        var result = carReservationDao.findUserReservationsByFilters(userId, ReservationStatus.AKTYVI, start, end);

        verify(userDataIntegrity).validateId(userId);
        assertEquals(1, result.size());
    }

    @Test
    void findReservationsEndingBetween_shouldReturnList() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(5);
        when(carReservationRepository.findReservationsEndingBetween(start, end))
                .thenReturn(List.of(reservation));

        var result = carReservationDao.findReservationsEndingBetween(start, end);
        assertEquals(1, result.size());
    }

    @Test
    void findReservationsStartingBetween_shouldReturnList() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(5);
        when(carReservationRepository.findReservationsStartingBetween(start, end))
                .thenReturn(List.of(reservation));

        var result = carReservationDao.findReservationsStartingBetween(start, end);
        assertEquals(1, result.size());
    }

    @Test
    void findByCarId_shouldReturnList() {
        when(carReservationRepository.findByCarIdAndDeletedAtIsNull(carId))
                .thenReturn(List.of(reservation));

        var result = carReservationDao.findByCarId(carId);

        verify(carDataIntegrity).validateId(carId);
        assertEquals(1, result.size());
    }
}
