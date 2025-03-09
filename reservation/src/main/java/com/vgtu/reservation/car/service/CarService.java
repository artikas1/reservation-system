package com.vgtu.reservation.car.service;

import com.vgtu.reservation.car.entity.Car;
import com.vgtu.reservation.car.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    private final CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> getCar() {
        return carRepository.findAll();
    }

}
