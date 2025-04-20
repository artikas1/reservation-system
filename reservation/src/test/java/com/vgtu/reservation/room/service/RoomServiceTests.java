package com.vgtu.reservation.room.service;

import com.vgtu.reservation.common.type.Address;
import com.vgtu.reservation.room.entity.Room;
import com.vgtu.reservation.room.repository.RoomRepository;
import com.vgtu.reservation.room.type.RoomType;
import com.vgtu.reservation.roomreservation.repository.RoomReservationRepository;
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
public class RoomServiceTests {

    @Mock
    private RoomRepository roomRepository;

    @Mock private RoomReservationRepository roomReservationRepository;

    @InjectMocks
    private RoomService roomService;

    private Room room;

    @BeforeEach
    void setUp() {
        room = Room.builder()
                .id(UUID.randomUUID())
                .name("Darbo vieta")
                .floor("10")
                .seats("4")
                .build();
    }

    @Test
    void testGetAllRooms() {
        when(roomRepository.findAll()).thenReturn(List.of(room));

        List<Room> rooms = roomService.getRoom();

        assertNotNull(rooms);
        assertEquals(1, rooms.size());
        assertEquals("Darbo vieta", rooms.get(0).getName());
    }

    @Test
    void getAvailableRooms_shouldReturnFilteredRooms_whenNoReservationsExist() {
        Room room1 = Room.builder()
                .id(UUID.randomUUID())
                .name("Darbo kambarys")
                .floor("1")
                .seats("2")
                .roomType(RoomType.DARBO)
                .address(Address.SAULETEKIO_AL_15)
                .build();

        Room room2 = Room.builder()
                .id(UUID.randomUUID())
                .name("Susitikimu kambarys")
                .floor("1")
                .seats("10")
                .roomType(RoomType.SUSITIKIMU)
                .address(Address.SAULETEKIO_AL_15)
                .build();

        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusDays(1);

        when(roomReservationRepository.findReservedRoomIdsBetween(startTime, endTime))
                .thenReturn(List.of());

        when(roomRepository.findByDeletedAtIsNull())
                .thenReturn(List.of(room1, room2));

        List<Room> result = roomService.getAvailableRooms(startTime, endTime, RoomType.DARBO, Address.SAULETEKIO_AL_15);

        assertEquals(1, result.size());
        assertEquals(RoomType.DARBO, result.get(0).getRoomType());
        assertEquals(Address.SAULETEKIO_AL_15, result.get(0).getAddress());
    }

    @Test
    void getAvailableRooms_shouldReturnAllRooms_whenNoRoomTypeOrAddressFilter() {
        Room room1 = Room.builder()
                .id(UUID.randomUUID())
                .name("Darbo kambarys")
                .floor("2")
                .seats("5")
                .roomType(RoomType.DARBO)
                .address(Address.SAULETEKIO_AL_15)
                .build();

        Room room2 = Room.builder()
                .id(UUID.randomUUID())
                .name("Susitikimu kambarys")
                .floor("3")
                .seats("8")
                .roomType(RoomType.SUSITIKIMU)
                .address(Address.SAULETEKIO_AL_15)
                .build();

        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(3);

        when(roomReservationRepository.findReservedRoomIdsBetween(startTime, endTime)).thenReturn(List.of());
        when(roomRepository.findByDeletedAtIsNull()).thenReturn(List.of(room1, room2));

        List<Room> result = roomService.getAvailableRooms(startTime, endTime, null, null);

        assertEquals(2, result.size());
    }

    @Test
    void getRoom_shouldReturnEmptyList_whenNoRoomsExist() {
        when(roomRepository.findAll()).thenReturn(List.of());

        List<Room> rooms = roomService.getRoom();

        assertNotNull(rooms);
        assertEquals(0, rooms.size());
    }

}
