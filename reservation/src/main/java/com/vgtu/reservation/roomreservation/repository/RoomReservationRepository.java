package com.vgtu.reservation.roomreservation.repository;

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

    @Transactional
    void deleteById(UUID id);

    @Query("SELECT r FROM RoomReservation r WHERE r.room.id = :roomId " +
            "AND r.deletedAt IS NULL " +
            "AND ((r.reservedFrom < :endTime AND r.reservedTo > :startTime))")
    List<RoomReservation> findConflictingReservations (
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

}
