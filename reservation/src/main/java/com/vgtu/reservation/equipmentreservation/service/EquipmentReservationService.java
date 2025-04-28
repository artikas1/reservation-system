package com.vgtu.reservation.equipmentreservation.service;

import com.vgtu.reservation.auth.service.authentication.AuthenticationService;
import com.vgtu.reservation.common.type.ReservationStatus;
import com.vgtu.reservation.equipment.integrity.EquipmentDataIntegrity;
import com.vgtu.reservation.equipment.service.EquipmentService;
import com.vgtu.reservation.equipmentreservation.dao.EquipmentReservationDao;
import com.vgtu.reservation.equipmentreservation.dto.EquipmentReservationResponseDto;
import com.vgtu.reservation.equipmentreservation.dto.EquipmentReservationTimeRangeDto;
import com.vgtu.reservation.equipmentreservation.entity.EquipmentReservation;
import com.vgtu.reservation.equipmentreservation.integrity.EquipmentReservationDataIntegrity;
import com.vgtu.reservation.equipmentreservation.mapper.EquipmentReservationMapper;
import jakarta.transaction.Transactional;
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
@Transactional
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

    public EquipmentReservationResponseDto updateEquipmentReservation(UUID reservationId, LocalDateTime newStart, LocalDateTime newEnd) {
        var reservation = equipmentReservationDao.findReservationByEquipmentReservationId(reservationId);
        var user = authenticationService.getAuthenticatedUser();
        authenticationService.checkAuthorizationBetweenUserAndEquipmentReservation(user, reservation);

        LocalDateTime start = newStart != null ? newStart : reservation.getReservedFrom();
        LocalDateTime end = newEnd != null ? newEnd : reservation.getReservedTo();

        equipmentReservationDataIntegrity.validateTimeRange(start, end);
        equipmentReservationDataIntegrity.checkForConflictingReservationsExceptSelf(reservation.getEquipment(), start, end, reservationId);

        reservation.setReservedFrom(start);
        reservation.setReservedTo(end);
        reservation.setUpdatedAt(LocalDateTime.now());

        var updated = equipmentReservationDao.save(reservation);
        return equipmentReservationMapper.toEquipmentResponseDto(updated);
    }

    public List<EquipmentReservationResponseDto> findAllUserReservations(ReservationStatus reservationStatus, LocalDateTime startTime, LocalDateTime endTime) {
        var user = authenticationService.getAuthenticatedUser();

        List<EquipmentReservation> reservations = equipmentReservationDao.findUserReservationsByFilters(
                user.getId(), reservationStatus, startTime, endTime
        );

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

    public List<EquipmentReservation> findReservationsStartingBetween(LocalDateTime start, LocalDateTime end) {
        return equipmentReservationDao.findReservationsStartingBetween(start, end);
    }

    public List<EquipmentReservationTimeRangeDto> getReservedTimeRangesForEquipment(UUID equipmentId, UUID excludeReservationId) {
        equipmentDataIntegrity.validateId(equipmentId);

        return equipmentReservationDao.findByEquipmentId(equipmentId).stream()
                .filter(r -> excludeReservationId == null || !r.getId().equals(excludeReservationId))
                .map(equipmentReservationMapper::toTimeRangeDto)
                .collect(Collectors.toList());
    }

    public long countAllReservations() {
        return equipmentReservationDao.countAll();
    }
}
