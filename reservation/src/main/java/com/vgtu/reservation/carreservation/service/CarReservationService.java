package com.vgtu.reservation.carreservation.service;

import com.vgtu.reservation.carreservation.dto.CarReservationResponseDto;
import com.vgtu.reservation.carreservation.entity.CarReservation;
import com.vgtu.reservation.carreservation.mapper.CarReservationMapper;
import com.vgtu.reservation.carreservation.dao.CarReservationDao;
import com.vgtu.reservation.carreservation.repository.CarReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

//    public List<CarReservationResponseDto> findAllUserReservations(UUID userId) {
//
//        //List<CarReservation> reservations = carReservationDao.findAllUserReservations(userId);
//
//        List<CarReservation> reservations = carReservationRepository.findByUserId(userId);
//
//        System.out.println("User reservation id - " + carReservation.getId());
//
//        return reservations.stream()
//                .map(carReservationMapper::toCarResponseDto)
//                .collect(Collectors.toList());
//    }

    @Autowired
    private CarReservationRepository carReservationRepository;

    public List<CarReservationResponseDto> findAllUserReservations(UUID userId) {
        List<CarReservation> reservations = carReservationRepository.findByUserId(userId); // Query reservations by user ID
        return reservations.stream()
                .map(carReservationMapper::toCarResponseDto) // Map entities to DTOs
                .collect(Collectors.toList());
    }
}
