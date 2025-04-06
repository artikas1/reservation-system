package com.vgtu.reservation.roomreservation.repository;

import com.vgtu.reservation.common.type.ReservationStatus;
import com.vgtu.reservation.roomreservation.entity.RoomReservation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Provides the interface to interact with the database for RoomReservation entity
 */
@Repository
public interface RoomReservationRepository extends JpaRepository<RoomReservation, UUID> {

    List<RoomReservation> findByUserId(UUID userId);

    @Query("""
                SELECT r FROM RoomReservation r
                WHERE r.user.id = :userId
                AND r.deletedAt IS NULL
                AND r.reservedTo > CURRENT_TIMESTAMP
            """)
    List<RoomReservation> findActiveByUserId(@Param("userId") UUID userId);

    @Transactional
    void deleteById(UUID id);

    @Query("SELECT r FROM RoomReservation r WHERE r.room.id = :roomId " +
            "AND r.deletedAt IS NULL " +
            "AND ((r.reservedFrom < :endTime AND r.reservedTo > :startTime))")
    List<RoomReservation> findConflictingReservations(
            @Param("roomId") UUID roomId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Query("SELECT DISTINCT r.room.id FROM RoomReservation r " +
            "WHERE r.deletedAt IS NULL " +
            "AND ((r.reservedFrom < :endTime AND r.reservedTo > :startTime))")
    List<UUID> findReservedRoomIdsBetween(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Query("""
                SELECT r FROM RoomReservation r
                WHERE r.user.id = :userId 
                AND r.reservationStatus = :reservationStatus
            """)
    List<RoomReservation> findByUserIdAndReservationStatus(@Param("userId") UUID userId, @Param("reservationStatus") ReservationStatus reservationStatus);
}
