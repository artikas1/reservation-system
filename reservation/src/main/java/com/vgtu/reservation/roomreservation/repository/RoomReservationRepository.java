package com.vgtu.reservation.roomreservation.repository;

import com.vgtu.reservation.roomreservation.entity.RoomReservation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Provides the interface to interact with the database for RoomReservation entity
 */
@Repository
public interface RoomReservationRepository extends JpaRepository<RoomReservation, UUID> {

    List<RoomReservation> findByUserId(UUID userId);

    @Transactional
    void deleteRoomReservationByRoom_Id(UUID roomId);

    @Transactional
    RoomReservation findRoomReservationByRoom_Id(UUID roomId);
}
