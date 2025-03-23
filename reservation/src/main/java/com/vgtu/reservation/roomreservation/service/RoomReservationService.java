package com.vgtu.reservation.roomreservation.service;

import com.vgtu.reservation.auth.service.authentication.AuthenticationService;
import com.vgtu.reservation.roomreservation.dao.RoomReservationDao;
import com.vgtu.reservation.roomreservation.dto.RoomReservationResponseDto;
import com.vgtu.reservation.roomreservation.mapper.RoomReservationMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoomReservationService {

    private final RoomReservationMapper roomReservationMapper;
    private final AuthenticationService authenticationService;
    private final RoomReservationDao roomReservationDao;

    public List<RoomReservationResponseDto> findAllUserReservations() {
        var user = authenticationService.getAuthenticatedUser();

        return roomReservationDao.findAllUserReservations(user.getId())
                .stream().map(roomReservationMapper::toRoomResponseDto)
                .collect(Collectors.toList());
    }

}
