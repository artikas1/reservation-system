package com.vgtu.reservation.car.service;

import com.vgtu.reservation.car.entity.Car;
import com.vgtu.reservation.car.repository.CarRepository;
import com.vgtu.reservation.car.type.BodyType;
import com.vgtu.reservation.carreservation.repository.CarReservationRepository;
import com.vgtu.reservation.common.type.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepository carRepository;
    @Mock private CarReservationRepository carReservationRepository;

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

    @Test
    void getAvailableCars_shouldReturnFilteredCars_whenNoReservationsExist() {
        Car sedanCar = Car.builder()
                .id(UUID.randomUUID())
                .manufacturer("Skoda")
                .model("Octavia")
                .bodyType(BodyType.SEDANAS)
                .address(Address.SAULETEKIO_AL_15)
                .build();

        Car minivanCar = Car.builder()
                .id(UUID.randomUUID())
                .manufacturer("Volkswagen")
                .model("Transporter")
                .bodyType(BodyType.MINIVENAS)
                .address(Address.SAULETEKIO_AL_15)
                .build();

        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusDays(1);

        when(carReservationRepository.findReservedCarIdsBetween(startTime, endTime))
                .thenReturn(List.of());

        when(carRepository.findAll())
                .thenReturn(List.of(sedanCar, minivanCar));

        List<Car> result = carService.getAvailableCars(startTime, endTime, BodyType.SEDANAS, Address.SAULETEKIO_AL_15);

        assertEquals(1, result.size());
        assertEquals(BodyType.SEDANAS, result.get(0).getBodyType());
        assertEquals(Address.SAULETEKIO_AL_15, result.get(0).getAddress());
    }
}
