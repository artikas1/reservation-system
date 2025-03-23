package com.vgtu.reservation.equipmentreservation.dao;

import com.vgtu.reservation.equipmentreservation.entity.EquipmentReservation;
import com.vgtu.reservation.equipmentreservation.repository.EquipmentReservationRepository;
import com.vgtu.reservation.user.integrity.UserDataIntegrity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Interacts with the database to fetch data related to a car reservations
 */
@Service
@AllArgsConstructor
public class EquipmentReservationDao {

    private final UserDataIntegrity userDataIntegrity;
    private final EquipmentReservationRepository equipmentReservationRepository;

    public List<EquipmentReservation> findAllUserReservations(UUID userId) {
        userDataIntegrity.validateId(userId);

        return equipmentReservationRepository.findByUserId(userId);
    }

}
