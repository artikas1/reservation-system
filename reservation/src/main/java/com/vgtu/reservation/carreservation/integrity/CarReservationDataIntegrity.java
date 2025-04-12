package com.vgtu.reservation.carreservation.integrity;

import com.vgtu.reservation.car.entity.Car;
import com.vgtu.reservation.carreservation.entity.CarReservation;
import com.vgtu.reservation.carreservation.repository.CarReservationRepository;
import com.vgtu.reservation.common.exception.CarConflictException;
import com.vgtu.reservation.common.exception.CarReservationBadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CarReservationDataIntegrity {

    private final CarReservationRepository carReservationRepository;

    public static final String ID_CANNOT_BE_NULL = "Car reservation id cannot be null";

    public void validateId(UUID id) {
        if (id == null) {
            throw new CarReservationBadRequestException(ID_CANNOT_BE_NULL);
        }
    }

    public void validateTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            throw new CarReservationBadRequestException("Start time and end time cannot be null");
        }
        if (startTime.isAfter(endTime)) {
            throw new CarReservationBadRequestException("Start time must be before end time");
        }
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new CarReservationBadRequestException("Cannot reserve for past time");
        }
    }

    public void checkForConflictingReservations(Car car, LocalDateTime startTime, LocalDateTime endTime) {
        List<CarReservation> conflictingReservations = carReservationRepository.findConflictingReservations(
                car.getId(),
                startTime,
                endTime
        );

        if (!conflictingReservations.isEmpty()) {
            throw new CarConflictException("Car is already reserved for the requested time period");
        }
    }

    public void checkForConflictingReservationsExceptSelf(Car car, LocalDateTime startTime, LocalDateTime endTime, UUID reservationId) {
        List<CarReservation> conflictingReservations = carReservationRepository.findConflictingReservationsExceptSelf(
                car.getId(), startTime, endTime, reservationId
        );

        if (!conflictingReservations.isEmpty()) {
            throw new CarConflictException("Car is already reserved for the requested time period");
        }
    }


    public void validateCarReservation(CarReservation carReservation) {
        if (carReservation == null) {
            throw new CarReservationBadRequestException("Car reservation cannot be null");
        }
    }


}
