package com.vgtu.reservation.carreservation.mapper;

import com.vgtu.reservation.car.mapper.CarMapper;
import com.vgtu.reservation.carreservation.dto.CarReservationResponseDto;
import com.vgtu.reservation.carreservation.entity.CarReservation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/*
 * Converts the CarReservation entity to a CarReservationResponseDto so it can be sent as a response
 */

@Component
@AllArgsConstructor
public class CarReservationMapper {

    private final CarMapper carMapper;

    public CarReservationResponseDto toCarResponseDto(CarReservation carReservation) {
        return CarReservationResponseDto.builder()
                .id(carReservation.getId())
                .car(carMapper.toResponseDto(carReservation.getCar()))
                .userId(carReservation.getUser().getId())
                .reservedFrom(carReservation.getReservedFrom())
                .reservedTo(carReservation.getReservedTo())
                .createdAt(carReservation.getCreatedAt())
                .updatedAt(carReservation.getUpdatedAt())
                .deletedAt(carReservation.getDeletedAt())
                .build();
    }

}
