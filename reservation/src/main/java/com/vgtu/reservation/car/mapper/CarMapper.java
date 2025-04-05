package com.vgtu.reservation.car.mapper;

import com.vgtu.reservation.car.dto.CarResponseDto;
import com.vgtu.reservation.car.entity.Car;
import org.springframework.stereotype.Component;

import java.util.List;

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
                .numberPlate(car.getNumberPlate())
                .createdAt(car.getCreatedAt())
                .updatedAt(car.getUpdatedAt())
                .deletedAt(car.getDeletedAt())
                .bodyType(car.getBodyType())
                .averageFuelConsumption(car.getAverageFuelConsumption())
                .isEcoFriendly(false) //always false in this one
                .build();
    }

    public CarResponseDto toResponseDto(Car car, boolean isEcoFriendly) {
        return CarResponseDto.builder()
                .id(car.getId())
                .manufacturer(car.getManufacturer())
                .model(car.getModel())
                .vin(car.getVin())
                .fuel(car.getFuel())
                .manufacturerDate(car.getManufacturerDate())
                .engineCapacity(car.getEngineCapacity())
                .numberPlate(car.getNumberPlate())
                .createdAt(car.getCreatedAt())
                .updatedAt(car.getUpdatedAt())
                .deletedAt(car.getDeletedAt())
                .bodyType(car.getBodyType())
                .averageFuelConsumption(car.getAverageFuelConsumption())
                .isEcoFriendly(isEcoFriendly)
                .build();
    }

    public List<CarResponseDto> toDto(List<Car> cars, double minEcoValue) {
        return cars.stream()
                .map(car -> toResponseDto(
                        car,
                        car.getAverageFuelConsumption() != null &&
                                car.getAverageFuelConsumption().equals(minEcoValue)
                ))
                .toList();
    }

}