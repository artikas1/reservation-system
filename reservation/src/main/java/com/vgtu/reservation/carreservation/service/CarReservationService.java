package com.vgtu.reservation.carreservation.service;

import com.vgtu.reservation.auth.service.authentication.AuthenticationService;
import com.vgtu.reservation.carreservation.dto.CarReservationResponseDto;
import com.vgtu.reservation.carreservation.mapper.CarReservationMapper;
import com.vgtu.reservation.carreservation.dao.CarReservationDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Contains the business logic for the car reservation functionality
 */
@Service
@AllArgsConstructor
public class CarReservationService {

    private final CarReservationMapper carReservationMapper;
    private final AuthenticationService authenticationService;
    private final CarReservationDao carReservationDao;

    public List<CarReservationResponseDto> findAllUserReservations() {
        var user = authenticationService.getAuthenticatedUser();

        return carReservationDao.findAllUserReservations(user.getId())
                .stream().map(carReservationMapper::toCarResponseDto)
                .collect(Collectors.toList());
    }


}
