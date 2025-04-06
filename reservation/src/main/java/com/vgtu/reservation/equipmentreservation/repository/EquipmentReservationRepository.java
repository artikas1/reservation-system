package com.vgtu.reservation.equipmentreservation.repository;

import com.vgtu.reservation.common.type.ReservationStatus;
import com.vgtu.reservation.equipmentreservation.entity.EquipmentReservation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Provides the interface to interact with the database for EquipmentReservation entity
 */
@Repository
public interface EquipmentReservationRepository extends JpaRepository<EquipmentReservation, UUID> {

    List<EquipmentReservation> findByUserId(UUID userId);

    @Query("""
                SELECT r FROM EquipmentReservation r
                WHERE r.user.id = :userId
                AND r.deletedAt IS NULL
                AND r.reservedTo > CURRENT_TIMESTAMP
            """)
    List<EquipmentReservation> findActiveByUserId(@Param("userId") UUID userId);

    @Transactional
    void deleteById(UUID id);

    @Query("SELECT r FROM EquipmentReservation r WHERE r.equipment.id = :equipmentId " +
            "AND r.deletedAt IS NULL " +
            "AND ((r.reservedFrom < :endTime AND r.reservedTo > :startTime))")
    List<EquipmentReservation> findConflictingReservations(
            @Param("equipmentId") UUID equipmentId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Query("SELECT DISTINCT r.equipment.id FROM EquipmentReservation r " +
            "WHERE r.deletedAt IS NULL " +
            "AND ((r.reservedFrom < :endTime AND r.reservedTo > :startTime)) ")
    List<UUID> findReservedEquipmentIdsBetween(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Query("""
                SELECT r FROM EquipmentReservation r
                WHERE r.user.id = :userId
                AND r.reservationStatus = :reservationStatus
            """)
    List<EquipmentReservation> findByUserIdAndReservationStatus(@Param("userId") UUID userId, @Param("reservationStatus") ReservationStatus reservationStatus);

}