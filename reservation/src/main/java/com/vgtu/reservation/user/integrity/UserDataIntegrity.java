package com.vgtu.reservation.user.integrity;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserDataIntegrity {

    public void validateId(UUID id) {
        if(id == null) {
            throw new IllegalArgumentException();
        }
    }
}
