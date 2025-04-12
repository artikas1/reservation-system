package com.vgtu.reservation.roomreservation.integrity;

import com.vgtu.reservation.common.exception.RoomConflictException;
import com.vgtu.reservation.common.exception.RoomReservationBadRequestException;
import com.vgtu.reservation.room.entity.Room;
import com.vgtu.reservation.roomreservation.entity.RoomReservation;
import com.vgtu.reservation.roomreservation.repository.RoomReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RoomReservationDataIntegrity {

    private final RoomReservationRepository roomReservationRepository;

    public static final String ID_CANNOT_BE_NULL = "Room reservation id cannot be null";

    public void validateId(UUID id) {
        if(id == null) {
            throw new RoomReservationBadRequestException(ID_CANNOT_BE_NULL);
        }
    }

    public void validateTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            throw new RoomReservationBadRequestException("Start time and end time cannot be null");
        }
        if (startTime.isAfter(endTime)) {
            throw new RoomReservationBadRequestException("Start time must be before end time");
        }
        if(startTime.isBefore(LocalDateTime.now())) {
            throw new RoomReservationBadRequestException("Cannot reserve for past time");
        }
    }

    public void checkForConflictingReservations(Room room, LocalDateTime startTime, LocalDateTime endTime) {
        List<RoomReservation> conflictingReservations = roomReservationRepository.findConflictingReservations(
                room.getId(),
                startTime,
                endTime
        );

        if (!conflictingReservations.isEmpty()) {
            throw new RoomConflictException("Room is already reserved for the requested time period");
        }
    }

    public void checkForConflictingReservationsExceptSelf(Room room, LocalDateTime startTime, LocalDateTime endTime, UUID reservationId) {
        List<RoomReservation> conflictingReservations = roomReservationRepository.findConflictingReservationsExceptSelf(
                room.getId(), startTime, endTime, reservationId
        );

        if(!conflictingReservations.isEmpty()) {
            throw new RoomConflictException("Room is already reserved for the requested time period");
        }
    }

    public void validateRoomReservation(RoomReservation roomReservation) {
        if(roomReservation == null) {
            throw new RoomReservationBadRequestException("Car reservation cannot be null");
        }
    }
}
