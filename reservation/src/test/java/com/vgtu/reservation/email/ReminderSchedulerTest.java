package com.vgtu.reservation.email;

import com.vgtu.reservation.car.entity.Car;
import com.vgtu.reservation.carreservation.entity.CarReservation;
import com.vgtu.reservation.carreservation.service.CarReservationService;
import com.vgtu.reservation.email.scheduler.ReminderScheduler;
import com.vgtu.reservation.email.service.EmailService;
import com.vgtu.reservation.equipment.entity.Equipment;
import com.vgtu.reservation.equipmentreservation.entity.EquipmentReservation;
import com.vgtu.reservation.equipmentreservation.service.EquipmentReservationService;
import com.vgtu.reservation.room.entity.Room;
import com.vgtu.reservation.roomreservation.entity.RoomReservation;
import com.vgtu.reservation.roomreservation.service.RoomReservationService;
import com.vgtu.reservation.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReminderSchedulerTest {
    @Mock
    private CarReservationService carReservationService;
    @Mock
    private RoomReservationService roomReservationService;
    @Mock
    private EquipmentReservationService equipmentReservationService;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private ReminderScheduler reminderScheduler;

    @Test
    void sendReminders_shouldSendEmailsForUpcomingReservations() {
        // Arrange
        User user = User.builder().email("test@example.com").build();
        CarReservation carReservation = CarReservation.builder()
                .user(user)
                .reservedFrom(LocalDateTime.now().plusMinutes(30))
                .car(Car.builder().numberPlate("ABC123").build())
                .build();

        RoomReservation roomReservation = RoomReservation.builder()
                .user(user)
                .reservedFrom(LocalDateTime.now().plusMinutes(40))
                .room(Room.builder().roomNumber("101").build())
                .build();

        EquipmentReservation equipmentReservation = EquipmentReservation.builder()
                .user(user)
                .reservedFrom(LocalDateTime.now().plusMinutes(50))
                .equipment(Equipment.builder().name("Projector").build())
                .build();

        when(carReservationService.findReservationsStartingBetween(any(), any()))
                .thenReturn(List.of(carReservation));
        when(roomReservationService.findReservationsStartingBetween(any(), any()))
                .thenReturn(List.of(roomReservation));
        when(equipmentReservationService.findReservationsStartingBetween(any(), any()))
                .thenReturn(List.of(equipmentReservation));

        // Act
        reminderScheduler.sendReminders();

        // Assert
        verify(emailService, times(1)).sendSimpleEmail(
                eq("test@example.com"),
                eq("Primename apie artėjančią automobilio rezervaciją"),
                contains("ABC123")
        );
        verify(emailService, times(1)).sendSimpleEmail(
                eq("test@example.com"),
                eq("Primename apie artėjančią patalpos rezervaciją"),
                contains("101")
        );
        verify(emailService, times(1)).sendSimpleEmail(
                eq("test@example.com"),
                eq("Primename apie artėjančios įrangos rezervaciją"),
                contains("Projector")
        );
    }
}
