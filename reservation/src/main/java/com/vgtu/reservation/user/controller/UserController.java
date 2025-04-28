package com.vgtu.reservation.user.controller;

import com.vgtu.reservation.user.dto.UserResponseDto;
import com.vgtu.reservation.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Used to define endpoints of the user.
 */
@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Tag(name = "User Controller", description = "APIs for user management")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get user by JWT token", description = "Retrieves user details by JTW token")
    @GetMapping("/me")
    public UserResponseDto getUserById() { return userService.getUser();}

    @Operation(summary = "Get user by ID", description = "Retrieves user details by their ID")
    @GetMapping("/{userId}")
    public UserResponseDto getUserById(
            @Parameter(description = "ID of the user") @PathVariable UUID userId) {
        return userService.getUserById(userId);
    }

}
