package com.vgtu.reservation.roomreservation.controller;

import com.vgtu.reservation.roomreservation.dto.RoomReservationResponseDto;
import com.vgtu.reservation.roomreservation.service.RoomReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @Operation(summary = "Delete room reservation", description = "Delete room reservation by room id")
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteReservationByRoomId(
            @Parameter(description = "ID of the reserved room to delete") @RequestParam UUID roomId) {
        roomReservationService.deleteReservationByRoomId(roomId);
        return ResponseEntity.ok().build();
    }
}
