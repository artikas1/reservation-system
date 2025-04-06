package com.vgtu.reservation.room.dao;

import com.vgtu.reservation.common.exception.RoomNotFoundException;
import com.vgtu.reservation.room.entity.Room;
import com.vgtu.reservation.room.integrity.RoomDataIntegrity;
import com.vgtu.reservation.room.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class RoomDao {

    private final RoomDataIntegrity roomDataIntegrity;
    private final RoomRepository roomRepository;

    public Room getRoomById(UUID id) {
        roomDataIntegrity.validateId(id);

        return roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException("Room not found"));
    }

}
