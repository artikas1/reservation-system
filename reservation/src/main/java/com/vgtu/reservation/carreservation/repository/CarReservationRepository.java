package com.vgtu.reservation.carreservation.repository;

import com.vgtu.reservation.carreservation.entity.CarReservation;
import com.vgtu.reservation.common.type.ReservationStatus;
import org.springframework.lang.Nullable;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

/**
 * Repository interface for managing CarReservation entity.
 * Provides standard CRUD operations as well as custom queries.
 */
@Repository
public interface CarReservationRepository extends JpaRepository<CarReservation, UUID> {

    List<CarReservation> findByUserId(UUID userId);

    List<CarReservation> findByCarIdAndDeletedAtIsNull(UUID carId);

    @Query("""
                SELECT r FROM CarReservation r
                WHERE r.user.id = :userId
                AND r.deletedAt IS NULL
                AND r.reservedTo > CURRENT_TIMESTAMP
            """)
    List<CarReservation> findActiveByUserId(@Param("userId") UUID userId);

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

    @Query("""
                SELECT r FROM CarReservation r
                WHERE r.car.id = :carId
                  AND r.deletedAt IS NULL
                  AND r.id <> :reservationId
                  AND ((r.reservedFrom < :endTime AND r.reservedTo > :startTime))
            """)
    List<CarReservation> findConflictingReservationsExceptSelf(
            @Param("carId") UUID carId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("reservationId") UUID reservationId
    );


    @Query("SELECT DISTINCT r.car.id FROM CarReservation r " +
            "WHERE r.deletedAt IS NULL " +
            "AND ((r.reservedFrom < :endTime AND r.reservedTo > :startTime))")
    List<UUID> findReservedCarIdsBetween(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Query("""
                SELECT r FROM CarReservation r
                WHERE r.user.id = :userId
                  AND (:#{#reservationStatus == null} = true OR r.reservationStatus = :reservationStatus)
                  AND (:#{#startTime == null} = true OR r.reservedFrom >= :startTime)
                  AND (:#{#endTime == null} = true OR r.reservedTo <= :endTime)
            """)
    List<CarReservation> findUserReservationsByFilters(
            @Param("userId") UUID userId,
            @Param("reservationStatus") @Nullable ReservationStatus reservationStatus,
            @Param("startTime") @Nullable LocalDateTime startTime,
            @Param("endTime") @Nullable LocalDateTime endTime
    );

    @Query("""
                SELECT r FROM CarReservation r
                JOIN FETCH r.user
                WHERE r.reservationStatus = 'AKTYVI'
                  AND r.reservedTo BETWEEN :start AND :end
                  AND r.deletedAt IS NULL
            """)
    List<CarReservation> findReservationsEndingBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
                SELECT r FROM CarReservation r
                JOIN FETCH r.user
                WHERE r.reservationStatus = 'AKTYVI'
                  AND r.reservedFrom BETWEEN :start AND :end
                  AND r.deletedAt IS NULL
            """)
    List<CarReservation> findReservationsStartingBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
                SELECT r FROM CarReservation r
                WHERE r.car.id = :carId
                  AND r.reservedFrom >= :start
                  AND r.reservedTo <= :end
                  AND r.deletedAt IS NULL
            """)
    List<CarReservation> findByCarIdAndTimeRange(
            @Param("carId") UUID carId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

}
