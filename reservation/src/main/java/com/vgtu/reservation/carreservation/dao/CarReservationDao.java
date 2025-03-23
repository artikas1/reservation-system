package com.vgtu.reservation.carreservation.dao;

import com.vgtu.reservation.carreservation.entity.CarReservation;
import com.vgtu.reservation.carreservation.integrity.CarReservationDataIntegrity;
import com.vgtu.reservation.carreservation.repository.CarReservationRepository;
import com.vgtu.reservation.user.integrity.UserDataIntegrity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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

    public List<CarReservation> findAllUserReservations(UUID userId) {
        userDataIntegrity.validateId(userId);

        return carReservationRepository.findByUserId(userId);
    }

    public CarReservation findReservationByCarId(UUID carId) {
        carReservationDataIntegrity.validateId(carId);

        return carReservationRepository.findCarReservationByCar_Id(carId);
    }

    public void deleteCarReservationByCarId(UUID carId) {
        carReservationDataIntegrity.validateId(carId);

        carReservationRepository.deleteCarReservationByCar_Id(carId);
    }
}
