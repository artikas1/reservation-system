package com.vgtu.reservation.room.service;

import com.vgtu.reservation.common.type.Address;
import com.vgtu.reservation.room.dao.RoomDao;
import com.vgtu.reservation.room.entity.Room;
import com.vgtu.reservation.room.integrity.RoomDataIntegrity;
import com.vgtu.reservation.room.repository.RoomRepository;
import com.vgtu.reservation.room.type.RoomType;
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

    public List<Room> getAvailableRooms(LocalDateTime startTime, LocalDateTime endTime, RoomType roomType, Address address) {
        List<UUID> reservedRoomIds = roomReservationRepository.findReservedRoomIdsBetween(startTime, endTime);
        List<Room> rooms;

        if (reservedRoomIds.isEmpty()) {
            rooms = roomRepository.findAll(); // all rooms are free
        } else {
            rooms = roomRepository.findByIdNotIn(reservedRoomIds);
        }

        if(roomType != null) {
            rooms = rooms.stream()
                    .filter(room -> room.getRoomType() == roomType)
                    .toList();
        }

        if (address != null) {
            rooms = rooms.stream()
                    .filter(room -> room.getAddress() == address)
                    .toList();
        }

        return rooms;
    }
}
