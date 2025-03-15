package com.vgtu.reservation.room.controller;

import com.vgtu.reservation.room.entity.Room;
import com.vgtu.reservation.room.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
public class RoomController {

    private final RoomService roomService;

    @Operation(summary = "Get all rooms", description = "Retrieves all rooms from database")
    @GetMapping("/room")
    public List<Room> getRoom() {
        return roomService.getRoom();
    }

}
