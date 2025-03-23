package com.vgtu.reservation.equipmentreservation.dto;

import com.vgtu.reservation.equipment.dto.EquipmentResponseDto;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * This is a data transfer object that represents the equipment reservation details to be sent in the response
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentReservationResponseDto {

    private UUID id;
    private EquipmentResponseDto equipment;
    private UUID userId;
    private LocalDate reservedFrom;
    private LocalDate reservedTo;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private LocalDate deletedAt;

}
