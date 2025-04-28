package com.vgtu.reservation.car.service;

import com.vgtu.reservation.auth.service.authentication.AuthenticationService;
import com.vgtu.reservation.car.dao.CarDao;
import com.vgtu.reservation.car.dto.CarRequestDto;
import com.vgtu.reservation.car.dto.CarResponseDto;
import com.vgtu.reservation.car.entity.Car;
import com.vgtu.reservation.car.integrity.CarDataIntegrity;
import com.vgtu.reservation.car.mapper.CarMapper;
import com.vgtu.reservation.car.repository.CarRepository;
import com.vgtu.reservation.car.type.BodyType;
import com.vgtu.reservation.carreservation.repository.CarReservationRepository;
import com.vgtu.reservation.common.type.Address;
import com.vgtu.reservation.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Contains the business logic for the car functionality
 */
@Service
@AllArgsConstructor
@Transactional
public class CarService {

    private final CarDao carDao;
    private final CarRepository carRepository;
    private final CarDataIntegrity carDataIntegrity;
    private final CarReservationRepository carReservationRepository;
    private final CarMapper carMapper;
    private final AuthenticationService authenticationService;

    private static final double GASOLINE_CO2_EMISSION_FACTOR = 2.31; // kg CO2 per liter
    private static final double DIESEL_CO2_EMISSION_FACTOR = 2.68;   // kg CO2 per liter

    public CarResponseDto createCar(CarRequestDto carRequestDto) {
        carDataIntegrity.validateCarRequest(carRequestDto);

        User user = authenticationService.getAuthenticatedUser();
        if (!user.isAdmin()) {
            throw new AccessDeniedException("Only admins can create cars");
        }

        var car = carMapper.toEntity(carRequestDto);

        return carMapper.toResponseDto(carDao.createCar(car));
    }


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
            cars = carRepository.findByDeletedAtIsNull();
        } else {
            cars = carRepository.findByIdNotInAndDeletedAtIsNull(reservedCarIds);
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

        double minEcoImpact = availableCars.stream()
                .mapToDouble(this::getAdjustedEcoImpact)
                .min()
                .orElse(Double.MAX_VALUE);

        return carMapper.toDtoWithEcoFlag(availableCars, minEcoImpact, this::getAdjustedEcoImpact);
    }


    private double getAdjustedEcoImpact(Car car) {
        if (car.getAverageFuelConsumption() == null || car.getFuel() == null) {
            return Double.MAX_VALUE;
        }

        return switch (car.getFuel().toUpperCase()) {
            case "GASOLINE" -> car.getAverageFuelConsumption() * GASOLINE_CO2_EMISSION_FACTOR;
            case "DIESEL" -> car.getAverageFuelConsumption() * DIESEL_CO2_EMISSION_FACTOR;
            default -> Double.MAX_VALUE;
        };
    }

    public CarResponseDto updateCarById(UUID id, CarRequestDto carRequestDto) {
        carDataIntegrity.validateId(id);

        User user = authenticationService.getAuthenticatedUser();
        if (!user.isAdmin()) {
            throw new AccessDeniedException("Only admins can create cars");
        }
        var car = carDao.getCarById(id);

        car.setManufacturer(carRequestDto.getManufacturer());
        car.setModel(carRequestDto.getModel());
        car.setVin(carRequestDto.getVin());
        car.setFuel(carRequestDto.getFuel());
        car.setManufacturerDate(carRequestDto.getManufacturerDate());
        car.setEngineCapacity(carRequestDto.getEngineCapacity());
        car.setNumberPlate(carRequestDto.getNumberPlate());
        car.setBodyType(carRequestDto.getBodyType());
        car.setAddress(carRequestDto.getAddress());
        car.setImage(carRequestDto.getImage());

        car.setUpdatedAt(LocalDateTime.now());

        return carMapper.toResponseDto(carDao.createCar(car));
    }

    public void deleteCarById(UUID id) {
        carDataIntegrity.validateId(id);

        var user = authenticationService.getAuthenticatedUser();
        if (!user.isAdmin()) {
            throw new AccessDeniedException("Only admins can delete cars");
        }

        var car = carDao.getCarById(id);

        car.setDeletedAt(LocalDateTime.now());

        carDao.save(car);
    }

    public long countAllCars() {
        return carRepository.count();
    }
}
