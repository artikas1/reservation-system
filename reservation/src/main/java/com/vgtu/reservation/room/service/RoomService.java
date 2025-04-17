package com.vgtu.reservation.room.service;

import com.vgtu.reservation.auth.service.authentication.AuthenticationService;
import com.vgtu.reservation.common.type.Address;
import com.vgtu.reservation.room.dao.RoomDao;
import com.vgtu.reservation.room.dto.RoomRequestDto;
import com.vgtu.reservation.room.dto.RoomResponseDto;
import com.vgtu.reservation.room.entity.Room;
import com.vgtu.reservation.room.integrity.RoomDataIntegrity;
import com.vgtu.reservation.room.mapper.RoomMapper;
import com.vgtu.reservation.room.repository.RoomRepository;
import com.vgtu.reservation.room.type.RoomType;
import com.vgtu.reservation.roomreservation.repository.RoomReservationRepository;
import com.vgtu.reservation.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Contains the business logic for the room functionality
 */
@Service
@AllArgsConstructor
@Transactional
public class RoomService {

    private final RoomDao roomDao;
    private final RoomRepository roomRepository;
    private final RoomDataIntegrity roomDataIntegrity;
    private final RoomReservationRepository roomReservationRepository;
    private final RoomMapper roomMapper;
    private final AuthenticationService authenticationService;

    public RoomResponseDto createRoom(RoomRequestDto roomRequestDto) {
        roomDataIntegrity.validateRoomRequest(roomRequestDto);

        User user = authenticationService.getAuthenticatedUser();
        if (!user.isAdmin()) {
            throw new AccessDeniedException("Only admins can create rooms");
        }

        var room = roomMapper.toEntity(roomRequestDto);

        return roomMapper.toResponseDto(roomDao.createRoom(room));
    }

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
            rooms = roomRepository.findByDeletedAtIsNull(); // all rooms are free
        } else {
            rooms = roomRepository.findByIdNotInAndDeletedAtIsNull(reservedRoomIds);
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

    public RoomResponseDto updateRoomById(UUID id, RoomRequestDto roomRequestDto) {
        roomDataIntegrity.validateId(id);

        User user = authenticationService.getAuthenticatedUser();
        if (!user.isAdmin()) {
            throw new AccessDeniedException("Only admins can update rooms");
        }
        var room = roomDao.getRoomById(id);

        room.setName(roomRequestDto.getName());
        room.setFloor(roomRequestDto.getFloor());
        room.setRoomNumber(roomRequestDto.getRoomNumber());
        room.setSeats(roomRequestDto.getSeats());
        room.setDescription(roomRequestDto.getDescription());
        room.setRoomType(roomRequestDto.getRoomType());
        room.setAddress(roomRequestDto.getAddress());
        room.setImage(roomRequestDto.getImage());

        room.setUpdatedAt(LocalDateTime.now());

        return roomMapper.toResponseDto(roomDao.createRoom(room));
    }

    public void deleteRoomById(UUID id) {
        roomDataIntegrity.validateId(id);

        var user = authenticationService.getAuthenticatedUser();
        if(!user.isAdmin()) {
            throw new AccessDeniedException("Only admins can delete rooms");
        }

        var room = roomDao.getRoomById(id);

        room.setDeletedAt(LocalDateTime.now());

        roomDao.save(room);
    }

}
