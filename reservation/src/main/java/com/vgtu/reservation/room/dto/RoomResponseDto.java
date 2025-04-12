package com.vgtu.reservation.room.dto;

import com.vgtu.reservation.common.type.Address;
import com.vgtu.reservation.room.type.RoomType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponseDto {

    private UUID id;
    private String name;
    private String floor;
    private String roomNumber;
    private String seats;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private RoomType roomType;
    private Address address;
}
