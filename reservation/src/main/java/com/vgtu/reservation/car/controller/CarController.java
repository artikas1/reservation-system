package com.vgtu.reservation.car.controller;

import com.vgtu.reservation.car.dto.CarResponseDto;
import com.vgtu.reservation.car.entity.Car;
import com.vgtu.reservation.car.mapper.CarMapper;
import com.vgtu.reservation.car.service.CarService;
import com.vgtu.reservation.car.type.BodyType;
import com.vgtu.reservation.common.type.Address;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Used to define endpoints for car
 */
@RequestMapping("/car")
@AllArgsConstructor
@RestController
public class CarController {

    private final CarService carService;
    private final CarMapper carMapper;

    @Operation(summary = "Create a new car", description = "Creates a new car in the database")
    @PostMapping(value = "/create")
    public ResponseEntity<CarResponseDto> createCar(
            @RequestParam("manufacturer") String manufacturer,
            @RequestParam("model") String model,
            @RequestParam("vin") String vin,
            @RequestParam("fuel") String fuel,
            @RequestParam("manufacturerDate") String manufacturerDate,
            @RequestParam("engineCapacity") String engineCapacity,
            @RequestParam("numberPlate") String numberPlate,
            @RequestParam("bodyType") BodyType bodyType,
            @RequestParam("address") Address address,
            @RequestParam(value = "averageFuelConsumption", required = false) Double averageFuelConsumption) {
        try {
            var request = carMapper.toRequestDto(manufacturer, model, vin, fuel, manufacturerDate, engineCapacity, numberPlate, bodyType, address, averageFuelConsumption);
            return ResponseEntity.status(HttpStatus.CREATED).body(carService.createCar(request));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get all cars", description = "Retrieves all cars from database")
    @GetMapping("/all")
    public List<Car> getCar() {
        return carService.getCar();
    }

    @Operation(summary = "Get all available cars", description = "Retrieves all cars from database that are available for reservation")
    @GetMapping("/available")
    public ResponseEntity<List<CarResponseDto>> getAvailableCars(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(required = false) BodyType bodyType,
            @RequestParam(required = false) Address address) {

        var availableCars = carService.getAvailableCars(startTime, endTime, bodyType, address);
        var result = availableCars.stream().map(carMapper::toResponseDto).toList();

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get all available cars with eco flag", description = "Retrieves available cars and marks the most eco-friendly ones")
    @GetMapping("/available/eco")
    public ResponseEntity<List<CarResponseDto>> getAvailableEcoCars(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(required = false) BodyType bodyType,
            @RequestParam(required = false) Address address) {

        var result = carService.getAvailableCarDtosWithEcoFlag(startTime, endTime, bodyType, address);
        return ResponseEntity.ok(result);
    }

}
