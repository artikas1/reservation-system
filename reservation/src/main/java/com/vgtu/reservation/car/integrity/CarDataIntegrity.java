package com.vgtu.reservation.car.integrity;

import com.vgtu.reservation.common.exception.exceptions.CarBadRequestException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CarDataIntegrity {

    public static final String CAR_ID_CANNOT_BE_NULL = "Car id cannot be null";

    public void validateId(UUID id) {
        if(id == null) {
            throw new CarBadRequestException(CAR_ID_CANNOT_BE_NULL);
        }
    }
}
