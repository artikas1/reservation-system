package com.vgtu.reservation.equipment.dto;

import com.vgtu.reservation.common.type.Address;
import com.vgtu.reservation.equipment.type.EquipmentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.lang.Nullable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    @Nullable
    private String manufacturer;

    @Nullable
    private String model;

    @Nullable
    private String code;

    @Nullable
    private String description;

    @NotNull(message = "Equipment type is required")
    private EquipmentType equipmentType;

    @NotNull(message = "Address is required")
    private Address address;

    @Nullable
    private byte[] image;
}
