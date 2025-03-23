package com.vgtu.reservation.equipmentreservation.service;

import com.vgtu.reservation.auth.service.authentication.AuthenticationService;
import com.vgtu.reservation.equipmentreservation.dao.EquipmentReservationDao;
import com.vgtu.reservation.equipmentreservation.dto.EquipmentReservationResponseDto;
import com.vgtu.reservation.equipmentreservation.mapper.EquipmentReservationMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Contains the business logic for the car reservation functionality
 */
@Service
@AllArgsConstructor
public class EquipmentReservationService {

    private final EquipmentReservationMapper equipmentReservationMapper;
    private final AuthenticationService authenticationService;
    private final EquipmentReservationDao equipmentReservationDao;


    public List<EquipmentReservationResponseDto> findAllUserReservations() {
        var user = authenticationService.getAuthenticatedUser();

        return equipmentReservationDao.findAllUserReservations(user.getId())
                .stream().map(equipmentReservationMapper::toEquipmentResponseDto)
                .collect(Collectors.toList());
    }

}
