package com.vgtu.reservation.carreservation.repository;

import com.vgtu.reservation.carreservation.entity.CarReservation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

/**
 * Provides the interface to interact with the database for CarReservation entity
 */
@Repository
public interface CarReservationRepository extends JpaRepository<CarReservation, UUID> {

    List<CarReservation> findByUserId(UUID userId);

    @Transactional
    void deleteById(UUID id);

    @Query("SELECT r FROM CarReservation r WHERE r.car.id = :carId " +
            "AND r.deletedAt IS NULL " +
            "AND ((r.reservedFrom < :endTime AND r.reservedTo > :startTime))")
    List<CarReservation> findConflictingReservations(
            @Param("carId") UUID carId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Query("SELECT DISTINCT r.car.id FROM CarReservation r " +
            "WHERE r.deletedAt IS NULL " +
            "AND ((r.reservedFrom < :endTime AND r.reservedTo > :startTime))")
    List<UUID> findReservedCarIdsBetween(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

}
