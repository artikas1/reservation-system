package com.vgtu.reservation.car.service;

import com.vgtu.reservation.car.entity.Car;
import com.vgtu.reservation.car.repository.CarRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    public List<Car> getCar() {
        return carRepository.findAll();
    }
}
