package com.vgtu.reservation.car.repository;

import com.vgtu.reservation.car.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CarRepository extends JpaRepository<Car, UUID> {
    List<Car> findByIdNotIn(List<UUID> reservedCarIds);
}
