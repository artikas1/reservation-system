package com.vgtu.reservation.carreservation.integrity;

import com.vgtu.reservation.car.entity.Car;
import com.vgtu.reservation.carreservation.entity.CarReservation;
import com.vgtu.reservation.carreservation.repository.CarReservationRepository;
import com.vgtu.reservation.common.exception.CarConflictException;
import com.vgtu.reservation.common.exception.CarReservationBadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarReservationDataIntegrityTests {

    @Mock
    private CarReservationRepository carReservationRepository;

    @InjectMocks
    private CarReservationDataIntegrity carReservationDataIntegrity;

    private UUID validId;
    private LocalDateTime now;
    private LocalDateTime future;
    private Car car;

    @BeforeEach
    void setUp() {
        validId = UUID.randomUUID();
        now = LocalDateTime.now().plusHours(1);
        future = now.plusHours(2);
        car = Car.builder().id(UUID.randomUUID()).build();
    }

    @Test
    void validateId_shouldThrow_whenIdIsNull() {
        assertThrows(CarReservationBadRequestException.class, () -> carReservationDataIntegrity.validateId(null));
    }

    @Test
    void validateTimeRange_shouldThrow_whenStartTimeIsNull() {
        assertThrows(CarReservationBadRequestException.class, () -> carReservationDataIntegrity.validateTimeRange(null, future));
    }

    @Test
    void validateTimeRange_shouldThrow_whenEndTimeIsNull() {
        assertThrows(CarReservationBadRequestException.class, () -> carReservationDataIntegrity.validateTimeRange(now, null));
    }

    @Test
    void validateTimeRange_shouldThrow_whenStartIsAfterEnd() {
        assertThrows(CarReservationBadRequestException.class, () -> carReservationDataIntegrity.validateTimeRange(future, now));
    }

    @Test
    void validateTimeRange_shouldThrow_whenStartIsInThePast() {
        LocalDateTime past = LocalDateTime.now().minusHours(1);
        assertThrows(CarReservationBadRequestException.class, () -> carReservationDataIntegrity.validateTimeRange(past, future));
    }

    @Test
    void validateTimeRange_shouldPass_whenValidTimeRangeProvided() {
        assertDoesNotThrow(() -> carReservationDataIntegrity.validateTimeRange(now, future));
    }

    @Test
    void checkForConflictingReservations_shouldThrow_whenConflictsExist() {
        when(carReservationRepository.findConflictingReservations(car.getId(), now, future)).thenReturn(List.of(new CarReservation()));

        assertThrows(CarConflictException.class, () -> carReservationDataIntegrity.checkForConflictingReservations(car, now, future));
    }

    @Test
    void checkForConflictingReservations_shouldPass_whenNoConflictsExist() {
        when(carReservationRepository.findConflictingReservations(car.getId(), now, future)).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> carReservationDataIntegrity.checkForConflictingReservations(car, now, future));
    }

    @Test
    void checkForConflictingReservationsExceptSelf_shouldThrow_whenConflictsExist() {
        UUID reservationId = UUID.randomUUID();
        when(carReservationRepository.findConflictingReservationsExceptSelf(car.getId(), now, future, reservationId)).thenReturn(List.of(new CarReservation()));

        assertThrows(CarConflictException.class, () -> carReservationDataIntegrity.checkForConflictingReservationsExceptSelf(car, now, future, reservationId));
    }

    @Test
    void checkForConflictingReservationsExceptSelf_shouldPass_whenNoConflictsExist() {
        UUID reservationId = UUID.randomUUID();
        when(carReservationRepository.findConflictingReservationsExceptSelf(car.getId(), now, future, reservationId)).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> carReservationDataIntegrity.checkForConflictingReservationsExceptSelf(car, now, future, reservationId));
    }

    @Test
    void validateCarReservation_shouldThrow_whenReservationIsNull() {
        assertThrows(CarReservationBadRequestException.class, () -> carReservationDataIntegrity.validateCarReservation(null));
    }

    @Test
    void validateCarReservation_shouldPass_whenReservationIsValid() {
        CarReservation reservation = CarReservation.builder().id(UUID.randomUUID()).build();
        assertDoesNotThrow(() -> carReservationDataIntegrity.validateCarReservation(reservation));
    }
}
