package com.vgtu.reservation.equipmentreservation.dto;

import com.vgtu.reservation.equipment.dto.EquipmentResponseDto;
import com.vgtu.reservation.equipmentreservation.type.ReservationStatus;
import lombok.*;

import java.time.LocalDateTime;
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
    private LocalDateTime reservedFrom;
    private LocalDateTime reservedTo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private ReservationStatus reservationStatus;

}
