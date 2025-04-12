package com.vgtu.reservation.carreservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarReservationTimeRangeDto {

    private LocalDateTime reservedFrom;
    private LocalDateTime reservedTo;

}
