package com.vgtu.reservation.room.repository;

import com.vgtu.reservation.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {
    List<Room> findByIdNotIn(List<UUID> reservedRoomIds);
}
