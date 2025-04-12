package com.vgtu.reservation.equipment.controller;

import com.vgtu.reservation.equipment.dto.EquipmentResponseDto;
import com.vgtu.reservation.equipment.entity.Equipment;
import com.vgtu.reservation.equipment.mapper.EquipmentMapper;
import com.vgtu.reservation.equipment.service.EquipmentService;
import com.vgtu.reservation.equipment.type.EquipmentType;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 *  Used to define endpoints for equipment
 */
@RequestMapping("/equipment")
@AllArgsConstructor
@RestController
public class EquipmentController {

    private final EquipmentService equipmentService;
    private final EquipmentMapper equipmentMapper;

    @Operation(summary = "Get all equipment", description = "Retrieves all equipment from database")
    @GetMapping("/all")
    public List<Equipment> getEquipment() {
        return equipmentService.getEquipment();
    }

    @Operation(summary = "Get all available equipment", description = "Retrieves all equipment from database that are available for reservation")
    @GetMapping("/available")
    public ResponseEntity<List<EquipmentResponseDto>> getAvailableEquipment(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(required = false) EquipmentType equipmentType) {

        var availableEquipment = equipmentService.getAvailableEquipment(startTime, endTime, equipmentType);
        var result = availableEquipment.stream().map(equipmentMapper::toResponseDto).toList();

        return ResponseEntity.ok(result);
    }
}
