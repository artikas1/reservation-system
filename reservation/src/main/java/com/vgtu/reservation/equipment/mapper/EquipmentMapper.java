package com.vgtu.reservation.equipment.mapper;

import com.vgtu.reservation.common.type.Address;
import com.vgtu.reservation.equipment.dto.EquipmentRequestDto;
import com.vgtu.reservation.equipment.dto.EquipmentResponseDto;
import com.vgtu.reservation.equipment.entity.Equipment;
import com.vgtu.reservation.equipment.type.EquipmentType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class EquipmentMapper {

    public Equipment toEntity(EquipmentRequestDto equipmentRequestDto) {
        return Equipment.builder()
                .name(equipmentRequestDto.getName())
                .manufacturer(equipmentRequestDto.getManufacturer())
                .model(equipmentRequestDto.getModel())
                .code(equipmentRequestDto.getCode())
                .description(equipmentRequestDto.getDescription())
                .createdAt(LocalDateTime.now())
                .equipmentType(equipmentRequestDto.getEquipmentType())
                .address(equipmentRequestDto.getAddress())
                .image(equipmentRequestDto.getImage())
                .build();
    }

    public EquipmentResponseDto toResponseDto(Equipment equipment) {
        return EquipmentResponseDto.builder()
                .id(equipment.getId())
                .name(equipment.getName())
                .manufacturer(equipment.getManufacturer())
                .model(equipment.getModel())
                .code(equipment.getCode())
                .description(equipment.getDescription())
                .createdAt(equipment.getCreatedAt())
                .updatedAt(equipment.getUpdatedAt())
                .deletedAt(equipment.getDeletedAt())
                .equipmentType(equipment.getEquipmentType())
                .address(equipment.getAddress())
                .image(equipment.getImage())
                .build();
    }

    public EquipmentRequestDto toRequestDto(String name,
                               String manufacturer,
                               String model,
                               String code,
                               String description,
                               EquipmentType equipmentType,
                               Address address,
                               MultipartFile image) throws IOException {
        return EquipmentRequestDto.builder()
                .name(name)
                .manufacturer(manufacturer)
                .model(model)
                .code(code)
                .description(description)
                .equipmentType(equipmentType)
                .address(address)
                .image(image != null ? image.getBytes() : null)
                .build();
    }


}
