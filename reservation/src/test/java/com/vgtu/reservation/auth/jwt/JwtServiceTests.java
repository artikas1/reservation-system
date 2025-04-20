package com.vgtu.reservation.auth.jwt;


import com.vgtu.reservation.auth.service.jwt.JwtService;
import com.vgtu.reservation.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTests {

    private JwtService jwtService;

    @Mock
    private User mockUser;

    private final String secretKey = Base64.getEncoder()
            .encodeToString("mySuperSecureTestKey1234567890123456".getBytes());

    private final long expirationTime = 1000 * 60 * 60; // 1 hour

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtService, "expirationTime", expirationTime);

        when(mockUser.getUsername()).thenReturn("mockuser@example.com");
    }

    @Test
    void testGenerateToken_WithMockedUser_ReturnsValidToken() {
        String token = jwtService.generateToken(mockUser);

        assertNotNull(token);
        assertTrue(token.startsWith("eyJ"));
        verify(mockUser, times(1)).getUsername();
    }

    @Test
    void testExtractUsername_ReturnsCorrectUsernameFromToken() {
        String token = jwtService.generateToken(mockUser);

        String extracted = jwtService.extractUsername(token);
        assertEquals("mockuser@example.com", extracted);
    }

    @Test
    void testIsTokenValid_ReturnsTrueForMatchingUser() {
        String token = jwtService.generateToken(mockUser);
        boolean result = jwtService.isTokenValid(token, mockUser);

        assertTrue(result);
        verify(mockUser, atLeastOnce()).getUsername();
    }

    @Test
    void testIsTokenValid_ReturnsFalseForMismatchedUser() {
        String token = jwtService.generateToken(mockUser);

        User anotherUser = mock(User.class);
        when(anotherUser.getUsername()).thenReturn("another@example.com");

        boolean result = jwtService.isTokenValid(token, anotherUser);

        assertFalse(result);
        verify(anotherUser).getUsername();
    }

}
