package com.vgtu.reservation.car.dto;

import com.vgtu.reservation.car.type.BodyType;
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
    private String numberPlate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private Double averageFuelConsumption; // in L/100km
    private Boolean isEcoFriendly;
    private BodyType bodyType;
}
