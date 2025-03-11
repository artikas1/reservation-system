package com.vgtu.reservation.car.mapper;

import com.vgtu.reservation.car.dto.CarResponseDto;
import com.vgtu.reservation.car.entity.Car;
import org.springframework.stereotype.Component;

@Component
public class CarMapper {

    public CarResponseDto toResponseDto(Car car) {
        return CarResponseDto.builder()
                .id(car.getId())
                .manufacturer(car.getManufacturer())
                .model(car.getModel())
                .vin(car.getVin())
                .fuel(car.getFuel())
                .manufacturerDate(car.getManufacturerDate())
                .engineCapacity(car.getEngineCapacity())
                .createAt(car.getCreatedAt())
                .updatedAt(car.getUpdatedAt())
                .deletedAt(car.getDeletedAt())
                .bodyType(car.getBodyType())
                .build();
    }

}
