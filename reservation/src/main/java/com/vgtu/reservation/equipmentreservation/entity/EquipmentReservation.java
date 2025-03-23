package com.vgtu.reservation.equipmentreservation.entity;

import com.vgtu.reservation.equipment.entity.Equipment;
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
@Table(name = "equipment_reservation")
public class EquipmentReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDate reservedFrom;

    private LocalDate reservedTo;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private LocalDate deletedAt;

}
