package com.vgtu.reservation.carreservation.entity;

import com.vgtu.reservation.car.entity.Car;
import com.vgtu.reservation.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "car_reservation")
public class CarReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDate reservedFrom;

    private LocalDate reservedTo;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private LocalDate deletedAt;

}
