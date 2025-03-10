package com.vgtu.reservation.auth.controller;

import com.vgtu.reservation.auth.dto.LoginRequestDto;
import com.vgtu.reservation.auth.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Parameter(description = "User login credentials") @Valid @RequestBody LoginRequestDto loginRequestDto) {
        boolean isAuthenticated = authenticationService.authenticate(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        if (isAuthenticated) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

}
