package com.vgtu.reservation.roomreservation.controller;

import com.vgtu.reservation.roomreservation.dto.RoomReservationResponseDto;
import com.vgtu.reservation.roomreservation.service.RoomReservationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/room-reservation")
@RestController
@AllArgsConstructor
public class RoomReservationController {

    private final RoomReservationService roomReservationService;

    @Operation(summary = "Get all user room reservations", description = "Retrieves all room reservations made by a specific user")
    @GetMapping("/user")
    public ResponseEntity<List<RoomReservationResponseDto>> findAllUserReservations() {

        List<RoomReservationResponseDto> reservations = roomReservationService.findAllUserReservations();
        return ResponseEntity.ok(reservations);
    }
}
