package com.vgtu.reservation.equipment.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentResponseDto {

    private UUID id;
    private String name;
    private String manufacturer;
    private String model;
    private String code;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

}
