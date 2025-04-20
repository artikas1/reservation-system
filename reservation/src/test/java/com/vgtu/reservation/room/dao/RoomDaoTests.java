package com.vgtu.reservation.room.dao;

import com.vgtu.reservation.common.exception.RoomNotFoundException;
import com.vgtu.reservation.common.type.Address;
import com.vgtu.reservation.room.entity.Room;
import com.vgtu.reservation.room.integrity.RoomDataIntegrity;
import com.vgtu.reservation.room.repository.RoomRepository;
import com.vgtu.reservation.room.type.RoomType;
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
@Import({RoomDao.class, RoomDataIntegrity.class})
public class RoomDaoTests {

    @Autowired
    private RoomDao roomDao;

    @Autowired
    private RoomRepository roomRepository;

    private Room room;

    @BeforeEach
    void setUp() {
        room = Room.builder()
                .name("Darbo kambarys")
                .floor("1")
                .roomNumber("101")
                .seats("4")
                .description("Darbo kambarys su kompiuteriu")
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .deletedAt(null)
                .roomType(RoomType.DARBO)
                .address(Address.SAULETEKIO_AL_15)
                .build();

        room = roomRepository.save(room);
    }

    @Test
    void getRoomById_shouldReturnRoom() {
        Room found = roomDao.getRoomById(room.getId());

        assertNotNull(found);
        assertEquals(room.getId(), found.getId());
        assertEquals("Darbo kambarys", found.getName());
        assertEquals("1", found.getFloor());
        assertEquals("101", found.getRoomNumber());
        assertEquals("4", found.getSeats());
        assertEquals("Darbo kambarys su kompiuteriu", found.getDescription());
        assertEquals(RoomType.DARBO, found.getRoomType());
        assertEquals(Address.SAULETEKIO_AL_15, found.getAddress());
    }

    @Test
    void getRoomById_shouldThrowIfNotFound() {
        UUID randomId = UUID.randomUUID();

        assertThrows(RoomNotFoundException.class, () -> roomDao.getRoomById(randomId));
    }
}
