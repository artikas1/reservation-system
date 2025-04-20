package com.vgtu.reservation.equipmentreservation.dao;

import com.vgtu.reservation.common.type.ReservationStatus;
import com.vgtu.reservation.equipment.entity.Equipment;
import com.vgtu.reservation.equipment.integrity.EquipmentDataIntegrity;
import com.vgtu.reservation.equipmentreservation.entity.EquipmentReservation;
import com.vgtu.reservation.equipmentreservation.integrity.EquipmentReservationDataIntegrity;
import com.vgtu.reservation.equipmentreservation.repository.EquipmentReservationRepository;
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
class EquipmentReservationDaoTests {

    @Mock private UserDataIntegrity userDataIntegrity;
    @Mock private EquipmentReservationRepository equipmentReservationRepository;
    @Mock private EquipmentReservationDataIntegrity equipmentReservationDataIntegrity;
    @Mock private EquipmentDataIntegrity equipmentDataIntegrity;

    @InjectMocks
    private EquipmentReservationDao equipmentReservationDao;

    private EquipmentReservation reservation;
    private UUID userId;
    private UUID equipmentId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        equipmentId = UUID.randomUUID();

        User testUser = User.builder().id(userId).build();
        Equipment testEquipment = Equipment.builder().id(equipmentId).build();

        reservation = EquipmentReservation.builder()
                .id(UUID.randomUUID())
                .user(testUser)
                .equipment(testEquipment)
                .reservedFrom(LocalDateTime.now())
                .reservedTo(LocalDateTime.now().plusHours(2))
                .reservationStatus(ReservationStatus.AKTYVI)
                .build();
    }

    @Test
    void save_shouldCallIntegrityAndSave() {
        when(equipmentReservationRepository.save(reservation)).thenReturn(reservation);

        EquipmentReservation saved = equipmentReservationDao.save(reservation);

        verify(equipmentReservationDataIntegrity).validateEquipmentReservation(reservation);
        assertEquals(reservation, saved);
    }

    @Test
    void findAllUserReservations_shouldCallValidationAndReturnResults() {
        when(equipmentReservationRepository.findByUserId(userId)).thenReturn(List.of(reservation));

        List<EquipmentReservation> results = equipmentReservationDao.findAllUserReservations(userId);

        verify(userDataIntegrity).validateId(userId);
        assertEquals(1, results.size());
    }

    @Test
    void findAllActiveUserReservations_shouldReturnActive() {
        when(equipmentReservationRepository.findActiveByUserId(userId)).thenReturn(List.of(reservation));

        List<EquipmentReservation> results = equipmentReservationDao.findAllActiveUserReservations(userId);

        verify(userDataIntegrity).validateId(userId);
        assertEquals(1, results.size());
    }

    @Test
    void findReservationByEquipmentReservationId_shouldReturnReservation() {
        when(equipmentReservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));

        EquipmentReservation found = equipmentReservationDao.findReservationByEquipmentReservationId(reservation.getId());

        verify(equipmentReservationDataIntegrity).validateId(reservation.getId());
        assertEquals(reservation, found);
    }

    @Test
    void findReservationByEquipmentReservationId_shouldThrow_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(equipmentReservationRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> equipmentReservationDao.findReservationByEquipmentReservationId(id));
    }

    @Test
    void deleteReservationByEquipmentReservationId_shouldDelete() {
        UUID id = UUID.randomUUID();

        equipmentReservationDao.deleteReservationByEquipmentReservationId(id);

        verify(equipmentReservationDataIntegrity).validateId(id);
        verify(equipmentReservationRepository).deleteById(id);
    }

    @Test
    void findUserReservationsByFilters_shouldReturnFilteredList() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        when(equipmentReservationRepository.findUserReservationsByFilters(userId, ReservationStatus.AKTYVI, start, end))
                .thenReturn(List.of(reservation));

        var result = equipmentReservationDao.findUserReservationsByFilters(userId, ReservationStatus.AKTYVI, start, end);

        verify(userDataIntegrity).validateId(userId);
        assertEquals(1, result.size());
    }

    @Test
    void findReservationsStartingBetween_shouldReturnList() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(5);
        when(equipmentReservationRepository.findReservationsStartingBetween(start, end))
                .thenReturn(List.of(reservation));

        var result = equipmentReservationDao.findReservationsStartingBetween(start, end);
        assertEquals(1, result.size());
    }

    @Test
    void findByEquipmentId_shouldReturnList() {
        when(equipmentReservationRepository.findByEquipmentIdAndDeletedAtIsNull(equipmentId))
                .thenReturn(List.of(reservation));

        var result = equipmentReservationDao.findByEquipmentId(equipmentId);

        verify(equipmentDataIntegrity).validateId(equipmentId);
        assertEquals(1, result.size());
    }
}
