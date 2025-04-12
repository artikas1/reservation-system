package com.vgtu.reservation.car.service;

import com.vgtu.reservation.car.dao.CarDao;
import com.vgtu.reservation.car.dto.CarResponseDto;
import com.vgtu.reservation.car.entity.Car;
import com.vgtu.reservation.car.integrity.CarDataIntegrity;
import com.vgtu.reservation.car.mapper.CarMapper;
import com.vgtu.reservation.car.repository.CarRepository;
import com.vgtu.reservation.car.type.BodyType;
import com.vgtu.reservation.carreservation.repository.CarReservationRepository;
import com.vgtu.reservation.common.type.Address;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Contains the business logic for the car functionality
 */
@Service
@AllArgsConstructor
public class CarService {

    private final CarDao carDao;
    private final CarRepository carRepository;
    private final CarDataIntegrity carDataIntegrity;
    private final CarReservationRepository carReservationRepository;
    private final CarMapper carMapper;

    public List<Car> getCar() {
        return carRepository.findAll();
    }

    public Car getCarById(UUID id) {
        carDataIntegrity.validateId(id);

        return carDao.getCarById(id);
    }

    public List<Car> getAvailableCars(LocalDateTime startTime, LocalDateTime endTime, BodyType bodyType, Address address) {
        List<UUID> reservedCarIds = carReservationRepository.findReservedCarIdsBetween(startTime, endTime);
        List<Car> cars;

        if (reservedCarIds.isEmpty()) {
            cars = carRepository.findAll(); // all cars are free
        } else {
            cars = carRepository.findByIdNotIn(reservedCarIds);
        }

        if (bodyType != null) {
            cars = cars.stream()
                    .filter(car -> car.getBodyType() == bodyType)
                    .toList();
        }

        if (address != null) {
            cars = cars.stream()
                    .filter(car -> car.getAddress() == address)
                    .toList();
        }

        return cars;
    }

    public List<CarResponseDto> getAvailableCarDtosWithEcoFlag(LocalDateTime startTime, LocalDateTime endTime, BodyType bodyType, Address address) {
        List<Car> availableCars = getAvailableCars(startTime, endTime, bodyType, address);

        double minConsumption = getMinConsumption(availableCars);
        return carMapper.toDto(availableCars, minConsumption);
    }

    private double getMinConsumption(List<Car> cars) {
        return cars.stream()
                .filter(car -> car.getAverageFuelConsumption() != null)
                .mapToDouble(Car::getAverageFuelConsumption)
                .min()
                .orElse(Double.MAX_VALUE);
    }
}
