package com.vgtu.reservation.equipment.integrity;

import com.vgtu.reservation.common.exception.EquipmentBadRequestException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EquipmentDataIntegrity {

    public static final String EQUIPMENT_ID_CANNOT_BE_NULL = "Equipment id cannot be null";

    public void validateId(UUID id) {
        if(id == null) {
            throw new EquipmentBadRequestException(EQUIPMENT_ID_CANNOT_BE_NULL);
        }
    }

}
