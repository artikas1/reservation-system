package com.vgtu.reservation.equipment.service;

import com.vgtu.reservation.common.type.Address;
import com.vgtu.reservation.equipment.dao.EquipmentDao;
import com.vgtu.reservation.equipment.entity.Equipment;
import com.vgtu.reservation.equipment.integrity.EquipmentDataIntegrity;
import com.vgtu.reservation.equipment.repository.EquipmentRepository;
import com.vgtu.reservation.equipment.type.EquipmentType;
import com.vgtu.reservation.equipmentreservation.repository.EquipmentReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Contains the business logic for the equipment functionality
 */
@Service
@AllArgsConstructor
public class EquipmentService {

    private final EquipmentDao equipmentDao;
    private final EquipmentRepository equipmentRepository;
    private final EquipmentReservationRepository equipmentReservationRepository;
    private final EquipmentDataIntegrity equipmentDataIntegrity;

    public List<Equipment> getEquipment() {
        return equipmentRepository.findAll();
    }

    public Equipment getEquipmentById(UUID id) {
        equipmentDataIntegrity.validateId(id);

        return equipmentDao.getEquipmentById(id);
    }

    public List<Equipment> getAvailableEquipment(LocalDateTime startTime, LocalDateTime endTime, EquipmentType equipmentType, Address address) {
        List<UUID> reservedEquipmentIds = equipmentReservationRepository.findReservedEquipmentIdsBetween(startTime, endTime);
        List<Equipment> equipments;

        if (reservedEquipmentIds.isEmpty()) {
            equipments = equipmentRepository.findAll();
        } else {
            equipments = equipmentRepository.findByIdNotIn(reservedEquipmentIds);
        }

        if(equipmentType != null) {
            equipments = equipments.stream()
                    .filter( equipment -> equipment.getEquipmentType() == equipmentType)
                    .toList();
        }

        if(address != null) {
            equipments = equipments.stream()
                    .filter( equipment -> equipment.getAddress() == address)
                    .toList();
        }

        return equipments;
    }


}
