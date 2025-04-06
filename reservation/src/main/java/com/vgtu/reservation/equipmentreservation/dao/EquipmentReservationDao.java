package com.vgtu.reservation.equipmentreservation.dao;

import com.vgtu.reservation.equipmentreservation.entity.EquipmentReservation;
import com.vgtu.reservation.equipmentreservation.integrity.EquipmentReservationDataIntegrity;
import com.vgtu.reservation.equipmentreservation.repository.EquipmentReservationRepository;
import com.vgtu.reservation.common.type.ReservationStatus;
import com.vgtu.reservation.user.integrity.UserDataIntegrity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Interacts with the database to fetch data related to a equipment reservations
 */
@Service
@AllArgsConstructor
public class EquipmentReservationDao {

    private final UserDataIntegrity userDataIntegrity;
    private final EquipmentReservationRepository equipmentReservationRepository;
    private final EquipmentReservationDataIntegrity equipmentReservationDataIntegrity;

    public EquipmentReservation save(EquipmentReservation equipmentReservation) {
        equipmentReservationDataIntegrity.validateEquipmentReservation(equipmentReservation);

        return equipmentReservationRepository.save(equipmentReservation);
    }

    public List<EquipmentReservation> findAllUserReservations(UUID userId) {
        userDataIntegrity.validateId(userId);

        return equipmentReservationRepository.findByUserId(userId);
    }

    public List <EquipmentReservation> findAllActiveUserReservations(UUID userId) {
        userDataIntegrity.validateId(userId);

        return equipmentReservationRepository.findActiveByUserId(userId);
    }

    public EquipmentReservation findReservationByEquipmentReservationId(UUID equipmentReservationId) {
        equipmentReservationDataIntegrity.validateId(equipmentReservationId);

        return equipmentReservationRepository.findById(equipmentReservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
    }

    public void deleteReservationByEquipmentReservationId(UUID equipmentReservationId) {
        equipmentReservationDataIntegrity.validateId(equipmentReservationId);

        equipmentReservationRepository.deleteById(equipmentReservationId);
    }

    public List<EquipmentReservation> findByUserIdAndStatus(UUID userId, ReservationStatus reservationStatus) {
        userDataIntegrity.validateId(userId);
        return equipmentReservationRepository.findByUserIdAndReservationStatus(userId, reservationStatus);
    }
}
