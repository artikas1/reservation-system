package com.vgtu.reservation.equipment.dao;

import com.vgtu.reservation.common.exception.EquipmentNotFoundException;
import com.vgtu.reservation.common.type.Address;
import com.vgtu.reservation.equipment.entity.Equipment;
import com.vgtu.reservation.equipment.integrity.EquipmentDataIntegrity;
import com.vgtu.reservation.equipment.repository.EquipmentRepository;
import com.vgtu.reservation.equipment.type.EquipmentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({EquipmentDao.class, EquipmentDataIntegrity.class})
public class EquipmentDaoTests {

    @Autowired
    private EquipmentDao equipmentDao;

    @Autowired
    private EquipmentRepository equipmentRepository;

    private Equipment equipment;

    @BeforeEach
    void setUp() {
        equipment = Equipment.builder()
                .name("Laptop")
                .manufacturer("Levono")
                .model("Legion")
                .code("LL54321")
                .description("Super fast laptop")
                .createdAt(LocalDateTime.of(2020, 6, 1,0,0,0))
                .updatedAt(null)
                .deletedAt(null)
                .equipmentType(EquipmentType.NESIOJAMAS_KOMPIUTERIS)
                .address(Address.NERIES_G_3)
                .build();

        equipment = equipmentRepository.save(equipment);
    }

    @Test
    void testGetEquipmentById_shouldReturnEquipment() {
        var foundEquipment = equipmentDao.getEquipmentById(equipment.getId());

        assertNotNull(foundEquipment);
        assertEquals(equipment.getId(), foundEquipment.getId());
        assertEquals("Laptop", foundEquipment.getName());
        assertEquals("Levono", foundEquipment.getManufacturer());
        assertEquals("Legion", foundEquipment.getModel());
        assertEquals("LL54321", foundEquipment.getCode());
        assertEquals("Super fast laptop", foundEquipment.getDescription());
        assertEquals(LocalDateTime.of(2020, 6, 1,0,0,0), foundEquipment.getCreatedAt());
        assertEquals(EquipmentType.NESIOJAMAS_KOMPIUTERIS, foundEquipment.getEquipmentType());
        assertEquals(Address.NERIES_G_3, foundEquipment.getAddress());

    }

    @Test
    void testGetEquipmentById_shouldThrowIfNotNotFound() {
        UUID randomId = UUID.randomUUID();

        assertThrows(EquipmentNotFoundException.class, () -> equipmentDao.getEquipmentById(randomId));
    }

}
