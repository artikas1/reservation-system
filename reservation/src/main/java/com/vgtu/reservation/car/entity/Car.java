package com.vgtu.reservation.car.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "car")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    //private int typeId;
    private String manufacturer;
    private String model;
    private String vin;
    private String fuel;
    private String manufacturerDate;
    private String engineCapacity;
    private String numberPlate;
    //private int areaId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private String bodyType;
    private Double averageFuelConsumption; // in L/100km
}
