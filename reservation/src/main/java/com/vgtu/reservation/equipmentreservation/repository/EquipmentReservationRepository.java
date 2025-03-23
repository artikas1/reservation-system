package com.vgtu.reservation.equipmentreservation.repository;

import com.vgtu.reservation.equipmentreservation.entity.EquipmentReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Provides the interface to interact with the database for EquipmentReservation entity
 */
@Repository
public interface EquipmentReservationRepository extends JpaRepository<EquipmentReservation, UUID> {

    List<EquipmentReservation> findByUserId(UUID userId);

}
