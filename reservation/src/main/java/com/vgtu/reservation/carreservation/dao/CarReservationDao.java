package com.vgtu.reservation.carreservation.dao;

import com.vgtu.reservation.car.integrity.CarDataIntegrity;
import com.vgtu.reservation.carreservation.entity.CarReservation;
import com.vgtu.reservation.carreservation.integrity.CarReservationDataIntegrity;
import com.vgtu.reservation.carreservation.repository.CarReservationRepository;
import com.vgtu.reservation.common.type.ReservationStatus;
import com.vgtu.reservation.common.exception.CarReservationBadRequestException;
import com.vgtu.reservation.user.integrity.UserDataIntegrity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Interacts with the database to fetch data related to a car reservations
 */
@Service
@AllArgsConstructor
public class CarReservationDao {

    private final UserDataIntegrity userDataIntegrity;
    private final CarReservationRepository carReservationRepository;
    private final CarReservationDataIntegrity carReservationDataIntegrity;
    private final CarDataIntegrity carDataIntegrity;

    public CarReservation save(CarReservation carReservation) {
        carReservationDataIntegrity.validateCarReservation(carReservation);

        return carReservationRepository.save(carReservation);
    }

    public List<CarReservation> findAllUserReservations(UUID userId) {
        userDataIntegrity.validateId(userId);

        return carReservationRepository.findByUserId(userId);
    }

    public List<CarReservation> findAllActiveUserReservations(UUID userId) {
        userDataIntegrity.validateId(userId);

        return carReservationRepository.findActiveByUserId(userId);
    }

    public CarReservation findReservationByCarReservationId(UUID carReservationId) {
        carReservationDataIntegrity.validateId(carReservationId);

        return carReservationRepository.findById(carReservationId)
                .orElseThrow(() -> new CarReservationBadRequestException("Reservation not found"));
    }

    public void deleteCarReservationByCarReservationId(UUID carReservationId) {
        carReservationDataIntegrity.validateId(carReservationId);

        carReservationRepository.deleteById(carReservationId);
    }

    public List<CarReservation> findUserReservationsByFilters(UUID userId, ReservationStatus reservationStatus, LocalDateTime startTime, LocalDateTime endTime) {
        userDataIntegrity.validateId(userId);
        return carReservationRepository.findUserReservationsByFilters(userId, reservationStatus, startTime, endTime);
    }

    public List<CarReservation> findReservationsEndingBetween(LocalDateTime start, LocalDateTime end) {
        return carReservationRepository.findReservationsEndingBetween(start, end);
    }

    public List<CarReservation> findReservationsStartingBetween(LocalDateTime start, LocalDateTime end) {
        return carReservationRepository.findReservationsStartingBetween(start, end);
    }

    public List<CarReservation> findByCarId(UUID carId) {
        carDataIntegrity.validateId(carId);

        return carReservationRepository.findByCarIdAndDeletedAtIsNull(carId);
    }
}
