package com.vgtu.reservation.carreservation.dto;

import com.vgtu.reservation.car.dto.CarResponseDto;
import com.vgtu.reservation.common.type.ReservationStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
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
    private LocalDateTime reservedFrom;
    private LocalDateTime reservedTo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private ReservationStatus reservationStatus;

}
