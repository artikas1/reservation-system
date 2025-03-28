package com.vgtu.reservation.room.service;

import com.vgtu.reservation.room.dao.RoomDao;
import com.vgtu.reservation.room.entity.Room;
import com.vgtu.reservation.room.integrity.RoomDataIntegrity;
import com.vgtu.reservation.room.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RoomService {

    private final RoomDao roomDao;
    private final RoomRepository roomRepository;
    private final RoomDataIntegrity roomDataIntegrity;

    public List<Room> getRoom() {
        return roomRepository.findAll();
    }

    public Room getRoomById(UUID id) {
        roomDataIntegrity.validateId(id);

        return roomDao.getRoomById(id);
    }
}
