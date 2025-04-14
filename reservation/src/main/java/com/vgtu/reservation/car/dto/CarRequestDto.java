package com.vgtu.reservation.car.dto;

import com.vgtu.reservation.car.type.BodyType;
import com.vgtu.reservation.common.type.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarRequestDto {

    @NotBlank(message = "Manufacturer is required")
    private String manufacturer;

    @NotBlank(message = "Model is required")
    private String model;

    @NotBlank(message = "Vin is required")
    private String vin;

    @NotBlank(message = "fuel is required")
    private String fuel;

    @NotBlank(message = "Manufacturer date is required")
    private String manufacturerDate;

    @NotBlank(message = "Engine capacity (in cc) is required")
    private String engineCapacity;

    @NotBlank(message = "Number place is required")
    private String numberPlate;

    @NotNull(message = "Body type is required")
    private BodyType bodyType;

    private Double averageFuelConsumption;

    @NotNull(message = "Address is required")
    private Address address;
}
