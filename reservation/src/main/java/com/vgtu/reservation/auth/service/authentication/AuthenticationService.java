package com.vgtu.reservation.auth.service.authentication;

import com.vgtu.reservation.auth.dto.LoginRequestDto;
import com.vgtu.reservation.auth.dto.LoginResponseDto;
import com.vgtu.reservation.auth.dto.RegisterRequestDto;
import com.vgtu.reservation.auth.mapper.AuthenticationMapper;
import com.vgtu.reservation.auth.service.jwt.JwtService;
import com.vgtu.reservation.carreservation.entity.CarReservation;
import com.vgtu.reservation.common.exception.exceptions.UserNotAuthorizedException;
import com.vgtu.reservation.equipmentreservation.entity.EquipmentReservation;
import com.vgtu.reservation.roomreservation.entity.RoomReservation;
import com.vgtu.reservation.user.dao.UserDao;
import com.vgtu.reservation.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private static final String YOU_ARE_NOT_AUTHORIZED_TO_PERFORM_THIS_ACTION = "You are not authorized to perform this action";

    private final UserDao userDao;
    private final AuthenticationMapper authenticationMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public void register(RegisterRequestDto registerRequestDto) {
        var existingUser = userDao.findByEmail(registerRequestDto.getEmail());

        if (existingUser.isPresent()) {
            throw new EntityNotFoundException("User already exists");
        }

        userDao.saveUser(authenticationMapper.toUser(registerRequestDto));
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));

        var user = userDao.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        var jwtToken = jwtService.generateToken(user);

        return LoginResponseDto.builder()
                .token(jwtToken)
                .build();
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()) {
            throw new EntityNotFoundException("User not authenticated");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User user)) {
            throw new EntityNotFoundException("Authenticated principal is not a User.");
        }

        return user;
    }

    public void checkAuthorizationBetweenUserAndRoomReservation(User user, RoomReservation reservation) {
        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new UserNotAuthorizedException(YOU_ARE_NOT_AUTHORIZED_TO_PERFORM_THIS_ACTION);
        }
    }

    public void checkAuthorizationBetweenUserAndEquipmentReservation(User user, EquipmentReservation reservation) {
        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new UserNotAuthorizedException(YOU_ARE_NOT_AUTHORIZED_TO_PERFORM_THIS_ACTION);
        }
    }

    public void checkAuthorizationBetweenUserAndCarReservation(User user, CarReservation reservation) {
        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new UserNotAuthorizedException(YOU_ARE_NOT_AUTHORIZED_TO_PERFORM_THIS_ACTION);
        }
    }
}
