package com.vgtu.reservation.roomreservation.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomReservationTimeRangeDto {

    private LocalDateTime reservedFrom;
    private LocalDateTime reservedTo;

}
