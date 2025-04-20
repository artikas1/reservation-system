package com.vgtu.reservation.equipmentreservation.integrity;

import com.vgtu.reservation.common.exception.EquipmentConflictException;
import com.vgtu.reservation.common.exception.EquipmentReservationBadRequestException;
import com.vgtu.reservation.equipment.entity.Equipment;
import com.vgtu.reservation.equipmentreservation.entity.EquipmentReservation;
import com.vgtu.reservation.equipmentreservation.repository.EquipmentReservationRepository;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EquipmentReservationDataIntegrityTests {

    @Mock
    private EquipmentReservationRepository repository;

    @InjectMocks
    private EquipmentReservationDataIntegrity integrity;

    private Equipment equipment;
    private EquipmentReservation reservation;
    private LocalDateTime start;
    private LocalDateTime end;
    private UUID reservationId;

    @BeforeEach
    void setUp() {
        equipment = Equipment.builder().id(UUID.randomUUID()).build();
        reservationId = UUID.randomUUID();
        start = LocalDateTime.now().plusHours(1);
        end = start.plusHours(2);
        reservation = EquipmentReservation.builder().id(reservationId).equipment(equipment).reservedFrom(start).reservedTo(end).build();
    }

    @Test
    void validateId_shouldThrow_whenIdIsNull() {
        assertThrows(EquipmentReservationBadRequestException.class, () -> integrity.validateId(null));
    }

    @Test
    void validateId_shouldPass_whenIdNotNull() {
        assertDoesNotThrow(() -> integrity.validateId(UUID.randomUUID()));
    }

    @Test
    void validateTimeRange_shouldThrow_whenStartIsAfterEnd() {
        LocalDateTime start = LocalDateTime.now().plusHours(2);
        LocalDateTime end = LocalDateTime.now().plusHours(1);

        assertThrows(EquipmentReservationBadRequestException.class, () -> integrity.validateTimeRange(start, end));
    }

    @Test
    void validateTimeRange_shouldThrow_whenStartIsInThePast() {
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(1);

        assertThrows(EquipmentReservationBadRequestException.class, () -> integrity.validateTimeRange(start, end));
    }

    @Test
    void validateTimeRange_shouldThrow_whenStartOrEndIsNull() {
        assertThrows(EquipmentReservationBadRequestException.class, () -> integrity.validateTimeRange(null, end));
        assertThrows(EquipmentReservationBadRequestException.class, () -> integrity.validateTimeRange(start, null));
    }

    @Test
    void validateTimeRange_shouldPass_whenValid() {
        assertDoesNotThrow(() -> integrity.validateTimeRange(start, end));
    }

    @Test
    void checkForConflictingReservations_shouldThrow_whenConflictsExist() {
        when(repository.findConflictingReservations(equipment.getId(), start, end))
                .thenReturn(List.of(reservation));

        assertThrows(EquipmentConflictException.class,
                () -> integrity.checkForConflictingReservations(equipment, start, end));
    }

    @Test
    void checkForConflictingReservations_shouldPass_whenNoConflicts() {
        when(repository.findConflictingReservations(equipment.getId(), start, end))
                .thenReturn(List.of());

        assertDoesNotThrow(() -> integrity.checkForConflictingReservations(equipment, start, end));
    }

    @Test
    void checkForConflictingReservationsExceptSelf_shouldThrow_whenConflictsExist() {
        when(repository.findConflictingReservationsExceptSelf(equipment.getId(), start, end, reservationId))
                .thenReturn(List.of(reservation));

        assertThrows(EquipmentConflictException.class,
                () -> integrity.checkForConflictingReservationsExceptSelf(equipment, start, end, reservationId));
    }

    @Test
    void checkForConflictingReservationsExceptSelf_shouldPass_whenNoConflicts() {
        when(repository.findConflictingReservationsExceptSelf(equipment.getId(), start, end, reservationId))
                .thenReturn(List.of());

        assertDoesNotThrow(() -> integrity.checkForConflictingReservationsExceptSelf(equipment, start, end, reservationId));
    }

    @Test
    void validateEquipmentReservation_shouldThrow_whenNull() {
        assertThrows(EquipmentReservationBadRequestException.class, () -> integrity.validateEquipmentReservation(null));
    }

    @Test
    void validateEquipmentReservation_shouldPass_whenValid() {
        assertDoesNotThrow(() -> integrity.validateEquipmentReservation(reservation));
    }
}
