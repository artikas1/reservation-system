package com.vgtu.reservation.room.service;

import com.vgtu.reservation.room.dao.RoomDao;
import com.vgtu.reservation.room.entity.Room;
import com.vgtu.reservation.room.integrity.RoomDataIntegrity;
import com.vgtu.reservation.room.repository.RoomRepository;
import com.vgtu.reservation.roomreservation.repository.RoomReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Contains the business logic for the room functionality
 */
@Service
@AllArgsConstructor
public class RoomService {

    private final RoomDao roomDao;
    private final RoomRepository roomRepository;
    private final RoomDataIntegrity roomDataIntegrity;
    private final RoomReservationRepository roomReservationRepository;

    public List<Room> getRoom() {
        return roomRepository.findAll();
    }

    public Room getRoomById(UUID id) {
        roomDataIntegrity.validateId(id);

        return roomDao.getRoomById(id);
    }

    public List<Room> getAvailableRooms(LocalDateTime startTime, LocalDateTime endTime) {
        List<UUID> reservedRoomIds = roomReservationRepository.findReservedRoomIdsBetween(startTime, endTime);

        if (reservedRoomIds.isEmpty()) {
            return roomRepository.findAll();
        }

        return roomRepository.findByIdNotIn(reservedRoomIds);
    }
}
