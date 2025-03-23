package com.vgtu.reservation.carreservation.controller;

import com.vgtu.reservation.carreservation.dto.CarReservationResponseDto;
import com.vgtu.reservation.carreservation.service.CarReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/car-reservation")
@RestController
@AllArgsConstructor
public class CarReservationController {

    private final CarReservationService carReservationService;

    @Operation(summary = "Get all user car reservations", description = "Retrieves all car reservations made by a specific user")
    @GetMapping("/user")
    public ResponseEntity<List<CarReservationResponseDto>> findAllUserReservations() {

        List<CarReservationResponseDto> reservations = carReservationService.findAllUserReservations();
        return ResponseEntity.ok(reservations);
    }

    @Operation(summary ="Delete car reservation", description = "Delete car reservation by car id")
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteReservationByCarId(
            @Parameter(description = "ID of the reserved car to delete") @RequestParam UUID carId) {
        carReservationService.deleteReservationByCarId(carId);
        return ResponseEntity.ok().build();
    }
}
