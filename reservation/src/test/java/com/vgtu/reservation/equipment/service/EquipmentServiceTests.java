package com.vgtu.reservation.equipment.service;

import com.vgtu.reservation.common.type.Address;
import com.vgtu.reservation.equipment.entity.Equipment;
import com.vgtu.reservation.equipment.repository.EquipmentRepository;
import com.vgtu.reservation.equipment.type.EquipmentType;
import com.vgtu.reservation.equipmentreservation.repository.EquipmentReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EquipmentServiceTests {

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock private EquipmentReservationRepository equipmentReservationRepository;

    @InjectMocks
    private EquipmentService equipmentService;

    private Equipment equipment;

    @BeforeEach
    void setUp() {
        equipment = Equipment.builder()
                .id(UUID.randomUUID())
                .name("Projector")
                .manufacturer("Epson")
                .model("EB-X41")
                .build();
    }

    @Test
    void testGetAllEquipment() {
        when(equipmentRepository.findAll()).thenReturn(List.of(equipment));

        List<Equipment> equipment = equipmentService.getEquipment();

        assertNotNull(equipment);
        assertEquals(1, equipment.size());
        assertEquals("Projector", equipment.get(0).getName());
    }

    @Test
    void getAvailableEquipment_shouldReturnFilteredEquipment_whenNoReservationsExist() {
        Equipment equipment1 = Equipment.builder()
                .id(UUID.randomUUID())
                .name("Projector")
                .manufacturer("Epson")
                .model("EB-X41")
                .equipmentType(EquipmentType.PROJEKTORIUS)
                .address(Address.SAULETEKIO_AL_15)
                .build();

        Equipment equipment2 = Equipment.builder()
                .id(UUID.randomUUID())
                .name("Laptop")
                .manufacturer("Dell")
                .model("XPS 13")
                .equipmentType(EquipmentType.NESIOJAMAS_KOMPIUTERIS)
                .address(Address.SAULETEKIO_AL_15)
                .build();

        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(6);

        when(equipmentReservationRepository.findReservedEquipmentIdsBetween(startTime, endTime))
                .thenReturn(List.of());

        when(equipmentRepository.findAll())
                .thenReturn(List.of(equipment1, equipment2));

        List<Equipment> result = equipmentService.getAvailableEquipment(startTime, endTime, EquipmentType.PROJEKTORIUS, Address.SAULETEKIO_AL_15);

        assertEquals(1, result.size());
        assertEquals(EquipmentType.PROJEKTORIUS, result.get(0).getEquipmentType());
        assertEquals(Address.SAULETEKIO_AL_15, result.get(0).getAddress());
    }

}
