package com.vgtu.reservation.roomreservation.integrity;

import com.vgtu.reservation.common.exception.exceptions.RoomReservationBadRequestException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RoomReservationDataIntegrity {

    public static final String ID_CANNOT_BE_NULL = "Room reservation id cannot be null";

    public void validateId(UUID id) {
        if(id == null) {
            throw new RoomReservationBadRequestException(ID_CANNOT_BE_NULL);
        }
    }
}
