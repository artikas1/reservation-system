package com.vgtu.reservation.equipment.integrity;

import com.vgtu.reservation.common.exception.EquipmentBadRequestException;
import com.vgtu.reservation.equipment.dto.EquipmentRequestDto;
import com.vgtu.reservation.equipment.entity.Equipment;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EquipmentDataIntegrity {

    public static final String EQUIPMENT_ID_CANNOT_BE_NULL = "Equipment id cannot be null";
    public static final String EQUIPMENT_REQUEST_CANNOT_BE_NULL = "Equipment request cannot be null";
    public static final String EQUIPMENT_NAME_CANNOT_BE_NULL = "Equipment name cannot be null";
    public static final String EQUIPMENT_TYPE_CANNOT_BE_NULL = "Equipment type cannot be null";
    public static final String EQUIPMENT_ADDRESS_CANNOT_BE_NULL = "Equipment address cannot be null";
    public static final String EQUIPMENT_CANNOT_BE_NULL = "Equipment cannot be null";

    public void validateId(UUID id) {
        if(id == null) {
            throw new EquipmentBadRequestException(EQUIPMENT_ID_CANNOT_BE_NULL);
        }
    }

    public void validateEquipmentRequest(EquipmentRequestDto equipmentRequestDto) {
        if(equipmentRequestDto == null) {
            throw new EquipmentBadRequestException(EQUIPMENT_REQUEST_CANNOT_BE_NULL);
        }
        if(equipmentRequestDto.getName() == null) {
            throw new EquipmentBadRequestException(EQUIPMENT_NAME_CANNOT_BE_NULL);
        }
        if(equipmentRequestDto.getEquipmentType() == null) {
            throw new EquipmentBadRequestException(EQUIPMENT_TYPE_CANNOT_BE_NULL);
        }
        if(equipmentRequestDto.getAddress() == null) {
            throw new EquipmentBadRequestException(EQUIPMENT_ADDRESS_CANNOT_BE_NULL);
        }
    }

    public void validateEquipment(Equipment equipment) {
        if(equipment == null) {
            throw new EquipmentBadRequestException(EQUIPMENT_CANNOT_BE_NULL);
        }
    }
}
