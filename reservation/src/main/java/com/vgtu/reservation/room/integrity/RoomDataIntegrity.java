package com.vgtu.reservation.room.integrity;

import com.vgtu.reservation.common.exception.RoomBadRequestException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RoomDataIntegrity {

    public static final String ROOM_ID_CANNOT_BE_NULL = "Room id cannot be null";

    public void validateId(UUID id) {
        if(id == null) {
            throw new RoomBadRequestException(ROOM_ID_CANNOT_BE_NULL);
        }
    }

}
