package com.vgtu.reservation.car.controller;

import com.vgtu.reservation.car.entity.Car;
import com.vgtu.reservation.car.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
public class CarController {

//    @Operation(summary = "Get a car by ID", description = "Retrieves a specific car by its ID")
//    @GetMapping("/{id}")

    private final CarService carService;

    @Operation(summary = "Get all cars", description = "Retrieves all cars from database")
    @GetMapping("/car")
    public List<Car> getCar() {
        return carService.getCar();
    }
}
