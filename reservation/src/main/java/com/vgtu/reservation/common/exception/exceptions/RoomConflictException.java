package com.vgtu.reservation.common.exception.exceptions;

public class RoomConflictException extends RuntimeException {
    public RoomConflictException(String message) {
        super(message);
    }
}
