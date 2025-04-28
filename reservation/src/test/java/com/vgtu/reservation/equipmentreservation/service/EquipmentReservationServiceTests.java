package com.vgtu.reservation.equipmentreservation.service;

import com.vgtu.reservation.auth.service.authentication.AuthenticationService;
import com.vgtu.reservation.common.type.ReservationStatus;
import com.vgtu.reservation.equipment.entity.Equipment;
import com.vgtu.reservation.equipment.integrity.EquipmentDataIntegrity;
import com.vgtu.reservation.equipment.service.EquipmentService;
import com.vgtu.reservation.equipmentreservation.dao.EquipmentReservationDao;
import com.vgtu.reservation.equipmentreservation.dto.EquipmentReservationResponseDto;
import com.vgtu.reservation.equipmentreservation.entity.EquipmentReservation;
import com.vgtu.reservation.equipmentreservation.integrity.EquipmentReservationDataIntegrity;
import com.vgtu.reservation.equipmentreservation.mapper.EquipmentReservationMapper;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipmentReservationServiceTests {

    @Mock
    private EquipmentDataIntegrity equipmentDataIntegrity;
    @Mock
    private EquipmentReservationMapper equipmentReservationMapper;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private EquipmentReservationDao equipmentReservationDao;
    @Mock
    private EquipmentReservationDataIntegrity equipmentReservationDataIntegrity;
    @Mock
    private EquipmentService equipmentService;

    @InjectMocks
    private EquipmentReservationService equipmentReservationService;

    private User testUser;
    private Equipment testEquipment;
    private EquipmentReservation testReservation;

    @BeforeEach
    void setUp() {
        testUser = User.builder().id(UUID.randomUUID()).build();
        testEquipment = Equipment.builder().id(UUID.randomUUID()).build();
        testReservation = EquipmentReservation.builder()
                .id(UUID.randomUUID())
                .user(testUser)
                .equipment(testEquipment)
                .reservedFrom(LocalDateTime.now().plusDays(1))
                .reservedTo(LocalDateTime.now().plusDays(2))
                .reservationStatus(ReservationStatus.AKTYVI)
                .build();
    }

    @Test
    void reserveEquipment_shouldReturnResponseDto_whenReservationIsSuccessful() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(2);

        when(authenticationService.getAuthenticatedUser()).thenReturn(testUser);
        when(equipmentService.getEquipmentById(testEquipment.getId())).thenReturn(testEquipment);
        when(equipmentReservationMapper.toEntity(testEquipment, testUser, start, end)).thenReturn(testReservation);
        when(equipmentReservationDao.save(testReservation)).thenReturn(testReservation);
        when(equipmentReservationMapper.toEquipmentResponseDto(testReservation)).thenReturn(
                EquipmentReservationResponseDto.builder().id(testReservation.getId()).userId(testUser.getId()).build()
        );

        EquipmentReservationResponseDto result = equipmentReservationService.reserveEquipment(testEquipment.getId(), start, end);

        assertNotNull(result);
        assertEquals(testReservation.getId(), result.getId());
        assertEquals(testUser.getId(), result.getUserId());

        verify(equipmentDataIntegrity).validateId(testEquipment.getId());
        verify(equipmentReservationDataIntegrity).validateTimeRange(start, end);
        verify(equipmentReservationDataIntegrity).checkForConflictingReservations(testEquipment, start, end);
    }

    @Test
    void deleteReservation_shouldSoftDelete_whenUserIsAuthorized() {
        UUID reservationId = testReservation.getId();
        when(authenticationService.getAuthenticatedUser()).thenReturn(testUser);
        when(equipmentReservationDao.findReservationByEquipmentReservationId(reservationId)).thenReturn(testReservation);

        equipmentReservationService.deleteReservationByEquipmentReservationId(reservationId);

        assertNotNull(testReservation.getDeletedAt());
        assertEquals(ReservationStatus.ATŠAUKTA, testReservation.getReservationStatus());
        verify(equipmentReservationDao).save(testReservation);
    }

    @Test
    void updateEquipmentReservation_shouldUpdateTimesAndReturnDto() {
        UUID reservationId = testReservation.getId();
        LocalDateTime newStart = LocalDateTime.now().plusDays(3);
        LocalDateTime newEnd = newStart.plusHours(4);

        when(authenticationService.getAuthenticatedUser()).thenReturn(testUser);
        when(equipmentReservationDao.findReservationByEquipmentReservationId(reservationId)).thenReturn(testReservation);
        when(equipmentReservationDao.save(any())).thenReturn(testReservation);
        when(equipmentReservationMapper.toEquipmentResponseDto(any())).thenReturn(
                EquipmentReservationResponseDto.builder().id(reservationId).build()
        );

        EquipmentReservationResponseDto result = equipmentReservationService.updateEquipmentReservation(reservationId, newStart, newEnd);

        assertNotNull(result);
        assertEquals(reservationId, result.getId());
        verify(equipmentReservationDataIntegrity).validateTimeRange(newStart, newEnd);
        verify(equipmentReservationDataIntegrity).checkForConflictingReservationsExceptSelf(testEquipment, newStart, newEnd, reservationId);
    }

    @Test
    void findAllUserReservations_shouldReturnDtos_andUpdateExpiredStatus() {
        EquipmentReservation expired = EquipmentReservation.builder()
                .id(UUID.randomUUID())
                .user(testUser)
                .equipment(testEquipment)
                .reservedFrom(LocalDateTime.now().minusDays(2))
                .reservedTo(LocalDateTime.now().minusDays(1))
                .reservationStatus(ReservationStatus.AKTYVI)
                .build();

        when(authenticationService.getAuthenticatedUser()).thenReturn(testUser);
        when(equipmentReservationDao.findUserReservationsByFilters(any(), any(), any(), any()))
                .thenReturn(List.of(testReservation, expired));

        when(equipmentReservationMapper.toEquipmentResponseDto(any())).thenReturn(
                EquipmentReservationResponseDto.builder().id(testReservation.getId()).build(),
                EquipmentReservationResponseDto.builder().id(expired.getId()).build()
        );

        var result = equipmentReservationService.findAllUserReservations(null, null, null);

        assertEquals(2, result.size());
        assertEquals(ReservationStatus.PASIBAIGUSI, expired.getReservationStatus());
    }

    @Test
    void findAllActiveUserReservations_shouldReturnDtos() {
        when(authenticationService.getAuthenticatedUser()).thenReturn(testUser);
        when(equipmentReservationDao.findAllActiveUserReservations(testUser.getId()))
                .thenReturn(List.of(testReservation));
        when(equipmentReservationMapper.toEquipmentResponseDto(testReservation))
                .thenReturn(EquipmentReservationResponseDto.builder().id(testReservation.getId()).build());

        var result = equipmentReservationService.findAllActiveUserReservations();

        assertEquals(1, result.size());
        assertEquals(testReservation.getId(), result.get(0).getId());
    }

    @Test
    void updateReservation_shouldUseOldTimes_whenNewTimesAreNull() {
        UUID reservationId = testReservation.getId();

        when(authenticationService.getAuthenticatedUser()).thenReturn(testUser);
        when(equipmentReservationDao.findReservationByEquipmentReservationId(reservationId)).thenReturn(testReservation);
        when(equipmentReservationDao.save(any())).thenReturn(testReservation);
        when(equipmentReservationMapper.toEquipmentResponseDto(any())).thenReturn(
                EquipmentReservationResponseDto.builder().id(reservationId).build()
        );

        var result = equipmentReservationService.updateEquipmentReservation(reservationId, null, null);

        assertNotNull(result);
        verify(equipmentReservationDataIntegrity).validateTimeRange(testReservation.getReservedFrom(), testReservation.getReservedTo());
    }

    @Test
    void findReservationsStartingBetween_shouldReturnList() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(4);

        when(equipmentReservationDao.findReservationsStartingBetween(start, end))
                .thenReturn(List.of(testReservation));

        var result = equipmentReservationService.findReservationsStartingBetween(start, end);
        assertEquals(1, result.size());
    }

    @Test
    void getReservedTimeRangesForEquipment_shouldExcludeSpecifiedReservationId() {
        when(equipmentReservationDao.findByEquipmentId(testEquipment.getId()))
                .thenReturn(List.of(testReservation));

        var result = equipmentReservationService.getReservedTimeRangesForEquipment(testEquipment.getId(), testReservation.getId());

        assertEquals(0, result.size());
    }


}
