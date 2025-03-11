package com.vgtu.reservation.carreservation.service;

import com.vgtu.reservation.carreservation.dto.CarReservationResponseDto;
import com.vgtu.reservation.carreservation.entity.CarReservation;
import com.vgtu.reservation.carreservation.mapper.CarReservationMapper;
import com.vgtu.reservation.carreservation.dao.CarReservationDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/*
 * Contains the business logic for the car reservation functionality
 */

@Service
@AllArgsConstructor
public class CarReservationService {

    private final CarReservationMapper carReservationMapper;
    private final CarReservationDao carReservationDao;

    public List<CarReservationResponseDto> findAllUserReservations(UUID userId) {

        List<CarReservation> reservations = carReservationDao.findAllUserReservations(userId);

        return reservations.stream()
                .map(carReservationMapper::toCarResponseDto)
                .collect(Collectors.toList());
    }
}
