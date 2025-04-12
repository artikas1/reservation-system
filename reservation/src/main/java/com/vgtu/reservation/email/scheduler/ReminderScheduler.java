package com.vgtu.reservation.email.scheduler;

import com.vgtu.reservation.carreservation.entity.CarReservation;
import com.vgtu.reservation.carreservation.service.CarReservationService;
import com.vgtu.reservation.email.service.EmailService;
import com.vgtu.reservation.equipmentreservation.entity.EquipmentReservation;
import com.vgtu.reservation.equipmentreservation.service.EquipmentReservationService;
import com.vgtu.reservation.roomreservation.entity.RoomReservation;
import com.vgtu.reservation.roomreservation.service.RoomReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReminderScheduler {

    private final CarReservationService carReservationService;
    private final RoomReservationService roomReservationService;
    private final EquipmentReservationService equipmentReservationService;
    private final EmailService emailService;

    @Scheduled(fixedRate = 1800000) // every 30 minutes
//    @Scheduled(fixedRate = 60000) // every 1 minute
    public void sendReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime inOneHour = now.plusHours(1);

        log.info("🔔 Checking for reservations starting within one hour...");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Cars
        List<CarReservation> carReservations = carReservationService.findReservationsStartingBetween(now, inOneHour);
        for (CarReservation r : carReservations) {
            log.info("📧 Sending car reminder to: {} (starts at {})", r.getUser().getEmail(), r.getReservedFrom());
            String email = r.getUser().getEmail();

            emailService.sendSimpleEmail(
                    email,
                    "Primename apie artėjančią automobilio rezervaciją",
                    "Jūsų rezervacija su automobilio numeriu " +
                            r.getCar().getNumberPlate() + " prasidės " + r.getReservedFrom().format(formatter)
            );
        }

        // Rooms
        List<RoomReservation> roomReservations = roomReservationService.findReservationsStartingBetween(now, inOneHour);
        for (RoomReservation r : roomReservations) {
            log.info("📧 Sending room reminder to: {} (starts at {})", r.getUser().getEmail(), r.getReservedFrom());
            String email = r.getUser().getEmail();

            emailService.sendSimpleEmail(
                    email,
                    "Primename apie artėjančią patalpos rezervaciją",
                    "Jūsų rezervacija su patalpos numeriu " +
                            r.getRoom().getRoomNumber() + " prasidės " + r.getReservedFrom().format(formatter)
            );
        }

        // Equipment
        List<EquipmentReservation> equipmentReservations = equipmentReservationService.findReservationsStartingBetween(now, inOneHour);
        for (EquipmentReservation r : equipmentReservations) {
            log.info("📧 Sending equipment reminder to: {} (starts at {})", r.getUser().getEmail(), r.getReservedFrom());
            String email = r.getUser().getEmail();

            emailService.sendSimpleEmail(
                    email,
                    "Primename apie artėjančios įrangos rezervaciją",
                    "Jūsų darbo priemonės \"" + r.getEquipment().getName() + "\" rezervacija prasidės" +
                            r.getReservedFrom().format(formatter)
            );
        }

        log.info("✅ Reminder emails sent successfully.");
    }
}