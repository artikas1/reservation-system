package com.vgtu.reservation.carreservation.repository;

import com.vgtu.reservation.carreservation.entity.CarReservation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

/**
 * Provides the interface to interact with the database for CarReservation entity
 */
@Repository
public interface CarReservationRepository extends JpaRepository<CarReservation, UUID> {

    List<CarReservation> findByUserId(UUID userId);

    @Transactional
    void deleteCarReservationByCar_Id(UUID carId);

    @Transactional
    CarReservation findCarReservationByCar_Id(UUID carId);
}
