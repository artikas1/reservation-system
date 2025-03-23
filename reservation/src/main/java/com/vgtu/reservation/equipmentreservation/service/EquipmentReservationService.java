package com.vgtu.reservation.equipmentreservation.service;

import com.vgtu.reservation.auth.service.authentication.AuthenticationService;
import com.vgtu.reservation.equipment.integrity.EquipmentDataIntegrity;
import com.vgtu.reservation.equipmentreservation.dao.EquipmentReservationDao;
import com.vgtu.reservation.equipmentreservation.dto.EquipmentReservationResponseDto;
import com.vgtu.reservation.equipmentreservation.mapper.EquipmentReservationMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Contains the business logic for the car reservation functionality
 */
@Service
@AllArgsConstructor
public class EquipmentReservationService {

    private final EquipmentDataIntegrity equipmentDataIntegrity;
    private final EquipmentReservationMapper equipmentReservationMapper;
    private final AuthenticationService authenticationService;
    private final EquipmentReservationDao equipmentReservationDao;


    public List<EquipmentReservationResponseDto> findAllUserReservations() {
        var user = authenticationService.getAuthenticatedUser();

        return equipmentReservationDao.findAllUserReservations(user.getId())
                .stream().map(equipmentReservationMapper::toEquipmentResponseDto)
                .collect(Collectors.toList());
    }

    public void deleteReservationByEquipmentId(UUID equipmentId) {
        equipmentDataIntegrity.validateId(equipmentId);

        var user = authenticationService.getAuthenticatedUser();
        //var equipment = equipmentService.getEquipmentById(equipmentId)
        var reservation = equipmentReservationDao.findReservationByEquipmentId(equipmentId);

        authenticationService.checkAuthorizationBetweenUserAndEquipmentReservation(user, reservation);

        //equipment.setAvailable(true);
        //equipmentDao.saveEquipment(equipment);

        equipmentReservationDao.deleteReservationByEquipmentId(equipmentId);
    }
}
