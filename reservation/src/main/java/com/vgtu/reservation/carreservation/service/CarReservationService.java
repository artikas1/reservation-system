package com.vgtu.reservation.carreservation.service;

import com.vgtu.reservation.auth.service.authentication.AuthenticationService;
import com.vgtu.reservation.car.integrity.CarDataIntegrity;
import com.vgtu.reservation.car.service.CarService;
import com.vgtu.reservation.carreservation.dto.CarReservationResponseDto;
import com.vgtu.reservation.carreservation.entity.CarReservation;
import com.vgtu.reservation.carreservation.integrity.CarReservationDataIntegrity;
import com.vgtu.reservation.carreservation.mapper.CarReservationMapper;
import com.vgtu.reservation.carreservation.dao.CarReservationDao;
import com.vgtu.reservation.carreservation.type.ReservationStatus;
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

    public List<CarReservationResponseDto> findAllActiveUserReservations() {
        var user = authenticationService.getAuthenticatedUser();

        return carReservationDao.findAllActiveUserReservations(user.getId())
                .stream().map(carReservationMapper::toCarResponseDto)
                .collect(Collectors.toList());
    }

    public List<CarReservationResponseDto> findAllUserReservations() {
        var user = authenticationService.getAuthenticatedUser();

        var reservations = carReservationDao.findAllUserReservations(user.getId());

        reservations.forEach(this::updateStatusIfExpired);

        return reservations.stream()
                .map(carReservationMapper::toCarResponseDto)
                .collect(Collectors.toList());
    }

    private void updateStatusIfExpired(CarReservation reservation) {
        if (reservation.getReservationStatus() == ReservationStatus.AKTYVI &&
                reservation.getReservedTo().isBefore(LocalDateTime.now()) &&
                reservation.getDeletedAt() == null) {
            reservation.setReservationStatus(ReservationStatus.PASIBAIGUSI);
        }
    }

    public void deleteReservationByCarReservationId(UUID carReservationId) {
        carDataIntegrity.validateId(carReservationId);

        var user = authenticationService.getAuthenticatedUser();
        var reservation = carReservationDao.findReservationByCarReservationId(carReservationId);

        authenticationService.checkAuthorizationBetweenUserAndCarReservation(user, reservation);

        reservation.setDeletedAt(LocalDateTime.now());
        reservation.setReservationStatus(ReservationStatus.ATŠAUKTA);
        carReservationDao.save(reservation);
    }

    public CarReservationResponseDto reserveCar(UUID carId, LocalDateTime startTime, LocalDateTime endTime) {
        carDataIntegrity.validateId(carId);
        carReservationDataIntegrity.validateTimeRange(startTime, endTime);

        var user = authenticationService.getAuthenticatedUser();
        var car = carService.getCarById(carId);

        carReservationDataIntegrity.checkForConflictingReservations(car, startTime, endTime);

        var reservation = carReservationMapper.toEntity(car, user, startTime, endTime);
        reservation.setReservationStatus(ReservationStatus.AKTYVI);
        return carReservationMapper.toCarResponseDto(carReservationDao.save(reservation));
    }
}
