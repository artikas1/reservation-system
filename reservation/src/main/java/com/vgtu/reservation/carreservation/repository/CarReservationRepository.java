package com.vgtu.reservation.carreservation.repository;

import com.vgtu.reservation.carreservation.entity.CarReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

/*
 * Provides the interface to interact with the database for CarReservation entity
 */
@Repository
public interface CarReservationRepository extends JpaRepository<CarReservation, UUID> {

//    @Query("SELECT cr FROM CarReservation cr WHERE cr.user.id = :userId")
    List<CarReservation> findAllByUserId(@Param("userId") UUID userId);
}
