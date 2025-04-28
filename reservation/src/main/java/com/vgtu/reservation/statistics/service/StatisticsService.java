package com.vgtu.reservation.statistics.service;

import com.vgtu.reservation.car.service.CarService;
import com.vgtu.reservation.equipment.service.EquipmentService;
import com.vgtu.reservation.equipmentreservation.service.EquipmentReservationService;
import com.vgtu.reservation.room.service.RoomService;
import com.vgtu.reservation.roomreservation.service.RoomReservationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.vgtu.reservation.carreservation.service.CarReservationService;

import java.util.HashMap;
import java.util.Map;


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
}
