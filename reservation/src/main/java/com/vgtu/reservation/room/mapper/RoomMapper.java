package com.vgtu.reservation.room.mapper;

import com.vgtu.reservation.common.type.Address;
import com.vgtu.reservation.room.dto.RoomRequestDto;
import com.vgtu.reservation.room.dto.RoomResponseDto;
import com.vgtu.reservation.room.entity.Room;
import com.vgtu.reservation.room.type.RoomType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class RoomMapper {

    public Room toEntity(RoomRequestDto roomRequestDto) {
        return Room.builder()
                .name(roomRequestDto.getName())
                .floor(roomRequestDto.getFloor())
                .roomNumber(roomRequestDto.getRoomNumber())
                .seats(roomRequestDto.getSeats())
                .description(roomRequestDto.getDescription())
                .createdAt(LocalDateTime.now())
                .roomType(roomRequestDto.getRoomType())
                .address(roomRequestDto.getAddress())
                .image(roomRequestDto.getImage())
                .build();
    }

    public RoomResponseDto toResponseDto(Room room) {
        return RoomResponseDto.builder()
                .id(room.getId())
                .name(room.getName())
                .floor(room.getFloor())
                .roomNumber(room.getRoomNumber())
                .seats(room.getSeats())
                .description(room.getDescription())
                .createdAt(room.getCreatedAt())
                .updatedAt(room.getUpdatedAt())
                .deletedAt(room.getDeletedAt())
                .roomType(room.getRoomType())
                .address(room.getAddress())
                .image(room.getImage())
                .build();
    }

    public RoomRequestDto toRequestDto(String name,
                                       String floor,
                                       String roomNumber,
                                       String seats,
                                       String description,
                                       RoomType roomType,
                                       Address address,
                                       MultipartFile image) throws IOException {
        return RoomRequestDto.builder()
                .name(name)
                .floor(floor)
                .roomNumber(roomNumber)
                .seats(seats)
                .description(description)
                .roomType(roomType)
                .address(address)
                .image(image.getBytes())
                .build();
    }

}
