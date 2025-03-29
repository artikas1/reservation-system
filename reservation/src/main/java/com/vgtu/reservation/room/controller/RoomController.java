package com.vgtu.reservation.room.controller;

import com.vgtu.reservation.room.dto.RoomResponseDto;
import com.vgtu.reservation.room.entity.Room;
import com.vgtu.reservation.room.mapper.RoomMapper;
import com.vgtu.reservation.room.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Used to define endpoints for room
 */
@RequestMapping("/room")
@AllArgsConstructor
@RestController
public class RoomController {

    private final RoomService roomService;
    private final RoomMapper roomMapper;

    @Operation(summary = "Get all rooms", description = "Retrieves all rooms from database")
    @GetMapping("/all")
    public List<Room> getRoom() {
        return roomService.getRoom();
    }

    @Operation(summary = "Get all available rooms", description = "Retrieves all rooms from database that are available for reservation")
    @GetMapping("/available")
    public ResponseEntity<List<RoomResponseDto>> getAvailableRooms(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        var availableRooms = roomService.getAvailableRooms(startTime, endTime);
        var result = availableRooms.stream().map(roomMapper::toResponseDto).toList();

        return ResponseEntity.ok(result);
    }

}
