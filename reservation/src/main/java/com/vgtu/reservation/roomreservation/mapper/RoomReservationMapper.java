package com.vgtu.reservation.roomreservation.mapper;

import com.vgtu.reservation.room.mapper.RoomMapper;
import com.vgtu.reservation.roomreservation.dto.RoomReservationResponseDto;
import com.vgtu.reservation.roomreservation.entity.RoomReservation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Converts the RoomReservation entity to a RoomReservationResponseDto so it can be sent as a response
 */
@Component
@AllArgsConstructor
public class RoomReservationMapper {

    private final RoomMapper roomMapper;

    public RoomReservationResponseDto toRoomResponseDto(RoomReservation roomReservation) {
        return RoomReservationResponseDto.builder()
                .id(roomReservation.getId())
                .room(roomMapper.toResponseDto(roomReservation.getRoom()))
                .userId(roomReservation.getUser().getId())
                .reservedFrom(roomReservation.getReservedFrom())
                .reservedTo(roomReservation.getReservedTo())
                .createdAt(roomReservation.getCreatedAt())
                .updatedAt(roomReservation.getUpdatedAt())
                .deletedAt(roomReservation.getDeletedAt())
                .build();
    }

}
