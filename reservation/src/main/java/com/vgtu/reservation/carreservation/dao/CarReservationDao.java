package com.vgtu.reservation.carreservation.dao;

import com.vgtu.reservation.carreservation.entity.CarReservation;
import com.vgtu.reservation.carreservation.repository.CarReservationRepository;
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

    private final CarReservationRepository carReservationRepository;

    public List<CarReservation> findAllUserReservations(UUID userId) {
        return carReservationRepository.findAllByUserId(userId);
    }
}
