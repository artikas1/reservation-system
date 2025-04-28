package com.vgtu.reservation.car.repository;

import com.vgtu.reservation.car.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for Car entities.
 * <p>
 * Provides built-in CRUD operations and custom query methods
 * for accessing Car records in the database.
 */
@Repository
public interface CarRepository extends JpaRepository<Car, UUID> {
    List<Car> findByIdNotIn(List<UUID> reservedCarIds);

    List<Car> findByDeletedAtIsNull();

    List<Car> findByIdNotInAndDeletedAtIsNull(List<UUID> ids);

}
