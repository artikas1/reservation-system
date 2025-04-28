package com.vgtu.reservation.roomreservation.service;

import com.vgtu.reservation.auth.service.authentication.AuthenticationService;
import com.vgtu.reservation.common.type.ReservationStatus;
import com.vgtu.reservation.room.entity.Room;
import com.vgtu.reservation.room.integrity.RoomDataIntegrity;
import com.vgtu.reservation.room.service.RoomService;
import com.vgtu.reservation.roomreservation.dao.RoomReservationDao;
import com.vgtu.reservation.roomreservation.dto.RoomReservationResponseDto;
import com.vgtu.reservation.roomreservation.entity.RoomReservation;
import com.vgtu.reservation.roomreservation.integrity.RoomReservationDataIntegrity;
import com.vgtu.reservation.roomreservation.mapper.RoomReservationMapper;
import com.vgtu.reservation.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomReservationServiceTests {

    @Mock
    private RoomDataIntegrity roomDataIntegrity;
    @Mock
    private RoomReservationMapper roomReservationMapper;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private RoomReservationDao roomReservationDao;
    @Mock
    private RoomService roomService;
    @Mock
    private RoomReservationDataIntegrity roomReservationDataIntegrity;

    @InjectMocks
    private RoomReservationService roomReservationService;

    private User testUser;
    private Room testRoom;
    private RoomReservation testReservation;

    @BeforeEach
    void setUp() {
        testUser = User.builder().id(UUID.randomUUID()).build();
        testRoom = Room.builder().id(UUID.randomUUID()).build();
        testReservation = RoomReservation.builder()
                .id(UUID.randomUUID())
                .user(testUser)
                .room(testRoom)
                .reservedFrom(LocalDateTime.now().plusDays(1))
                .reservedTo(LocalDateTime.now().plusDays(2))
                .reservationStatus(ReservationStatus.AKTYVI)
                .build();
    }

    @Test
    void reserveRoom_shouldCreateReservationSuccessfully() {
        LocalDateTime from = LocalDateTime.now().plusDays(1);
        LocalDateTime to = from.plusHours(3);

        when(authenticationService.getAuthenticatedUser()).thenReturn(testUser);
        when(roomService.getRoomById(testRoom.getId())).thenReturn(testRoom);
        when(roomReservationMapper.toEntity(testRoom, testUser, from, to)).thenReturn(testReservation);
        when(roomReservationDao.save(testReservation)).thenReturn(testReservation);
        when(roomReservationMapper.toRoomResponseDto(testReservation)).thenReturn(
                RoomReservationResponseDto.builder().id(testReservation.getId()).build()
        );

        var result = roomReservationService.reserveRoom(testRoom.getId(), from, to);

        assertNotNull(result);
        verify(roomDataIntegrity).validateId(testRoom.getId());
        verify(roomReservationDataIntegrity).validateTimeRange(from, to);
        verify(roomReservationDataIntegrity).checkForConflictingReservations(testRoom, from, to);
    }

    @Test
    void updateRoomReservation_shouldUpdateTimesAndReturnDto() {
        UUID id = testReservation.getId();
        LocalDateTime newFrom = LocalDateTime.now().plusDays(3);
        LocalDateTime newTo = newFrom.plusHours(4);

        when(roomReservationDao.findReservationByRoomReservationId(id)).thenReturn(testReservation);
        when(authenticationService.getAuthenticatedUser()).thenReturn(testUser);
        when(roomReservationDao.save(any())).thenReturn(testReservation);
        when(roomReservationMapper.toRoomResponseDto(any())).thenReturn(
                RoomReservationResponseDto.builder().id(id).build()
        );

        var result = roomReservationService.updateRoomReservation(id, newFrom, newTo);

        assertEquals(id, result.getId());
        verify(roomReservationDataIntegrity).validateTimeRange(newFrom, newTo);
        verify(roomReservationDataIntegrity).checkForConflictingReservationsExceptSelf(testRoom, newFrom, newTo, id);
    }

    @Test
    void updateRoomReservation_shouldUseOldTimeWhenNewIsNull() {
        UUID id = testReservation.getId();

        when(roomReservationDao.findReservationByRoomReservationId(id)).thenReturn(testReservation);
        when(authenticationService.getAuthenticatedUser()).thenReturn(testUser);
        when(roomReservationDao.save(testReservation)).thenReturn(testReservation);
        when(roomReservationMapper.toRoomResponseDto(testReservation)).thenReturn(
                RoomReservationResponseDto.builder().id(id).build()
        );

        var result = roomReservationService.updateRoomReservation(id, null, null);

        assertNotNull(result);
        verify(roomReservationDataIntegrity).validateTimeRange(testReservation.getReservedFrom(), testReservation.getReservedTo());
    }

    @Test
    void deleteReservation_shouldSoftDelete() {
        UUID id = testReservation.getId();

        when(authenticationService.getAuthenticatedUser()).thenReturn(testUser);
        when(roomReservationDao.findReservationByRoomReservationId(id)).thenReturn(testReservation);

        roomReservationService.deleteReservationByRoomReservationId(id);

        assertEquals(ReservationStatus.ATŠAUKTA, testReservation.getReservationStatus());
        assertNotNull(testReservation.getDeletedAt());
        verify(roomReservationDao).save(testReservation);
    }

    @Test
    void findAllUserReservations_withStatusFilter_shouldReturnFilteredList() {
        when(authenticationService.getAuthenticatedUser()).thenReturn(testUser);
        when(roomReservationDao.findUserReservationsByFilters(eq(testUser.getId()), eq(ReservationStatus.PASIBAIGUSI), any(), any()))
                .thenReturn(List.of(testReservation));

        when(roomReservationMapper.toRoomResponseDto(any()))
                .thenReturn(RoomReservationResponseDto.builder().id(testReservation.getId()).build());

        var result = roomReservationService.findAllUserReservations(ReservationStatus.PASIBAIGUSI, null, null);

        assertEquals(1, result.size());
    }

    @Test
    void findAllUserReservations_shouldUpdateExpired() {
        RoomReservation expired = RoomReservation.builder()
                .id(UUID.randomUUID())
                .user(testUser)
                .room(testRoom)
                .reservedFrom(LocalDateTime.now().minusDays(2))
                .reservedTo(LocalDateTime.now().minusDays(1))
                .reservationStatus(ReservationStatus.AKTYVI)
                .build();

        when(authenticationService.getAuthenticatedUser()).thenReturn(testUser);
        when(roomReservationDao.findUserReservationsByFilters(any(), any(), any(), any()))
                .thenReturn(List.of(testReservation, expired));

        when(roomReservationMapper.toRoomResponseDto(any()))
                .thenReturn(
                        RoomReservationResponseDto.builder().id(testReservation.getId()).build(),
                        RoomReservationResponseDto.builder().id(expired.getId()).build()
                );

        var result = roomReservationService.findAllUserReservations(null, null, null);

        assertEquals(2, result.size());
        assertEquals(ReservationStatus.PASIBAIGUSI, expired.getReservationStatus());
    }

    @Test
    void findAllActiveUserReservations_shouldReturnList() {
        when(authenticationService.getAuthenticatedUser()).thenReturn(testUser);
        when(roomReservationDao.findAllActiveUserReservations(testUser.getId())).thenReturn(List.of(testReservation));
        when(roomReservationMapper.toRoomResponseDto(testReservation)).thenReturn(
                RoomReservationResponseDto.builder().id(testReservation.getId()).build()
        );

        var result = roomReservationService.findAllActiveUserReservations();
        assertEquals(1, result.size());
    }

    @Test
    void findReservationsStartingBetween_shouldReturnList() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(5);

        when(roomReservationDao.findReservationsStartingBetween(start, end))
                .thenReturn(List.of(testReservation));

        var result = roomReservationService.findReservationsStartingBetween(start, end);

        assertEquals(1, result.size());
    }

    @Test
    void getReservedTimeRangesForRoom_shouldExcludeGivenId() {
        when(roomReservationDao.findByRoomId(testRoom.getId()))
                .thenReturn(List.of(testReservation));

        var result = roomReservationService.getReservedTimeRangesForRoom(testRoom.getId(), testReservation.getId());

        assertEquals(0, result.size());
    }
}
