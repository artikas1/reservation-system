package com.vgtu.reservation.equipment.integrity;

import com.vgtu.reservation.common.exception.EquipmentBadRequestException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EquipmentDataIntegrityTests {

    private final EquipmentDataIntegrity equipmentDataIntegrity = new EquipmentDataIntegrity();

    @Test
    void validateId_shouldPass_whenIdIsValid() {
        UUID validId = UUID.randomUUID();

        assertDoesNotThrow(() -> equipmentDataIntegrity.validateId(validId));
    }

    @Test
    void validateId_shouldThrow_whenIdIsNull() {
        assertThrows(EquipmentBadRequestException.class, () -> equipmentDataIntegrity.validateId(null));
    }
}
