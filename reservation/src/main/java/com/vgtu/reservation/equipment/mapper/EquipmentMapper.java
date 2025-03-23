package com.vgtu.reservation.equipment.mapper;

import com.vgtu.reservation.equipment.dto.EquipmentResponseDto;
import com.vgtu.reservation.equipment.entity.Equipment;
import org.springframework.stereotype.Component;

@Component
public class EquipmentMapper {

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
                .build();
    }

}
