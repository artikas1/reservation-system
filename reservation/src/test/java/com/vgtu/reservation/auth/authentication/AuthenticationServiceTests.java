package com.vgtu.reservation.auth.authentication;

import com.vgtu.reservation.auth.dto.LoginRequestDto;
import com.vgtu.reservation.auth.dto.LoginResponseDto;
import com.vgtu.reservation.auth.dto.RegisterRequestDto;
import com.vgtu.reservation.auth.mapper.AuthenticationMapper;
import com.vgtu.reservation.auth.service.authentication.AuthenticationService;
import com.vgtu.reservation.auth.service.jwt.JwtService;
import com.vgtu.reservation.carreservation.entity.CarReservation;
import com.vgtu.reservation.common.exception.UserNotAuthorizedException;
import com.vgtu.reservation.equipmentreservation.entity.EquipmentReservation;
import com.vgtu.reservation.roomreservation.entity.RoomReservation;
import com.vgtu.reservation.user.dao.UserDao;
import com.vgtu.reservation.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTests {

    @Mock private UserDao userDao;
    @Mock private AuthenticationMapper authenticationMapper;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock private JwtService jwtService;

    @InjectMocks
    private AuthenticationService authenticationService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder().id(UUID.randomUUID()).email("test@example.com").build();
    }

    @Test
    void register_shouldSaveUser_whenNotExists() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setEmail("test@example.com");
        dto.setPassword("pass");
        dto.setName("John");
        dto.setSurname("Doe");

        when(userDao.findByEmail(dto.getEmail())).thenReturn(Optional.empty());

        when(authenticationMapper.toUser(dto)).thenReturn(testUser);

        authenticationService.register(dto);
        verify(userDao).saveUser(testUser);
    }

    @Test
    void register_shouldThrow_whenUserAlreadyExists() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setEmail("test@example.com");

        when(userDao.findByEmail(dto.getEmail())).thenReturn(Optional.of(testUser));

        assertThrows(EntityNotFoundException.class, () -> authenticationService.register(dto));
    }

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() {
        LoginRequestDto loginDto = new LoginRequestDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("pass");

        when(userDao.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(testUser));
        when(jwtService.generateToken(testUser)).thenReturn("mocked-token");

        LoginResponseDto response = authenticationService.login(loginDto);

        assertEquals("mocked-token", response.getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void getAuthenticatedUser_shouldReturnUser() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(testUser);

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);

        User result = authenticationService.getAuthenticatedUser();

        assertEquals(testUser, result);
    }

    @Test
    void getAuthenticatedUser_shouldThrow_whenNoAuthentication() {
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(context);

        assertThrows(EntityNotFoundException.class, () -> authenticationService.getAuthenticatedUser());
    }

    @Test
    void checkAuthorizationBetweenUserAndRoomReservation_shouldThrow_whenDifferentUser() {
        RoomReservation reservation = RoomReservation.builder()
                .user(User.builder().id(UUID.randomUUID()).build())
                .build();

        assertThrows(UserNotAuthorizedException.class,
                () -> authenticationService.checkAuthorizationBetweenUserAndRoomReservation(testUser, reservation));
    }

    @Test
    void checkAuthorizationBetweenUserAndEquipmentReservation_shouldThrow_whenDifferentUser() {
        EquipmentReservation reservation = EquipmentReservation.builder()
                .user(User.builder().id(UUID.randomUUID()).build())
                .build();

        assertThrows(UserNotAuthorizedException.class,
                () -> authenticationService.checkAuthorizationBetweenUserAndEquipmentReservation(testUser, reservation));
    }

    @Test
    void checkAuthorizationBetweenUserAndCarReservation_shouldThrow_whenDifferentUser() {
        CarReservation reservation = CarReservation.builder()
                .user(User.builder().id(UUID.randomUUID()).build())
                .build();

        assertThrows(UserNotAuthorizedException.class,
                () -> authenticationService.checkAuthorizationBetweenUserAndCarReservation(testUser, reservation));
    }

    @Test
    void checkAuthorizationBetweenUserAndCarReservation_shouldPass_whenSameUser() {
        CarReservation reservation = CarReservation.builder()
                .user(testUser)
                .build();

        assertDoesNotThrow(() -> authenticationService.checkAuthorizationBetweenUserAndCarReservation(testUser, reservation));
    }

}
