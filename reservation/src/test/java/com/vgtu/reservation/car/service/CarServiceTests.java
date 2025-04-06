package com.vgtu.reservation.car.service;

import com.vgtu.reservation.car.entity.Car;
import com.vgtu.reservation.car.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    private Car car;

    @BeforeEach
    void setUp() {
        car = Car.builder()
                .id(UUID.randomUUID())
                .manufacturer("Skoda")
                .model("Octavia")
                .averageFuelConsumption(5.5)
                .build();
    }

    @Test
    void testGetAllCars() {
        when(carRepository.findAll()).thenReturn(List.of(car));

        List<Car> cars = carService.getCar();

        assertNotNull(cars);
        assertEquals(1, cars.size());
        assertEquals("Skoda", cars.get(0).getManufacturer());
    }
}
