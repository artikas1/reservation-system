package com.vgtu.reservation.room.dto;

import com.vgtu.reservation.common.type.Address;
import com.vgtu.reservation.room.type.RoomType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Floor is required")
    private String floor;

    @Nullable
    private String roomNumber;

    @Nullable
    private String seats;

    @Nullable
    private String description;

    @NotBlank(message = "Room type is required")
    private RoomType roomType;

    @NotBlank(message = "Address is required")
    private Address address;

    @Nullable
    private byte[] image;

}
