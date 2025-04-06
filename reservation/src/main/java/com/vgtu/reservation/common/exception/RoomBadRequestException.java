package com.vgtu.reservation.common.exception;

public class RoomBadRequestException extends RuntimeException {
    public RoomBadRequestException(String message) {
        super(message);
    }
}
