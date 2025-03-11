package com.vgtu.reservation.car.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarResponseDto {

    private UUID id;
    private String manufacturer;
    private String model;
    private String vin;
    private String fuel;
    private String manufacturerDate;
    private String engineCapacity;
    private LocalDateTime createAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private String bodyType;

}
