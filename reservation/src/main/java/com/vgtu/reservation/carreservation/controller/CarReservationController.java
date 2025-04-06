package com.vgtu.reservation.carreservation.controller;

import com.vgtu.reservation.carreservation.dto.CarReservationResponseDto;
import com.vgtu.reservation.carreservation.service.CarReservationService;
import com.vgtu.reservation.common.type.ReservationStatus;
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
 * Used to define endpoints for car reservation
 */
@RequestMapping("/car-reservation")
@RestController
@AllArgsConstructor
public class CarReservationController {

    private final CarReservationService carReservationService;

    @Operation(summary = "Reserve a car", description = "Reserve a car by its ID")
    @PostMapping("/{carId}")
    public ResponseEntity<CarReservationResponseDto> reserveCar(
            @Parameter(description = "ID of the car to reserve") @PathVariable UUID carId,
            @Parameter(description = "Reservation start time") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "Reservation end time") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        CarReservationResponseDto reservation = carReservationService.reserveCar(carId, startTime, endTime);
        return ResponseEntity.ok(reservation);
    }

    @Operation(summary ="Delete car reservation", description = "Delete car reservation by car reservation id")
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteReservationByCarReservationId(
            @Parameter(description = "ID of the car reservation to delete") @RequestParam UUID carReservationId) {
        carReservationService.deleteReservationByCarReservationId(carReservationId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all active user car reservations", description = "Retrieves all active car reservations made by a specific user")
    @GetMapping("/user")
    public ResponseEntity<List<CarReservationResponseDto>> findAllActiveUserReservations() {
        List<CarReservationResponseDto> reservations = carReservationService.findAllActiveUserReservations();
        return ResponseEntity.ok(reservations);
    }

    @Operation(summary = "Get all user car reservations", description = "Retrieves all history of car reservations made by a specific user")
    @GetMapping("/user/history")
    public ResponseEntity<List<CarReservationResponseDto>> findAllUserReservations(
            @RequestParam(required = false)ReservationStatus reservationStatus) {

        List<CarReservationResponseDto> reservations = carReservationService.findAllUserReservations(reservationStatus);
        return ResponseEntity.ok(reservations);
    }

}
