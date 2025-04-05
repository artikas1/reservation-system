package com.vgtu.reservation.car.controller;

import com.vgtu.reservation.car.dto.CarResponseDto;
import com.vgtu.reservation.car.entity.Car;
import com.vgtu.reservation.car.mapper.CarMapper;
import com.vgtu.reservation.car.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(summary = "Get all cars", description = "Retrieves all cars from database")
    @GetMapping("/all")
    public List<Car> getCar() {
        return carService.getCar();
    }

    @Operation(summary = "Get all available cars", description = "Retrieves all cars from database that are available for reservation")
    @GetMapping("/available")
    public ResponseEntity<List<CarResponseDto>> getAvailableCars(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        var availableCars = carService.getAvailableCars(startTime, endTime);
        var result = availableCars.stream().map(carMapper::toResponseDto).toList();

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get all available cars with eco flag", description = "Retrieves available cars and marks the most eco-friendly ones")
    @GetMapping("/available/eco")
    public ResponseEntity<List<CarResponseDto>> getAvailableEcoCars(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        var result = carService.getAvailableCarDtosWithEcoFlag(startTime, endTime);
        return ResponseEntity.ok(result);
    }

}
