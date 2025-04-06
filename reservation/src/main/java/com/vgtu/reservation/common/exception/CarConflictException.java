package com.vgtu.reservation.common.exception;

public class CarConflictException extends RuntimeException {
    public CarConflictException(String message) {
        super(message);
    }
}
