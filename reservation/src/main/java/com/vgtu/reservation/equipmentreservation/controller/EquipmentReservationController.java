package com.vgtu.reservation.equipmentreservation.controller;

import com.vgtu.reservation.equipmentreservation.dto.EquipmentReservationResponseDto;
import com.vgtu.reservation.equipmentreservation.service.EquipmentReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @Operation(summary= "Delete equipment reservation", description = "Delete equipment reservation by equipment id")
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteReservationByEquipmentId(
            @Parameter(description = "ID of the reserved equipment item to delete") @RequestParam UUID equipmentId) {
        equipmentReservationService.deleteReservationByEquipmentId(equipmentId);
        return ResponseEntity.ok().build();
    }
}
