package com.vgtu.reservation.roomreservation.controller;

import com.vgtu.reservation.roomreservation.dto.RoomReservationResponseDto;
import com.vgtu.reservation.roomreservation.service.RoomReservationService;
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
 * Used to define endpoints for room reservation
 */
@RequestMapping("/room-reservation")
@RestController
@AllArgsConstructor
public class RoomReservationController {

    private final RoomReservationService roomReservationService;

    @Operation(summary = "Reserve a room", description = "Reserves a room by its ID")
    @PostMapping("/{roomId}")
    public ResponseEntity<RoomReservationResponseDto> reserveRoom(
            @Parameter(description = "ID of the room to reserve") @PathVariable UUID roomId,
            @Parameter(description = "Reservation start time") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "Reservation end time") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        RoomReservationResponseDto reservation = roomReservationService.reserveRoom(roomId, startTime, endTime);
        return ResponseEntity.ok(reservation);
    }

    @Operation(summary = "Delete room reservation", description = "Delete room reservation by room reservation id")
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteReservationByRoomReservationId(
            @Parameter(description = "ID of the room reservation to delete") @RequestParam UUID roomReservationId) {
        roomReservationService.deleteReservationByRoomReservationId(roomReservationId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all active user room reservations", description = "Retrieves all active room reservations made by a specific user")
    @GetMapping("/user")
    public ResponseEntity<List<RoomReservationResponseDto>> findAllActiveUserReservations() {

        List<RoomReservationResponseDto> reservations = roomReservationService.findAllActiveUserReservations();
        return ResponseEntity.ok(reservations);
    }

}
