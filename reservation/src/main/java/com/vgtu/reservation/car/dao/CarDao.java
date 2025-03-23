package com.vgtu.reservation.car.dao;

import com.vgtu.reservation.car.entity.Car;
import com.vgtu.reservation.car.integrity.CarDataIntegrity;
import com.vgtu.reservation.car.repository.CarRepository;
import com.vgtu.reservation.carreservation.repository.CarReservationRepository;
import com.vgtu.reservation.common.exception.exceptions.CarNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CarDao {

    private final CarDataIntegrity carDataIntegrity;
    private final CarRepository carRepository;
    private final CarReservationRepository carReservationRepository;

    public Car getCarById(UUID id) {
        carDataIntegrity.validateId(id);

        return carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException("Car not found"));
    }

}
