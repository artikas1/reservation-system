package com.vgtu.reservation.roomreservation.service;

import com.vgtu.reservation.auth.service.authentication.AuthenticationService;
import com.vgtu.reservation.common.type.ReservationStatus;
import com.vgtu.reservation.room.integrity.RoomDataIntegrity;
import com.vgtu.reservation.room.service.RoomService;
import com.vgtu.reservation.roomreservation.dao.RoomReservationDao;
import com.vgtu.reservation.roomreservation.dto.RoomReservationResponseDto;
import com.vgtu.reservation.roomreservation.dto.RoomReservationTimeRangeDto;
import com.vgtu.reservation.roomreservation.entity.RoomReservation;
import com.vgtu.reservation.roomreservation.integrity.RoomReservationDataIntegrity;
import com.vgtu.reservation.roomreservation.mapper.RoomReservationMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Contains the business logic for the room reservation functionality
 */
@Service
@AllArgsConstructor
public class RoomReservationService {

    private final RoomDataIntegrity roomDataIntegrity;
    private final RoomReservationMapper roomReservationMapper;
    private final AuthenticationService authenticationService;
    private final RoomReservationDao roomReservationDao;
    private final RoomService roomService;
    private final RoomReservationDataIntegrity roomReservationDataIntegrity;

    public List<RoomReservationResponseDto> findAllActiveUserReservations() {
        var user = authenticationService.getAuthenticatedUser();

        return roomReservationDao.findAllActiveUserReservations(user.getId())
                .stream().map(roomReservationMapper::toRoomResponseDto)
                .collect(Collectors.toList());
    }

    public RoomReservationResponseDto updateRoomReservation(UUID reservationId, LocalDateTime newStart, LocalDateTime newEnd) {
        var reservation = roomReservationDao.findReservationByRoomReservationId(reservationId);
        var user = authenticationService.getAuthenticatedUser();
        authenticationService.checkAuthorizationBetweenUserAndRoomReservation(user, reservation);

        // Use existing times if any of the new ones are null
        LocalDateTime start = newStart != null ? newStart : reservation.getReservedFrom();
        LocalDateTime end = newEnd != null ? newEnd : reservation.getReservedTo();

        roomReservationDataIntegrity.validateTimeRange(start, end);
        roomReservationDataIntegrity.checkForConflictingReservationsExceptSelf(reservation.getRoom(), start, end, reservationId);

        reservation.setReservedFrom(start);
        reservation.setReservedTo(end);
        reservation.setUpdatedAt(LocalDateTime.now());

        var updated = roomReservationDao.save(reservation);
        return roomReservationMapper.toRoomResponseDto(updated);
    }

    public List<RoomReservationResponseDto> findAllUserReservations(ReservationStatus reservationStatus, LocalDateTime startTime, LocalDateTime endTime) {
        var user = authenticationService.getAuthenticatedUser();

        List<RoomReservation> reservations = roomReservationDao.findUserReservationsByFilters(
                user.getId(), reservationStatus, startTime, endTime
        );

        reservations.forEach(this::updateStatusIfExpired);

        return reservations.stream()
                .map(roomReservationMapper::toRoomResponseDto)
                .collect(Collectors.toList());
    }

    private void updateStatusIfExpired(RoomReservation reservation) {
        if (reservation.getReservationStatus() == ReservationStatus.AKTYVI &&
                reservation.getReservedTo().isBefore(LocalDateTime.now()) &&
                reservation.getDeletedAt() == null) {
            reservation.setReservationStatus(ReservationStatus.PASIBAIGUSI);
        }
    }

    public void deleteReservationByRoomReservationId(UUID roomReservationId) {
        roomDataIntegrity.validateId(roomReservationId);

        var user = authenticationService.getAuthenticatedUser();
        var reservation = roomReservationDao.findReservationByRoomReservationId(roomReservationId);

        authenticationService.checkAuthorizationBetweenUserAndRoomReservation(user, reservation);

        reservation.setDeletedAt(LocalDateTime.now());
        reservation.setReservationStatus(ReservationStatus.ATŠAUKTA);
        roomReservationDao.save(reservation);
    }

    public RoomReservationResponseDto reserveRoom(UUID roomId, LocalDateTime startTime, LocalDateTime endTime) {
        roomDataIntegrity.validateId(roomId);
        roomReservationDataIntegrity.validateTimeRange(startTime, endTime);

        var user = authenticationService.getAuthenticatedUser();
        var room = roomService.getRoomById(roomId);

        roomReservationDataIntegrity.checkForConflictingReservations(room, startTime, endTime);

        var reservation = roomReservationMapper.toEntity(room, user, startTime, endTime);
        reservation.setReservationStatus(ReservationStatus.AKTYVI);
        return roomReservationMapper.toRoomResponseDto(roomReservationDao.save(reservation));
    }

    public List<RoomReservation> findReservationsStartingBetween(LocalDateTime start, LocalDateTime end) {
        return roomReservationDao.findReservationsStartingBetween(start, end );
    }

    public List<RoomReservationTimeRangeDto> getReservedTimeRangesForRoom(UUID roomId, UUID excludeReservationId) {
        roomDataIntegrity.validateId(roomId);

        return roomReservationDao.findByRoomId(roomId).stream()
                .filter( r -> excludeReservationId == null || !r.getId().equals(excludeReservationId))
                .map(roomReservationMapper::toTimeRangeDto)
                .collect(Collectors.toList());
    }
}
