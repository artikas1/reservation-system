package com.vgtu.reservation.roomreservation.service;

import com.vgtu.reservation.auth.service.authentication.AuthenticationService;
import com.vgtu.reservation.room.integrity.RoomDataIntegrity;
import com.vgtu.reservation.room.service.RoomService;
import com.vgtu.reservation.roomreservation.dao.RoomReservationDao;
import com.vgtu.reservation.roomreservation.dto.RoomReservationResponseDto;
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

    public void deleteReservationByRoomReservationId(UUID roomReservationId) {
        roomDataIntegrity.validateId(roomReservationId);

        var user = authenticationService.getAuthenticatedUser();
        var reservation = roomReservationDao.findReservationByRoomReservationId(roomReservationId);

        authenticationService.checkAuthorizationBetweenUserAndRoomReservation(user, reservation);

        reservation.setDeletedAt(LocalDateTime.now());
        roomReservationDao.save(reservation);
    }

    public RoomReservationResponseDto reserveRoom(UUID roomId, LocalDateTime startTime, LocalDateTime endTime) {
        roomDataIntegrity.validateId(roomId);
        roomReservationDataIntegrity.validateTimeRange(startTime, endTime);

        var user = authenticationService.getAuthenticatedUser();
        var room = roomService.getRoomById(roomId);

        roomReservationDataIntegrity.checkForConflictingReservations(room, startTime, endTime);

        var reservation = roomReservationMapper.toEntity(room, user, startTime, endTime);
        return roomReservationMapper.toRoomResponseDto(roomReservationDao.save(reservation));
    }

}
