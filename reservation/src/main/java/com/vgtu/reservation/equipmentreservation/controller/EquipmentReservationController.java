package com.vgtu.reservation.equipmentreservation.controller;

import com.vgtu.reservation.common.type.ReservationStatus;
import com.vgtu.reservation.equipmentreservation.dto.EquipmentReservationResponseDto;
import com.vgtu.reservation.equipmentreservation.dto.EquipmentReservationTimeRangeDto;
import com.vgtu.reservation.equipmentreservation.service.EquipmentReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Used to define endpoints for equipment reservation
 */
@RequestMapping("/equipment-reservation")
@RestController
@AllArgsConstructor
public class EquipmentReservationController {

    private final EquipmentReservationService equipmentReservationService;

    @Operation(summary = "Reserve equipment", description = "Reserves equipment by its ID")
    @PostMapping("/{equipmentId}")
    public ResponseEntity<EquipmentReservationResponseDto> reserveEquipment(
            @Parameter(description = "ID of the equipment to reserve") @PathVariable UUID equipmentId,
            @Parameter(description = "Reservation start time") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "Reservation end time") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        EquipmentReservationResponseDto reservation = equipmentReservationService.reserveEquipment(equipmentId, startTime, endTime);
        return ResponseEntity.ok(reservation);

    }

    @Operation(summary = "Get all active user equipment reservations", description = "Retrieves all active equipment reservations made by a specific user")
    @GetMapping("/user")
    public ResponseEntity<List<EquipmentReservationResponseDto>> findAllActiveUserReservations() {
        List<EquipmentReservationResponseDto> reservations = equipmentReservationService.findAllActiveUserReservations();
        return ResponseEntity.ok(reservations);
    }

    @Operation(summary = "Update an equipment reservation", description = "Update reservation time for an equipment reservation")
    @PutMapping("/{reservationId}")
    public ResponseEntity<EquipmentReservationResponseDto> uppdateEquipmentReservation(
            @Parameter(description = "Reservation ID") @PathVariable UUID reservationId,
            @Parameter(description = "New start time") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newStartTime,
            @Parameter(description = "New end time") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newEndTime) {

        EquipmentReservationResponseDto updateReservation = equipmentReservationService.updateEquipmentReservation(reservationId, newStartTime, newEndTime);
        return ResponseEntity.ok(updateReservation);
    }

    @Operation(summary = "Delete equipment reservation", description = "Delete equipment reservation by equipment reservation id")
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteReservationByEquipmentReservationId(
            @Parameter(description = "ID of the equipment reservation to delete") @RequestParam UUID equipmentReservationId) {
        equipmentReservationService.deleteReservationByEquipmentReservationId(equipmentReservationId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all user equipment reservations", description = "Retrieves all history of equipment reservations made by a specific user")
    @GetMapping("/user/history")
    public ResponseEntity<List<EquipmentReservationResponseDto>> findAllUserReservations(
            @Parameter(description = "AKTYVI, PASIBAIGUSI or ATŠAUKTA") @RequestParam(required = false) ReservationStatus reservationStatus,
            @Parameter(description = "Reservation start time") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "Reservation end time") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        List<EquipmentReservationResponseDto> reservations = equipmentReservationService.findAllUserReservations(reservationStatus, startTime, endTime);
        return ResponseEntity.ok(reservations);
    }

    @Operation(summary = "Get equipment reservation times ranges for a specific equipment", description = "Used for disabling already reserved times in UI")
    @GetMapping("/equipment/{equipmentId}/time-ranges")
    public ResponseEntity<List<EquipmentReservationTimeRangeDto>> getEquipmentReservationTimeRanges(
            @Parameter(description = "ID of the equipment with all its reservations") @PathVariable UUID equipmentId,
            @Parameter(description = "ID of the reservation to exclude") @RequestParam(required = false) UUID excludeReservationId) {

        List<EquipmentReservationTimeRangeDto> timeRanges = equipmentReservationService.getReservedTimeRangesForEquipment(equipmentId, excludeReservationId);
        return ResponseEntity.ok(timeRanges);
    }
}
