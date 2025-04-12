package com.vgtu.reservation.roomreservation.mapper;

import com.vgtu.reservation.room.entity.Room;
import com.vgtu.reservation.room.mapper.RoomMapper;
import com.vgtu.reservation.roomreservation.dto.RoomReservationResponseDto;
import com.vgtu.reservation.roomreservation.dto.RoomReservationTimeRangeDto;
import com.vgtu.reservation.roomreservation.entity.RoomReservation;
import com.vgtu.reservation.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Converts the RoomReservation entity to a RoomReservationResponseDto so it can be sent as a response
 */
@Component
@AllArgsConstructor
public class RoomReservationMapper {

    private final RoomMapper roomMapper;

    public RoomReservation toEntity(Room room, User user, LocalDateTime startTime, LocalDateTime endTime) {
        return RoomReservation.builder()
                .room(room)
                .user(user)
                .reservedFrom(startTime)
                .reservedTo(endTime)
                .createdAt(LocalDateTime.now())
                .build();
    }

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
                .reservationStatus(roomReservation.getReservationStatus())
                .build();
    }

    public RoomReservationTimeRangeDto toTimeRangeDto(RoomReservation reservation) {
        return new RoomReservationTimeRangeDto(
                reservation.getReservedFrom(),
                reservation.getReservedTo()
        );
    }
}
