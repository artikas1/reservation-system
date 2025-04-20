package com.vgtu.reservation.roomreservation.dao;

import com.vgtu.reservation.common.exception.RoomReservationBadRequestException;
import com.vgtu.reservation.common.type.ReservationStatus;
import com.vgtu.reservation.room.entity.Room;
import com.vgtu.reservation.room.integrity.RoomDataIntegrity;
import com.vgtu.reservation.roomreservation.entity.RoomReservation;
import com.vgtu.reservation.roomreservation.integrity.RoomReservationDataIntegrity;
import com.vgtu.reservation.roomreservation.repository.RoomReservationRepository;
import com.vgtu.reservation.user.entity.User;
import com.vgtu.reservation.user.integrity.UserDataIntegrity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomReservationDaoTests {

    @Mock private UserDataIntegrity userDataIntegrity;
    @Mock private RoomReservationRepository roomReservationRepository;
    @Mock private RoomReservationDataIntegrity roomReservationDataIntegrity;
    @Mock private RoomDataIntegrity roomDataIntegrity;

    @InjectMocks
    private RoomReservationDao roomReservationDao;

    private RoomReservation reservation;
    private UUID userId;
    private UUID roomId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        roomId = UUID.randomUUID();

        User testUser = User.builder().id(userId).build();
        Room testRoom = Room.builder().id(roomId).build();

        reservation = RoomReservation.builder()
                .id(UUID.randomUUID())
                .user(testUser)
                .room(testRoom)
                .reservedFrom(LocalDateTime.now())
                .reservedTo(LocalDateTime.now().plusHours(2))
                .reservationStatus(ReservationStatus.AKTYVI)
                .build();
    }

    @Test
    void save_shouldCallIntegrityAndSave() {
        when(roomReservationRepository.save(reservation)).thenReturn(reservation);

        RoomReservation saved = roomReservationDao.save(reservation);

        verify(roomReservationDataIntegrity).validateRoomReservation(reservation);
        assertEquals(reservation, saved);
    }

    @Test
    void findAllUserReservations_shouldCallValidationAndReturnResults() {
        when(roomReservationRepository.findByUserId(userId)).thenReturn(List.of(reservation));

        List<RoomReservation> results = roomReservationDao.findAllUserReservations(userId);

        verify(userDataIntegrity).validateId(userId);
        assertEquals(1, results.size());
    }

    @Test
    void findAllActiveUserReservations_shouldReturnActive() {
        when(roomReservationRepository.findActiveByUserId(userId)).thenReturn(List.of(reservation));

        List<RoomReservation> results = roomReservationDao.findAllActiveUserReservations(userId);

        verify(userDataIntegrity).validateId(userId);
        assertEquals(1, results.size());
    }

    @Test
    void findReservationByRoomReservationId_shouldReturnReservation() {
        when(roomReservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));

        RoomReservation found = roomReservationDao.findReservationByRoomReservationId(reservation.getId());

        verify(roomReservationDataIntegrity).validateId(reservation.getId());
        assertEquals(reservation, found);
    }

    @Test
    void findReservationByRoomReservationId_shouldThrow_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(roomReservationRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RoomReservationBadRequestException.class, () -> roomReservationDao.findReservationByRoomReservationId(id));
    }

    @Test
    void deleteReservationByRoomId_shouldDelete() {
        UUID id = UUID.randomUUID();

        roomReservationDao.deleteReservationByRoomId(id);

        verify(roomReservationDataIntegrity).validateId(id);
        verify(roomReservationRepository).deleteById(id);
    }

    @Test
    void findUserReservationsByFilters_shouldReturnFilteredList() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        when(roomReservationRepository.findUserReservationsByFilters(userId, ReservationStatus.AKTYVI, start, end))
                .thenReturn(List.of(reservation));

        var result = roomReservationDao.findUserReservationsByFilters(userId, ReservationStatus.AKTYVI, start, end);

        verify(userDataIntegrity).validateId(userId);
        assertEquals(1, result.size());
    }

    @Test
    void findReservationsStartingBetween_shouldReturnList() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(5);
        when(roomReservationRepository.findReservationsStartingBetween(start, end))
                .thenReturn(List.of(reservation));

        var result = roomReservationDao.findReservationsStartingBetween(start, end);
        assertEquals(1, result.size());
    }

    @Test
    void findByRoomId_shouldReturnList() {
        when(roomReservationRepository.findByRoomIdAndDeletedAtIsNull(roomId))
                .thenReturn(List.of(reservation));

        var result = roomReservationDao.findByRoomId(roomId);

        verify(roomDataIntegrity).validateId(roomId);
        assertEquals(1, result.size());
    }
}
