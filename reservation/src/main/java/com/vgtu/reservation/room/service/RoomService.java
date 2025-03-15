package com.vgtu.reservation.room.service;

import com.vgtu.reservation.room.entity.Room;
import com.vgtu.reservation.room.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public List<Room> getRoom() {
        return roomRepository.findAll();
    }
}
