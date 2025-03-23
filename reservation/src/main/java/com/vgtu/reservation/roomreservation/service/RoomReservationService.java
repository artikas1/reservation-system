package com.vgtu.reservation.roomreservation.service;

import com.vgtu.reservation.auth.service.authentication.AuthenticationService;
import com.vgtu.reservation.room.integrity.RoomDataIntegrity;
import com.vgtu.reservation.room.service.RoomService;
import com.vgtu.reservation.roomreservation.dao.RoomReservationDao;
import com.vgtu.reservation.roomreservation.dto.RoomReservationResponseDto;
import com.vgtu.reservation.roomreservation.mapper.RoomReservationMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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

    public List<RoomReservationResponseDto> findAllUserReservations() {
        var user = authenticationService.getAuthenticatedUser();

        return roomReservationDao.findAllUserReservations(user.getId())
                .stream().map(roomReservationMapper::toRoomResponseDto)
                .collect(Collectors.toList());
    }

    public void deleteReservationByRoomId(UUID roomId) {
        roomDataIntegrity.validateId(roomId);

        var user = authenticationService.getAuthenticatedUser();
        //var room = roomService.getRoomById(roomId);
        var reservation = roomReservationDao.findReservationByRoomId(roomId);

        authenticationService.checkAuthorizationBetweenUserAndRoomReservation(user, reservation);

        //room.setAvailable(true);
        //roomDao.saveRoom(room);

        roomReservationDao.deleteReservationByRoomId(roomId);

    }

}
