package com.vgtu.reservation.equipment.service;

import com.vgtu.reservation.equipment.entity.Equipment;
import com.vgtu.reservation.equipment.repository.EquipmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    public List<Equipment> getEquipment() {
        return equipmentRepository.findAll();
    }

}
