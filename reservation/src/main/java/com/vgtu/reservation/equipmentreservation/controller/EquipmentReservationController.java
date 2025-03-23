package com.vgtu.reservation.equipmentreservation.controller;

import com.vgtu.reservation.equipmentreservation.dto.EquipmentReservationResponseDto;
import com.vgtu.reservation.equipmentreservation.service.EquipmentReservationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/equipment-reservation")
@RestController
@AllArgsConstructor
public class EquipmentReservationController {

    private final EquipmentReservationService equipmentReservationService;

    @Operation(summary = "Get all user equipment reservations", description = "Retrieves all equipment reservations made by a specific user")
    @GetMapping("/user")
    public ResponseEntity<List<EquipmentReservationResponseDto>> findAllUserReservations() {

        List<EquipmentReservationResponseDto> reservations = equipmentReservationService.findAllUserReservations();
        return ResponseEntity.ok(reservations);
    }

}
