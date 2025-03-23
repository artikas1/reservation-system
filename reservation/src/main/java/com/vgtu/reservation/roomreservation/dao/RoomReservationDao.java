package com.vgtu.reservation.roomreservation.dao;

import com.vgtu.reservation.roomreservation.entity.RoomReservation;
import com.vgtu.reservation.roomreservation.repository.RoomReservationRepository;
import com.vgtu.reservation.user.integrity.UserDataIntegrity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Interacts with the database to fetch data related to a room reservations
 */
@Service
@AllArgsConstructor
public class RoomReservationDao {

    private final UserDataIntegrity userDataIntegrity;
    private final RoomReservationRepository roomReservationRepository;

    public List<RoomReservation> findAllUserReservations(UUID userId) {
        userDataIntegrity.validateId(userId);

        return roomReservationRepository.findByUserId(userId);
    }

}
