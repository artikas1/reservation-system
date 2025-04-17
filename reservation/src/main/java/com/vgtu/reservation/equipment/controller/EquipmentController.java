package com.vgtu.reservation.equipment.controller;

import com.vgtu.reservation.common.type.Address;
import com.vgtu.reservation.equipment.dto.EquipmentResponseDto;
import com.vgtu.reservation.equipment.entity.Equipment;
import com.vgtu.reservation.equipment.mapper.EquipmentMapper;
import com.vgtu.reservation.equipment.service.EquipmentService;
import com.vgtu.reservation.equipment.type.EquipmentType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Used to define endpoints for equipment
 */
@RequestMapping("/equipment")
@AllArgsConstructor
@RestController
public class EquipmentController {

    private final EquipmentService equipmentService;
    private final EquipmentMapper equipmentMapper;

    @Operation(summary = "Create a new equipment", description = "Creates a new equipment in the database")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EquipmentResponseDto> createEquipment(
            @RequestParam("name") String name,
            @RequestParam(value = "manufacturer", required = false) String manufacturer,
            @RequestParam(value = "model", required = false) String model,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("equipmentType") EquipmentType equipmentType,
            @RequestParam("address") Address address,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            var request = equipmentMapper.toRequestDto(name, manufacturer, model, code, description, equipmentType, address, image);
            return ResponseEntity.status(HttpStatus.CREATED).body(equipmentService.createEquipment(request));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Delete an equipment", description = "Delete an enquipment from the database by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipment(
            @Parameter(description = "ID of the equipment") @PathVariable UUID id) {
        equipmentService.deleteEquipmentById(id);
        return ResponseEntity.ok().build();
    }

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
            @RequestParam(required = false) EquipmentType equipmentType,
            @RequestParam(required = false) Address address) {

        var availableEquipment = equipmentService.getAvailableEquipment(startTime, endTime, equipmentType, address);
        var result = availableEquipment.stream().map(equipmentMapper::toResponseDto).toList();

        return ResponseEntity.ok(result);
    }
}
