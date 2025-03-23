package com.vgtu.reservation.carreservation.service;

import com.vgtu.reservation.auth.service.authentication.AuthenticationService;
import com.vgtu.reservation.car.integrity.CarDataIntegrity;
import com.vgtu.reservation.car.service.CarService;
import com.vgtu.reservation.carreservation.dto.CarReservationResponseDto;
import com.vgtu.reservation.carreservation.mapper.CarReservationMapper;
import com.vgtu.reservation.carreservation.dao.CarReservationDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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

    public List<CarReservationResponseDto> findAllUserReservations() {
        var user = authenticationService.getAuthenticatedUser();

        return carReservationDao.findAllUserReservations(user.getId())
                .stream().map(carReservationMapper::toCarResponseDto)
                .collect(Collectors.toList());
    }

    public void deleteReservationByCarId(UUID carId) {
        carDataIntegrity.validateId(carId);

        var user = authenticationService.getAuthenticatedUser();
        //var car = carService.getCarById(carId);
        var reservation = carReservationDao.findReservationByCarId(carId);

        authenticationService.checkAuthorizationBetweenUserAndCarReservation(user, reservation);

//        car.setAvailable(true);
//        carDao.saveCar(car);

        carReservationDao.deleteCarReservationByCarId(carId);
    }

}
