package com.vgtu.reservation.equipmentreservation.service;

import com.vgtu.reservation.auth.service.authentication.AuthenticationService;
import com.vgtu.reservation.common.type.ReservationStatus;
import com.vgtu.reservation.equipment.integrity.EquipmentDataIntegrity;
import com.vgtu.reservation.equipment.service.EquipmentService;
import com.vgtu.reservation.equipmentreservation.dao.EquipmentReservationDao;
import com.vgtu.reservation.equipmentreservation.dto.EquipmentReservationResponseDto;
import com.vgtu.reservation.equipmentreservation.entity.EquipmentReservation;
import com.vgtu.reservation.equipmentreservation.integrity.EquipmentReservationDataIntegrity;
import com.vgtu.reservation.equipmentreservation.mapper.EquipmentReservationMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Contains the business logic for the equipment reservation functionality
 */
@Service
@AllArgsConstructor
public class EquipmentReservationService {

    private final EquipmentDataIntegrity equipmentDataIntegrity;
    private final EquipmentReservationMapper equipmentReservationMapper;
    private final AuthenticationService authenticationService;
    private final EquipmentReservationDao equipmentReservationDao;
    private final EquipmentReservationDataIntegrity equipmentReservationDataIntegrity;
    private final EquipmentService equipmentService;


    public List<EquipmentReservationResponseDto> findAllActiveUserReservations() {
        var user = authenticationService.getAuthenticatedUser();

        return equipmentReservationDao.findAllActiveUserReservations(user.getId())
                .stream().map(equipmentReservationMapper::toEquipmentResponseDto)
                .collect(Collectors.toList());
    }

    public List<EquipmentReservationResponseDto> findAllUserReservations(ReservationStatus reservationStatus) {
        var user = authenticationService.getAuthenticatedUser();

        List<EquipmentReservation> reservations;
        if (reservationStatus != null) {
            reservations = equipmentReservationDao.findByUserIdAndStatus(user.getId(), reservationStatus);
        } else {
            reservations = equipmentReservationDao.findAllUserReservations(user.getId());
        }

        reservations.forEach(this::updateStatusIfExpired);

        return reservations.stream()
                .map(equipmentReservationMapper::toEquipmentResponseDto)
                .collect(Collectors.toList());
    }

    private void updateStatusIfExpired(EquipmentReservation reservation) {
        if (reservation.getReservationStatus() == ReservationStatus.AKTYVI &&
                reservation.getReservedTo().isBefore(LocalDateTime.now()) &&
                reservation.getDeletedAt() == null) {
            reservation.setReservationStatus(ReservationStatus.PASIBAIGUSI);
        }
    }

    public void deleteReservationByEquipmentReservationId(UUID equipmentReservationId) {
        equipmentDataIntegrity.validateId(equipmentReservationId);

        var user = authenticationService.getAuthenticatedUser();
        var reservation = equipmentReservationDao.findReservationByEquipmentReservationId(equipmentReservationId);

        authenticationService.checkAuthorizationBetweenUserAndEquipmentReservation(user, reservation);

        reservation.setDeletedAt(LocalDateTime.now());
        reservation.setReservationStatus(ReservationStatus.ATŠAUKTA);
        equipmentReservationDao.save(reservation);
    }

    public EquipmentReservationResponseDto reserveEquipment(UUID equipmentId, LocalDateTime startTime, LocalDateTime endTime) {
        equipmentDataIntegrity.validateId(equipmentId);
        equipmentReservationDataIntegrity.validateTimeRange(startTime, endTime);

        var user = authenticationService.getAuthenticatedUser();
        var equipment = equipmentService.getEquipmentById(equipmentId);

        equipmentReservationDataIntegrity.checkForConflictingReservations(equipment, startTime, endTime);

        var reservation = equipmentReservationMapper.toEntity(equipment, user, startTime, endTime);
        reservation.setReservationStatus(ReservationStatus.AKTYVI);
        return equipmentReservationMapper.toEquipmentResponseDto(equipmentReservationDao.save(reservation));
    }
}
