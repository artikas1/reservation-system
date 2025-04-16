package com.vgtu.reservation.room.integrity;

import com.vgtu.reservation.common.exception.RoomBadRequestException;
import com.vgtu.reservation.room.dto.RoomRequestDto;
import com.vgtu.reservation.room.entity.Room;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RoomDataIntegrity {

    public static final String ROOM_ID_CANNOT_BE_NULL = "Room id cannot be null";
    public static final String ROOM_REQUEST_CANNOT_BE_NULL = "Room request cannot be null";
    public static final String ROOM_NAME_CANNOT_BE_NULL = "Room name cannot be null";
    public static final String ROOM_FLOOR_CANNOT_BE_NULL = "Room floor cannot be null";
    public static final String ROOM_TYPE_CANNOT_BE_NULL = "Room type cannot be null";
    public static final String ROOM_ADDRESS_CANNOT_BE_NULL = "Room address cannot be null";
    public static final String ROOM_CANNOT_BE_NULL = "Room cannot be null";

    public void validateId(UUID id) {
        if(id == null) {
            throw new RoomBadRequestException(ROOM_ID_CANNOT_BE_NULL);
        }
    }

    public void validateRoomRequest(RoomRequestDto roomRequestDto) {
        if (roomRequestDto == null) {
            throw new RoomBadRequestException(ROOM_REQUEST_CANNOT_BE_NULL);
        }
        if (roomRequestDto.getName() == null) {
            throw new RoomBadRequestException(ROOM_NAME_CANNOT_BE_NULL);
        }
        if (roomRequestDto.getFloor() == null) {
            throw new RoomBadRequestException(ROOM_FLOOR_CANNOT_BE_NULL);
        }
        if (roomRequestDto.getRoomType() == null) {
            throw new RoomBadRequestException(ROOM_TYPE_CANNOT_BE_NULL);
        }
        if (roomRequestDto.getAddress() == null) {
            throw new RoomBadRequestException(ROOM_ADDRESS_CANNOT_BE_NULL);
        }
    }

    public void validateRoom(Room room) {
        if (room == null) {
            throw new RoomBadRequestException(ROOM_CANNOT_BE_NULL);
        }
    }
}
