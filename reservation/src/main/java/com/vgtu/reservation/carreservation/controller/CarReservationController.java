package com.vgtu.reservation.carreservation.controller;

import com.vgtu.reservation.carreservation.dto.CarReservationResponseDto;
import com.vgtu.reservation.carreservation.service.CarReservationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/car-reservation")
@RestController
@AllArgsConstructor
public class CarReservationController {

    private final CarReservationService carReservationService;

    @Operation(summary = "Get all user reservations", description = "Retrieves all reservations made by a specific user")
    @GetMapping("/user")
    public ResponseEntity<List<CarReservationResponseDto>> findAllUserReservations() {

        List<CarReservationResponseDto> reservations = carReservationService.findAllUserReservations();
        return ResponseEntity.ok(reservations);
    }

}
