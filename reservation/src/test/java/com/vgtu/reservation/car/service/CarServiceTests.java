package com.vgtu.reservation.car.service;

import com.vgtu.reservation.auth.service.authentication.AuthenticationService;
import com.vgtu.reservation.car.dao.CarDao;
import com.vgtu.reservation.car.dto.CarRequestDto;
import com.vgtu.reservation.car.dto.CarResponseDto;
import com.vgtu.reservation.car.entity.Car;
import com.vgtu.reservation.car.integrity.CarDataIntegrity;
import com.vgtu.reservation.car.mapper.CarMapper;
import com.vgtu.reservation.car.repository.CarRepository;
import com.vgtu.reservation.car.type.BodyType;
import com.vgtu.reservation.carreservation.repository.CarReservationRepository;
import com.vgtu.reservation.common.type.Address;
import com.vgtu.reservation.user.entity.User;
import com.vgtu.reservation.user.repository.UserRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private CarReservationRepository carReservationRepository;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private CarMapper carMapper;

    @Mock
    CarDao carDao;

    @Mock
    private CarDataIntegrity carDataIntegrity;

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

        when(carRepository.findByDeletedAtIsNull())
                .thenReturn(List.of(sedanCar, minivanCar));

        List<Car> result = carService.getAvailableCars(startTime, endTime, BodyType.SEDANAS, Address.SAULETEKIO_AL_15);

        assertEquals(1, result.size());
        assertEquals(BodyType.SEDANAS, result.get(0).getBodyType());
        assertEquals(Address.SAULETEKIO_AL_15, result.get(0).getAddress());
    }

    @Test
    void createCar_shouldCreateCar_whenUserIsAdmin() {
        CarRequestDto carRequestDto = CarRequestDto.builder()
                .manufacturer("BMW")
                .model("530d xDrive")
                .vin("WBAJE5C56JB123456")
                .fuel("Diesel")
                .manufacturerDate("2018-01-01")
                .engineCapacity("2993")
                .numberPlate("AAA111")
                .bodyType(BodyType.SEDANAS)
                .averageFuelConsumption(6.0)
                .address(Address.SAULETEKIO_AL_15)
                .build();

        User adminUser = User.builder()
                .id(UUID.randomUUID())
                .isAdmin(true)
                .build();

        Car car = Car.builder()
                .id(UUID.randomUUID())
                .manufacturer("BMW")
                .model("530d xDrive")
                .vin("WBAJE5C56JB123456")
                .fuel("Diesel")
                .manufacturerDate("2018-01-01")
                .engineCapacity("2993")
                .numberPlate("AAA111")
                .bodyType(BodyType.SEDANAS)
                .averageFuelConsumption(6.0)
                .address(Address.SAULETEKIO_AL_15)
                .build();

        when(authenticationService.getAuthenticatedUser()).thenReturn(adminUser);
        when(carMapper.toEntity(carRequestDto)).thenReturn(car);
        when(carDao.createCar(car)).thenReturn(car);
        when(carMapper.toResponseDto(car)).thenReturn(CarResponseDto.builder()
                .id(car.getId())
                .manufacturer("BMW")
                .model("530d xDrive")
                .vin("WBAJE5C56JB123456")
                .fuel("Diesel")
                .build());

        CarResponseDto response = carService.createCar(carRequestDto);

        assertNotNull(response);
        assertEquals("BMW", response.getManufacturer());
        assertEquals("530d xDrive", response.getModel());
        assertEquals("WBAJE5C56JB123456", response.getVin());
    }

    @Test
    void updateCar_shouldUpdateCar_whenUserIsAdmin() {
        UUID carId = UUID.randomUUID();

        CarRequestDto carRequestDto = CarRequestDto.builder()
                .manufacturer("BMW")
                .model("530e xDrive")
                .vin("WBAJE5C56JB654321")
                .fuel("Hybrid")
                .manufacturerDate("2020-01-01")
                .engineCapacity("2000")
                .numberPlate("BBB222")
                .bodyType(BodyType.SEDANAS)
                .averageFuelConsumption(5.0)
                .address(Address.SAULETEKIO_AL_15)
                .build();

        User adminUser = User.builder()
                .id(UUID.randomUUID())
                .isAdmin(true)
                .build();

        Car existingCar = Car.builder()
                .id(carId)
                .manufacturer("BMW")
                .model("530d xDrive")
                .vin("WBAJE5C56JB123456")
                .fuel("Diesel")
                .manufacturerDate("2018-01-01")
                .engineCapacity("2993")
                .numberPlate("AAA111")
                .bodyType(BodyType.SEDANAS)
                .averageFuelConsumption(6.0)
                .address(Address.SAULETEKIO_AL_15)
                .build();

        Car updatedCar = Car.builder()
                .id(carId)
                .manufacturer(carRequestDto.getManufacturer())
                .model(carRequestDto.getModel())
                .vin(carRequestDto.getVin())
                .fuel(carRequestDto.getFuel())
                .manufacturerDate(carRequestDto.getManufacturerDate())
                .engineCapacity(carRequestDto.getEngineCapacity())
                .numberPlate(carRequestDto.getNumberPlate())
                .bodyType(carRequestDto.getBodyType())
                .averageFuelConsumption(carRequestDto.getAverageFuelConsumption())
                .address(carRequestDto.getAddress())
                .build();

        CarResponseDto expectedResponse = CarResponseDto.builder()
                .id(carId)
                .manufacturer(updatedCar.getManufacturer())
                .model(updatedCar.getModel())
                .vin(updatedCar.getVin())
                .fuel(updatedCar.getFuel())
                .build();

        when(authenticationService.getAuthenticatedUser()).thenReturn(adminUser);
        when(carDao.getCarById(carId)).thenReturn(existingCar);
        when(carDao.createCar(any(Car.class))).thenReturn(updatedCar);
        when(carMapper.toResponseDto(updatedCar)).thenReturn(expectedResponse);

        CarResponseDto result = carService.updateCarById(carId, carRequestDto);

        assertNotNull(result);
        assertEquals(expectedResponse.getManufacturer(), result.getManufacturer());
        assertEquals(expectedResponse.getModel(), result.getModel());
        assertEquals(expectedResponse.getVin(), result.getVin());
        assertEquals(expectedResponse.getFuel(), result.getFuel());
    }

    @Test
    void deleteCar_shouldMarkCarAsDeleted_whenUserIsAdmin() {
        UUID carId = UUID.randomUUID();

        User adminUser = User.builder()
                .id(UUID.randomUUID())
                .isAdmin(true)
                .build();

        Car existingCar = Car.builder()
                .id(carId)
                .manufacturer("Audi")
                .model("A6")
                .build();

        when(authenticationService.getAuthenticatedUser()).thenReturn(adminUser);
        when(carDao.getCarById(carId)).thenReturn(existingCar);
        doNothing().when(carDao).save(existingCar);

        carService.deleteCarById(carId);

        assertNotNull(existingCar.getDeletedAt());
        verify(carDao, times(1)).save(existingCar);
    }

}
