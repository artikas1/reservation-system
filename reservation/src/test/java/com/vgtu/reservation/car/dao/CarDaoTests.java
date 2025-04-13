package com.vgtu.reservation.car.dao;

import com.vgtu.reservation.car.entity.Car;
import com.vgtu.reservation.car.integrity.CarDataIntegrity;
import com.vgtu.reservation.car.repository.CarRepository;
import com.vgtu.reservation.car.type.BodyType;
import com.vgtu.reservation.common.exception.CarNotFoundException;
import com.vgtu.reservation.common.type.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({CarDao.class, CarDataIntegrity.class})
public class CarDaoTests {

    @Autowired
    private CarDao carDao;

    @Autowired
    private CarRepository carRepository;

    private Car car;

    @BeforeEach
    void setUp() {
        car = Car.builder()
                .manufacturer("Skoda")
                .model("Octavia")
                .vin("TMBZZZ1Z9J11234567")
                .fuel("Gasoline")
                .manufacturerDate("2017-01-01")
                .engineCapacity("1798")
                .numberPlate("CCC111")
                .createdAt(LocalDateTime.of(2017, 6, 1,0,0,0))
                .updatedAt(null)
                .deletedAt(null)
                .bodyType(BodyType.SEDANAS)
                .averageFuelConsumption(6.0)
                .address(Address.SAULETEKIO_AL_15)
                .build();

        car = carRepository.save(car);
    }

    @Test
    void testGetCarById_shouldReturnCar() {
        var foundCar = carDao.getCarById(car.getId());

        assertNotNull(foundCar);
        assertEquals(car.getId(), foundCar.getId());
        assertEquals("Skoda", foundCar.getManufacturer());
        assertEquals("Octavia", foundCar.getModel());
        assertEquals("Gasoline", foundCar.getFuel());
        assertEquals("2017-01-01", foundCar.getManufacturerDate());
        assertEquals("1798", foundCar.getEngineCapacity());
        assertEquals(BodyType.SEDANAS, foundCar.getBodyType());
        assertEquals(Address.SAULETEKIO_AL_15, foundCar.getAddress());

    }

    @Test
    void testGetCarById_shouldThrowIfNotFound() {
        UUID randomId = UUID.randomUUID();

        assertThrows(CarNotFoundException.class, () -> carDao.getCarById(randomId));
    }

}
