package com.vgtu.reservation.carreservation.dto;

import com.vgtu.reservation.car.dto.CarResponseDto;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;
/*
 * This is a data transfer object that represents the car reservation details to be sent in the response
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarReservationResponseDto {

    private UUID id;
    private CarResponseDto car;
    private UUID userId;
    private LocalDate reservedFrom;
    private LocalDate reservedTo;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private LocalDate deletedAt;

}
