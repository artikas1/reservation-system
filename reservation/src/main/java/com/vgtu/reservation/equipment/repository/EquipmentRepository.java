package com.vgtu.reservation.equipment.repository;

import com.vgtu.reservation.equipment.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, UUID> {
    List<Equipment> findByIdNotIn(List<UUID> reservedEquipmentIds);
}
