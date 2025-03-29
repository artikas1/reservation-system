package com.vgtu.reservation.carreservation.service;

import com.vgtu.reservation.auth.service.authentication.AuthenticationService;
import com.vgtu.reservation.car.integrity.CarDataIntegrity;
import com.vgtu.reservation.car.service.CarService;
import com.vgtu.reservation.carreservation.dto.CarReservationResponseDto;
import com.vgtu.reservation.carreservation.integrity.CarReservationDataIntegrity;
import com.vgtu.reservation.carreservation.mapper.CarReservationMapper;
import com.vgtu.reservation.carreservation.dao.CarReservationDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Contains the business logic for the car reservation functionality
 */
@Service
@AllArgsConstructor
public class CarReservationService {

    private final CarDataIntegrity carDataIntegrity;
    private final CarReservationMapper carReservationMapper;
    private final AuthenticationService authenticationService;
    private final CarReservationDao carReservationDao;
    private final CarService carService;
    private final CarReservationDataIntegrity carReservationDataIntegrity;

    public List<CarReservationResponseDto> findAllUserReservations() {
        var user = authenticationService.getAuthenticatedUser();

        return carReservationDao.findAllUserReservations(user.getId())
                .stream().map(carReservationMapper::toCarResponseDto)
                .collect(Collectors.toList());
    }

    public void deleteReservationByCarReservationId(UUID carReservationId) {
        carDataIntegrity.validateId(carReservationId);

        var user = authenticationService.getAuthenticatedUser();
        var reservation = carReservationDao.findReservationByCarReservationId(carReservationId);

        authenticationService.checkAuthorizationBetweenUserAndCarReservation(user, reservation);

        carReservationDao.deleteCarReservationByCarReservationId(carReservationId);
    }

    public CarReservationResponseDto reserveCar(UUID carId, LocalDateTime startTime, LocalDateTime endTime) {
        carDataIntegrity.validateId(carId);
        carReservationDataIntegrity.validateTimeRange(startTime, endTime);

        var user = authenticationService.getAuthenticatedUser();
        var car = carService.getCarById(carId);

        carReservationDataIntegrity.checkForConflictingReservations(car, startTime, endTime);

        var reservation = carReservationMapper.toEntity(car, user, startTime, endTime);
        return carReservationMapper.toCarResponseDto(carReservationDao.save(reservation));
    }
}
