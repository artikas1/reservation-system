package com.vgtu.reservation.equipmentreservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentReservationTimeRangeDto {

    private LocalDateTime reservedFrom;
    private LocalDateTime reservedTo;

}
