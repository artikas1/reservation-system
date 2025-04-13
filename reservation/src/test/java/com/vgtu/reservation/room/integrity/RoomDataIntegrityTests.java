package com.vgtu.reservation.room.integrity;


import com.vgtu.reservation.common.exception.RoomBadRequestException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RoomDataIntegrityTests {

    private final RoomDataIntegrity roomDataIntegrity = new RoomDataIntegrity();

    @Test
    void validateId_shouldPass_whenIdIsValid() {
        UUID validId = UUID.randomUUID();

        assertDoesNotThrow(() -> roomDataIntegrity.validateId(validId));
    }

    @Test
    void validateId_shouldThrow_whenIdIsNull() {
        assertThrows(RoomBadRequestException.class, () -> roomDataIntegrity.validateId(null));
    }

}
