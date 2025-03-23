package com.vgtu.reservation.equipment.dao;

import com.vgtu.reservation.common.exception.exceptions.EquipmentNotFoundException;
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

    public Equipment getEquipmentById(UUID id) {
        equipmentDataIntegrity.validateId(id);

        return equipmentRepository.findById(id).orElseThrow(() -> new EquipmentNotFoundException("Equipment not found"));
    }

}
