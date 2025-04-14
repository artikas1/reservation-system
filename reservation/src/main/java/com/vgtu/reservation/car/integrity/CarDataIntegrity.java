package com.vgtu.reservation.car.integrity;

import com.vgtu.reservation.car.dto.CarRequestDto;
import com.vgtu.reservation.car.entity.Car;
import com.vgtu.reservation.common.exception.CarBadRequestException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CarDataIntegrity {

    public static final String CAR_ID_CANNOT_BE_NULL = "Car id cannot be null";
    public static final String CAR_REQUEST_CANNOT_BE_NULL = "Car request cannot be null";
    public static final String CAR_MANUFACTURER_CANNOT_BE_NULL = "Car manufacturer cannot be null or empty";
    public static final String CAR_MODEL_CANNOT_BE_NULL = "Car model cannot be null or empty";
    public static final String CAR_VIN_CANNOT_BE_NULL = "Car vin cannot be null or empty";
    public static final String CAR_FUEL_CANNOT_BE_NULL = "Car fuel cannot be null or empty";
    public static final String CAR_MANUFACTURER_DATE_CANNOT_BE_NULL = "Car manufacturer date cannot be null or empty";
    public static final String CAR_ENGINE_CAPACITY_CANNOT_BE_NULL = "Car engine capacity cannot be null or empty";
    public static final String CAR_NUMBER_PLATE_CANNOT_BE_NULL = "Car number plate cannot be null or empty";
    public static final String CAR_BODY_TYPE_CANNOT_BE_NULL = "Car body type cannot be null or empty";
    public static final String CAR_ADDRESS_CANNOT_BE_NULL = "Car address cannot be null or empty";
    public static final String CAR_CANNOT_BE_NULL = "Car cannot be null or empty";

    public void validateId(UUID id) {
        if (id == null) {
            throw new CarBadRequestException(CAR_ID_CANNOT_BE_NULL);
        }
    }

    public void validateCarRequest(CarRequestDto carRequestDto) {
        if(carRequestDto == null){
            throw new CarBadRequestException(CAR_REQUEST_CANNOT_BE_NULL);
        }
        if(carRequestDto.getManufacturer() == null){
            throw new CarBadRequestException(CAR_MANUFACTURER_CANNOT_BE_NULL);
        }
        if(carRequestDto.getModel() == null){
            throw new CarBadRequestException(CAR_MODEL_CANNOT_BE_NULL);
        }
        if(carRequestDto.getVin() == null){
            throw new CarBadRequestException(CAR_VIN_CANNOT_BE_NULL);
        }
        if(carRequestDto.getFuel() == null){
            throw new CarBadRequestException(CAR_FUEL_CANNOT_BE_NULL);
        }
        if(carRequestDto.getManufacturerDate() == null){
            throw new CarBadRequestException(CAR_MANUFACTURER_DATE_CANNOT_BE_NULL);
        }
        if(carRequestDto.getEngineCapacity() == null){
            throw new CarBadRequestException(CAR_ENGINE_CAPACITY_CANNOT_BE_NULL);
        }
        if(carRequestDto.getNumberPlate() == null) {
            throw new CarBadRequestException(CAR_NUMBER_PLATE_CANNOT_BE_NULL);
        }
        if(carRequestDto.getBodyType() == null) {
            throw new CarBadRequestException(CAR_BODY_TYPE_CANNOT_BE_NULL);
        }
        if(carRequestDto.getAddress() == null) {
            throw new CarBadRequestException(CAR_ADDRESS_CANNOT_BE_NULL);
        }
    }

    public void validateCar(Car car) {
        if(car == null) {
            throw new CarBadRequestException(CAR_CANNOT_BE_NULL);
        }
    }
}
