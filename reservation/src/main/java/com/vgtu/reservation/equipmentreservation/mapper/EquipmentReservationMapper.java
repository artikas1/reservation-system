package com.vgtu.reservation.equipmentreservation.mapper;

import com.vgtu.reservation.equipment.entity.Equipment;
import com.vgtu.reservation.equipment.mapper.EquipmentMapper;
import com.vgtu.reservation.equipmentreservation.dto.EquipmentReservationResponseDto;
import com.vgtu.reservation.equipmentreservation.dto.EquipmentReservationTimeRangeDto;
import com.vgtu.reservation.equipmentreservation.entity.EquipmentReservation;
import com.vgtu.reservation.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Converts the EquipmentReservation entity to a EquipmentReservationResponseDto so it can be sent as a response
 */
@Component
@AllArgsConstructor
public class EquipmentReservationMapper {

    private final EquipmentMapper equipmentMapper;

    public EquipmentReservation toEntity(Equipment equipment, User user, LocalDateTime startTime, LocalDateTime endTime) {
        return EquipmentReservation.builder()
                .equipment(equipment)
                .user(user)
                .reservedFrom(startTime)
                .reservedTo(endTime)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public EquipmentReservationResponseDto toEquipmentResponseDto(EquipmentReservation equipmentReservation) {
        return EquipmentReservationResponseDto.builder()
                .id(equipmentReservation.getId())
                .equipment(equipmentMapper.toResponseDto(equipmentReservation.getEquipment()))
                .userId(equipmentReservation.getUser().getId())
                .reservedFrom(equipmentReservation.getReservedFrom())
                .reservedTo(equipmentReservation.getReservedTo())
                .createdAt(equipmentReservation.getCreatedAt())
                .updatedAt(equipmentReservation.getUpdatedAt())
                .deletedAt(equipmentReservation.getDeletedAt())
                .reservationStatus(equipmentReservation.getReservationStatus())
                .build();
    }

    public EquipmentReservationTimeRangeDto toTimeRangeDto(EquipmentReservation reservation) {
        return new EquipmentReservationTimeRangeDto(
                reservation.getReservedFrom(),
                reservation.getReservedTo()
        );
    }

}
