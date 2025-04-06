package com.vgtu.reservation.equipmentreservation.integrity;

import com.vgtu.reservation.common.exception.EquipmentConflictException;
import com.vgtu.reservation.common.exception.EquipmentReservationBadRequestException;
import com.vgtu.reservation.equipment.entity.Equipment;
import com.vgtu.reservation.equipmentreservation.entity.EquipmentReservation;
import com.vgtu.reservation.equipmentreservation.repository.EquipmentReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EquipmentReservationDataIntegrity {

    private final EquipmentReservationRepository equipmentReservationRepository;

    public static final String ID_CANNOT_BE_NULL = "Equipment reservation id cannot be null";

    public void validateId(UUID id) {
        if (id == null) {
            throw new EquipmentReservationBadRequestException(ID_CANNOT_BE_NULL);
        }
    }

    public void validateTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            throw new EquipmentReservationBadRequestException("Start time and end time cannot be null");
        }
        if (startTime.isAfter(endTime)) {
            throw new EquipmentReservationBadRequestException("Start time must be before end time");
        }
        if(startTime.isBefore(LocalDateTime.now())) {
            throw new EquipmentReservationBadRequestException("Cannot reserve for past time");
        }
    }

    public void checkForConflictingReservations(Equipment equipment, LocalDateTime startTime, LocalDateTime endTime) {
        List<EquipmentReservation> conflictingReservations = equipmentReservationRepository.findConflictingReservations(
                equipment.getId(),
                startTime,
                endTime
        );

        if(!conflictingReservations.isEmpty()) {
            throw new EquipmentConflictException("Equipment is already reserved during this time");
        }
    }

    public void validateEquipmentReservation(EquipmentReservation equipmentReservation) {
        if(equipmentReservation == null) {
            throw new EquipmentReservationBadRequestException("Car reservation cannot be null");
        }
    }
}
