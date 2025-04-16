package com.vgtu.reservation.room.controller;

import com.vgtu.reservation.common.type.Address;
import com.vgtu.reservation.room.dto.RoomResponseDto;
import com.vgtu.reservation.room.entity.Room;
import com.vgtu.reservation.room.mapper.RoomMapper;
import com.vgtu.reservation.room.service.RoomService;
import com.vgtu.reservation.room.type.RoomType;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Operation(summary = "Create a new room", description = "Creates a new room in the database")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RoomResponseDto> createRoom(
            @RequestParam("name") String name,
            @RequestParam("floor") String floor,
            @RequestParam(value = "roomNumber", required = false) String roomNumber,
            @RequestParam(value = "seats", required = false) String seats,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("roomType") RoomType roomType,
            @RequestParam("address") Address address,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            var request = roomMapper.toRequestDto(name, floor, roomNumber, seats, description, roomType, address, image);
            return ResponseEntity.status(HttpStatus.CREATED).body(roomService.createRoom(request));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get all rooms", description = "Retrieves all rooms from database")
    @GetMapping("/all")
    public List<Room> getRoom() {
        return roomService.getRoom();
    }

    @Operation(summary = "Get all available rooms", description = "Retrieves all rooms from database that are available for reservation")
    @GetMapping("/available")
    public ResponseEntity<List<RoomResponseDto>> getAvailableRooms(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(required = false) RoomType roomType,
            @RequestParam(required = false) Address address) {

        var availableRooms = roomService.getAvailableRooms(startTime, endTime, roomType, address);
        var result = availableRooms.stream().map(roomMapper::toResponseDto).toList();

        return ResponseEntity.ok(result);
    }

}
