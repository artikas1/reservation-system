package com.vgtu.reservation.car.mapper;

import com.vgtu.reservation.car.dto.CarRequestDto;
import com.vgtu.reservation.car.dto.CarResponseDto;
import com.vgtu.reservation.car.entity.Car;
import com.vgtu.reservation.car.type.BodyType;
import com.vgtu.reservation.common.type.Address;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@Component
public class CarMapper {

    public Car toEntity(CarRequestDto carRequestDto) {
        return Car.builder()
                .manufacturer(carRequestDto.getManufacturer())
                .model(carRequestDto.getModel())
                .vin(carRequestDto.getVin())
                .fuel(carRequestDto.getFuel())
                .manufacturerDate(carRequestDto.getManufacturerDate())
                .engineCapacity(carRequestDto.getEngineCapacity())
                .numberPlate(carRequestDto.getNumberPlate())
                .createdAt(LocalDateTime.now())
                .averageFuelConsumption(carRequestDto.getAverageFuelConsumption())
                .bodyType(carRequestDto.getBodyType())
                .address(carRequestDto.getAddress())
                .image(carRequestDto.getImage())
                .build();
    }

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
                .averageFuelConsumption(car.getAverageFuelConsumption())
                .isEcoFriendly(false) //always false in this one
                .bodyType(car.getBodyType())
                .address(car.getAddress())
                .image(car.getImage())
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
                .averageFuelConsumption(car.getAverageFuelConsumption())
                .isEcoFriendly(isEcoFriendly)
                .bodyType(car.getBodyType())
                .address(car.getAddress())
                .image(car.getImage())
                .build();
    }

    public List<CarResponseDto> toDtoWithEcoFlag(List<Car> cars, double minEcoImpact, Function<Car, Double> ecoImpactFunction) {
        return cars.stream()
                .map(car -> toResponseDto(
                        car,
                        ecoImpactFunction.apply(car).equals(minEcoImpact)
                ))
                .toList();
    }

    public CarRequestDto toRequestDto(String manufacturer,
                                      String model,
                                      String vin,
                                      String fuel,
                                      String manufacturerDate,
                                      String engineCapacity,
                                      String numberPlate,
                                      BodyType bodyType,
                                      Address address,
                                      Double averageFuelConsumption,
                                      MultipartFile image) throws IOException {
        return CarRequestDto.builder()
                .manufacturer(manufacturer)
                .model(model)
                .vin(vin)
                .fuel(fuel)
                .manufacturerDate(manufacturerDate)
                .engineCapacity(engineCapacity)
                .numberPlate(numberPlate)
                .bodyType(bodyType)
                .address(address)
                .averageFuelConsumption(averageFuelConsumption)
                .image(image.getBytes())
                .build();
    }

}