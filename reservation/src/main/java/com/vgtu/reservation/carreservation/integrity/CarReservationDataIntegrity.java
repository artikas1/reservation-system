package com.vgtu.reservation.carreservation.integrity;

import com.vgtu.reservation.common.exception.exceptions.CarReservationBadRequestException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CarReservationDataIntegrity {

    public static final String ID_CANNOT_BE_NULL = "Car reservation id cannot be null";

    public void validateId(UUID id) {
        if(id == null) {
            throw new CarReservationBadRequestException(ID_CANNOT_BE_NULL);
        }
    }

}
