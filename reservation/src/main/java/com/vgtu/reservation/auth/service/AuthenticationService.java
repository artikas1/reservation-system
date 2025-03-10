package com.vgtu.reservation.auth.service;

import com.vgtu.reservation.user.dao.UserDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserDao userDao;

    public boolean authenticate(String email, String password) {
        var user = userDao.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getPassword().equals(password); // In real application, you should use password hashing
    }
}
