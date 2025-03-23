package com.vgtu.reservation.equipmentreservation.integrity;

import com.vgtu.reservation.common.exception.exceptions.EquipmentReservationBadRequestException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EquipmentReservationDataIntegrity {

    public static final String ID_CANNOT_BE_NULL = "Equipment reservation id cannot be null";

    public void validateId(UUID id) {
        if (id == null) {
            throw new EquipmentReservationBadRequestException(ID_CANNOT_BE_NULL);
        }
    }
}
