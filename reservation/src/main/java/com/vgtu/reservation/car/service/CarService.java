package com.vgtu.reservation.car.service;

import com.vgtu.reservation.car.dao.CarDao;
import com.vgtu.reservation.car.dto.CarResponseDto;
import com.vgtu.reservation.car.entity.Car;
import com.vgtu.reservation.car.integrity.CarDataIntegrity;
import com.vgtu.reservation.car.mapper.CarMapper;
import com.vgtu.reservation.car.repository.CarRepository;
import com.vgtu.reservation.carreservation.repository.CarReservationRepository;
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

    public List<Car> getAvailableCars(LocalDateTime startTime, LocalDateTime endTime) {
        List<UUID> reservedCarIds = carReservationRepository.findReservedCarIdsBetween(startTime, endTime);

        if (reservedCarIds.isEmpty()) {
            return carRepository.findAll(); // all cars are free
        }

        return carRepository.findByIdNotIn(reservedCarIds);
    }

    public List<CarResponseDto> getAvailableCarDtosWithEcoFlag(LocalDateTime startTime, LocalDateTime endTime) {
        List<Car> availableCars = getAvailableCars(startTime, endTime);
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
