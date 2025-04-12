package com.vgtu.reservation.roomreservation.dao;

import com.vgtu.reservation.common.exception.RoomReservationBadRequestException;
import com.vgtu.reservation.common.type.ReservationStatus;
import com.vgtu.reservation.room.integrity.RoomDataIntegrity;
import com.vgtu.reservation.roomreservation.entity.RoomReservation;
import com.vgtu.reservation.roomreservation.integrity.RoomReservationDataIntegrity;
import com.vgtu.reservation.roomreservation.repository.RoomReservationRepository;
import com.vgtu.reservation.user.integrity.UserDataIntegrity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Interacts with the database to fetch data related to a room reservations
 */
@Service
@AllArgsConstructor
public class RoomReservationDao {

    private final UserDataIntegrity userDataIntegrity;
    private final RoomReservationRepository roomReservationRepository;
    private final RoomReservationDataIntegrity roomReservationDataIntegrity;
    private final RoomDataIntegrity roomDataIntegrity;

    public RoomReservation save(RoomReservation roomReservation) {
        roomReservationDataIntegrity.validateRoomReservation(roomReservation);

        return roomReservationRepository.save(roomReservation);
    }

    public List<RoomReservation> findAllUserReservations(UUID userId) {
        userDataIntegrity.validateId(userId);

        return roomReservationRepository.findByUserId(userId);
    }

    public List <RoomReservation> findAllActiveUserReservations(UUID userId) {
        userDataIntegrity.validateId(userId);

        return roomReservationRepository.findActiveByUserId(userId);
    }

    public RoomReservation findReservationByRoomReservationId(UUID roomReservationId) {
        roomReservationDataIntegrity.validateId(roomReservationId);

        return roomReservationRepository.findById(roomReservationId)
                .orElseThrow(() -> new RoomReservationBadRequestException("Reservation not found"));
    }

    public void deleteReservationByRoomId(UUID roomReservationId) {
        roomReservationDataIntegrity.validateId(roomReservationId);

        roomReservationRepository.deleteById(roomReservationId);
    }

    public List<RoomReservation> findUserReservationsByFilters(UUID userId, ReservationStatus reservationStatus, LocalDateTime startTime, LocalDateTime endTime) {
        userDataIntegrity.validateId(userId);
        return roomReservationRepository.findUserReservationsByFilters(userId, reservationStatus, startTime, endTime);
    }

    public List<RoomReservation> findReservationsStartingBetween(LocalDateTime start, LocalDateTime end) {
        return roomReservationRepository.findReservationsStartingBetween(start, end);
    }

    public List<RoomReservation> findByRoomId(UUID roomId) {
        roomDataIntegrity.validateId(roomId);

        return roomReservationRepository.findByRoomIdAndDeletedAtIsNull(roomId);
    }
}
