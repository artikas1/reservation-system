package com.vgtu.reservation.carreservation.controller;

import com.vgtu.reservation.carreservation.dto.CarReservationResponseDto;
import com.vgtu.reservation.carreservation.service.CarReservationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequestMapping("/car-reservation")
@RestController
@AllArgsConstructor
public class CarReservationController {

    private final CarReservationService carReservationService;

    @Operation(summary = "Get all user reservations", description = "Retrieves all reservations made by a specific user")
    @GetMapping("/user")
    public ResponseEntity<List<CarReservationResponseDto>> findAllUserReservations(
            @RequestParam UUID userId) {
        System.out.println("userId: " + userId);
        List<CarReservationResponseDto> reservations = carReservationService.findAllUserReservations(userId);
        return ResponseEntity.ok(reservations);
    }

}
