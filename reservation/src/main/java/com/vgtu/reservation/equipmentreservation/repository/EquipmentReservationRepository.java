package com.vgtu.reservation.equipmentreservation.repository;

import com.vgtu.reservation.common.type.ReservationStatus;
import com.vgtu.reservation.equipmentreservation.entity.EquipmentReservation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing EquipmentReservation entity.
 * Provides standard CRUD operations as well as custom queries.
 */
@Repository
public interface EquipmentReservationRepository extends JpaRepository<EquipmentReservation, UUID> {

    List<EquipmentReservation> findByUserId(UUID userId);

    List<EquipmentReservation> findByEquipmentIdAndDeletedAtIsNull(UUID equipmentId);

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
                  AND (:#{#reservationStatus == null} = true OR r.reservationStatus = :reservationStatus)
                  AND (:#{#startTime == null} = true OR r.reservedFrom >= :startTime)
                  AND (:#{#endTime == null} = true OR r.reservedTo <= :endTime)
            """)
    List<EquipmentReservation> findUserReservationsByFilters(
            @Param("userId") UUID userId,
            @Param("reservationStatus") @Nullable ReservationStatus reservationStatus,
            @Param("startTime") @Nullable LocalDateTime startTime,
            @Param("endTime") @Nullable LocalDateTime endTime
    );

    @Query("""
                SELECT r FROM EquipmentReservation r
                JOIN FETCH r.user
                WHERE r.reservationStatus = 'AKTYVI'
                  AND r.reservedFrom BETWEEN :start and :end
                  AND r.deletedAt IS NULL
            """)
    List<EquipmentReservation> findReservationsStartingBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
            
            SELECT r FROM EquipmentReservation r
                WHERE r.equipment.id = :equipmentId
                  AND r.deletedAt IS NULL
                  AND r.id <> :reservationId
                  AND ((r.reservedFrom < :endTime AND r.reservedTo > :startTime))
            """)
    List<EquipmentReservation> findConflictingReservationsExceptSelf(
            @Param("equipmentId") UUID equipmentId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("reservationId") UUID reservationId
    );

}