package com.vgtu.reservation.user.dao;

import com.vgtu.reservation.user.entity.User;
import com.vgtu.reservation.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserDao {

    private final UserRepository userRepository;

    public Optional<User> findByEmail(String email) {
        if(email == null) {
            throw new EntityNotFoundException("Email cannot be null");
        }

        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(UUID id) {
        if(id == null) {
            throw  new EntityNotFoundException("Id cannot be null");
        }

        return userRepository.findById(id);
    }

}
