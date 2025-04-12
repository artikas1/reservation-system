package com.vgtu.reservation.car.entity;

import com.vgtu.reservation.car.type.BodyType;
import com.vgtu.reservation.common.type.Address;
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
    private Double averageFuelConsumption; // in L/100km

    @Enumerated(EnumType.STRING)
    private BodyType bodyType;

    @Enumerated(EnumType.STRING)
    private Address address;
}
