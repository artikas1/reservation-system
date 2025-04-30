package com.vgtu.reservation.statistics.service;

import com.vgtu.reservation.car.service.CarService;
import com.vgtu.reservation.equipment.service.EquipmentService;
import com.vgtu.reservation.equipmentreservation.service.EquipmentReservationService;
import com.vgtu.reservation.room.service.RoomService;
import com.vgtu.reservation.roomreservation.service.RoomReservationService;
import com.vgtu.reservation.statistics.dto.MonthUsageDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.vgtu.reservation.carreservation.service.CarReservationService;

import java.util.*;


@Service
@AllArgsConstructor
public class StatisticsService {

    private final CarReservationService carReservationService;
    private final EquipmentReservationService equipmentReservationService;
    private final RoomReservationService roomReservationService;
    private final CarService carService;
    private final EquipmentService equipmentService;
    private final RoomService roomService;

    public Map<String, Long> getReservationsCount() {
        Map<String, Long> counts = new HashMap<>();
        counts.put("car", carReservationService.countAllReservations());
        counts.put("equipment", equipmentReservationService.countAllReservations());
        counts.put("room", roomReservationService.countAllReservations());
        return counts;
    }

    public Map<String, Long> getResourcesCount() {
        Map<String, Long> counts = new HashMap<>();
        counts.put("cars", carService.countAllCars());
        counts.put("equipment", equipmentService.countAllEquipment());
        counts.put("rooms", roomService.countAllRooms());
        return counts;
    }

    public Long getResourceReservationHours(UUID resourceId, int year, int month) {
        if (carService.existsById(resourceId)) {
            return carReservationService.calculateReservationHoursForResource(resourceId, year, month);
        } else if (equipmentService.existsById(resourceId)) {
            return equipmentReservationService.calculateReservationHoursForResource(resourceId, year, month);
        } else if (roomService.existsById(resourceId)) {
            return roomReservationService.calculateReservationHoursForResource(resourceId, year, month);
        } else {
            throw new IllegalArgumentException("Resource not found for id: " + resourceId);
        }
    }

    public List<MonthUsageDto> getResourceUsageHistory(UUID resourceId, int year) {
        List<MonthUsageDto> usageByMonth = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            Long hours;
            if (carService.existsById(resourceId)) {
                hours = carReservationService.calculateReservationHoursForResource(resourceId, year, month);
            } else if (equipmentService.existsById(resourceId)) {
                hours = equipmentReservationService.calculateReservationHoursForResource(resourceId, year, month);
            } else if (roomService.existsById(resourceId)) {
                hours = roomReservationService.calculateReservationHoursForResource(resourceId, year, month);
            } else {
                throw new IllegalArgumentException("Resource not found for id: " + resourceId);
            }
            usageByMonth.add(new MonthUsageDto(String.format("%02d", month), hours));
        }

        return usageByMonth;
    }

}
