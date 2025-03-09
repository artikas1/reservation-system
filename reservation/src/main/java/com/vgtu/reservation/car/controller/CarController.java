package com.vgtu.reservation.car.controller;

import com.vgtu.reservation.car.entity.Car;
import com.vgtu.reservation.car.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/car")
public class CarController {

//    @Operation(summary = "Get a car by ID", description = "Retrieves a specific car by its ID")
//    @GetMapping("/{id}")

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public List<Car> getCar() {
        return carService.getCar();
    }
}
