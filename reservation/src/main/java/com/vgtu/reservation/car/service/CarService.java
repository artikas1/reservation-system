package com.vgtu.reservation.car.service;

import com.vgtu.reservation.car.dao.CarDao;
import com.vgtu.reservation.car.entity.Car;
import com.vgtu.reservation.car.integrity.CarDataIntegrity;
import com.vgtu.reservation.car.repository.CarRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CarService {

    private final CarDao carDao;
    private final CarRepository carRepository;
    private final CarDataIntegrity carDataIntegrity;

    public List<Car> getCar() {
        return carRepository.findAll();
    }

    public Car getCarById(UUID id) {
        carDataIntegrity.validateId(id);

        return carDao.getCarById(id);
    }
}
