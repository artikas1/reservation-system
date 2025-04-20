package com.vgtu.reservation.roomreservation.integrity;

import com.vgtu.reservation.common.exception.RoomConflictException;
import com.vgtu.reservation.common.exception.RoomReservationBadRequestException;
import com.vgtu.reservation.room.entity.Room;
import com.vgtu.reservation.roomreservation.entity.RoomReservation;
import com.vgtu.reservation.roomreservation.repository.RoomReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoomReservationDataIntegrityTests {

    @Mock
    private RoomReservationRepository roomReservationRepository;

    @InjectMocks
    private RoomReservationDataIntegrity roomReservationDataIntegrity;

    private UUID validId;
    private LocalDateTime now;
    private LocalDateTime future;
    private Room room;

    @BeforeEach
    void setUp() {
        validId = UUID.randomUUID();
        now = LocalDateTime.now().plusHours(1);
        future = now.plusHours(2);
        room = Room.builder().id(UUID.randomUUID()).build();
    }

    @Test
    void validateId_shouldThrow_whenIdIsNull() {
        assertThrows(RoomReservationBadRequestException.class, () ->
                roomReservationDataIntegrity.validateId(null));
    }

    @Test
    void validateTimeRange_shouldThrow_whenStartTimeIsNull() {
        assertThrows(RoomReservationBadRequestException.class, () ->
                roomReservationDataIntegrity.validateTimeRange(null, future));
    }

    @Test
    void validateTimeRange_shouldThrow_whenEndTimeIsNull() {
        assertThrows(RoomReservationBadRequestException.class, () ->
                roomReservationDataIntegrity.validateTimeRange(now, null));
    }

    @Test
    void validateTimeRange_shouldThrow_whenStartIsAfterEnd() {
        assertThrows(RoomReservationBadRequestException.class, () ->
                roomReservationDataIntegrity.validateTimeRange(future, now));
    }

    @Test
    void validateTimeRange_shouldThrow_whenStartIsInThePast() {
        LocalDateTime past = LocalDateTime.now().minusHours(1);
        assertThrows(RoomReservationBadRequestException.class, () ->
                roomReservationDataIntegrity.validateTimeRange(past, future));
    }

    @Test
    void validateTimeRange_shouldPass_whenValid() {
        assertDoesNotThrow(() ->
                roomReservationDataIntegrity.validateTimeRange(now, future));
    }

    @Test
    void checkForConflictingReservations_shouldThrow_whenConflictsExist() {
        when(roomReservationRepository.findConflictingReservations(room.getId(), now, future))
                .thenReturn(List.of(new RoomReservation()));

        assertThrows(RoomConflictException.class, () ->
                roomReservationDataIntegrity.checkForConflictingReservations(room, now, future));
    }

    @Test
    void checkForConflictingReservations_shouldPass_whenNoConflicts() {
        when(roomReservationRepository.findConflictingReservations(room.getId(), now, future))
                .thenReturn(Collections.emptyList());

        assertDoesNotThrow(() ->
                roomReservationDataIntegrity.checkForConflictingReservations(room, now, future));
    }

    @Test
    void checkForConflictingReservationsExceptSelf_shouldThrow_whenConflictsExist() {
        UUID reservationId = UUID.randomUUID();
        when(roomReservationRepository.findConflictingReservationsExceptSelf(room.getId(), now, future, reservationId))
                .thenReturn(List.of(new RoomReservation()));

        assertThrows(RoomConflictException.class, () ->
                roomReservationDataIntegrity.checkForConflictingReservationsExceptSelf(room, now, future, reservationId));
    }

    @Test
    void checkForConflictingReservationsExceptSelf_shouldPass_whenNoConflicts() {
        UUID reservationId = UUID.randomUUID();
        when(roomReservationRepository.findConflictingReservationsExceptSelf(room.getId(), now, future, reservationId))
                .thenReturn(Collections.emptyList());

        assertDoesNotThrow(() ->
                roomReservationDataIntegrity.checkForConflictingReservationsExceptSelf(room, now, future, reservationId));
    }

    @Test
    void validateRoomReservation_shouldThrow_whenNull() {
        assertThrows(RoomReservationBadRequestException.class, () ->
                roomReservationDataIntegrity.validateRoomReservation(null));
    }

    @Test
    void validateRoomReservation_shouldPass_whenValid() {
        RoomReservation reservation = RoomReservation.builder().id(UUID.randomUUID()).build();
        assertDoesNotThrow(() -> roomReservationDataIntegrity.validateRoomReservation(reservation));
    }
}