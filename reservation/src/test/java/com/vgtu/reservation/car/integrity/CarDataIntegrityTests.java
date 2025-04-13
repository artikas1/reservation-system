package com.vgtu.reservation.car.integrity;

import com.vgtu.reservation.common.exception.CarBadRequestException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CarDataIntegrityTests {

    private final CarDataIntegrity carDataIntegrity = new CarDataIntegrity();

    @Test
    void validateId_shouldPass_whenIdIsValid() {
        UUID validId = UUID.randomUUID();

        assertDoesNotThrow(() -> carDataIntegrity.validateId(validId));
    }

    @Test
    void validateId_shouldThrow_whenIdIsNull() {
        assertThrows(CarBadRequestException.class, () -> carDataIntegrity.validateId(null));
    }
}
