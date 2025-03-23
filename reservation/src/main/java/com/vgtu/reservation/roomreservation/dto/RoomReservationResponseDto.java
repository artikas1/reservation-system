package com.vgtu.reservation.roomreservation.dto;

import com.vgtu.reservation.room.dto.RoomResponseDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * This is a data transfer object that represents the room reservation details to be sent in the response
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomReservationResponseDto {

    private UUID id;
    private RoomResponseDto room;
    private UUID userId;
    private LocalDateTime reservedFrom;
    private LocalDateTime reservedTo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
