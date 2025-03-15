package com.vgtu.reservation.equipment.controller;

import com.vgtu.reservation.equipment.entity.Equipment;
import com.vgtu.reservation.equipment.service.EquipmentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
public class EquipmentController {

    private final EquipmentService equipmentService;

    @GetMapping("/equipment")
    public List<Equipment> getEquipment() {
        return equipmentService.getEquipment();
    }
}
