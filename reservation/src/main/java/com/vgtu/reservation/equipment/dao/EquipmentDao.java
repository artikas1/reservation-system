package com.vgtu.reservation.equipment.dao;

import com.vgtu.reservation.common.exception.EquipmentNotFoundException;
import com.vgtu.reservation.equipment.entity.Equipment;
import com.vgtu.reservation.equipment.integrity.EquipmentDataIntegrity;
import com.vgtu.reservation.equipment.repository.EquipmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class EquipmentDao {

    private final EquipmentDataIntegrity equipmentDataIntegrity;
    private final EquipmentRepository equipmentRepository;

    public Equipment createEquipment(Equipment equipment) {
        equipmentDataIntegrity.validateEquipment(equipment);

        return equipmentRepository.save(equipment);
    }

    public Equipment getEquipmentById(UUID id) {
        equipmentDataIntegrity.validateId(id);

        return equipmentRepository.findById(id).orElseThrow(() -> new EquipmentNotFoundException("Equipment not found"));
    }

    public void save(Equipment equipment) {
        equipmentRepository.save(equipment);
    }
}
