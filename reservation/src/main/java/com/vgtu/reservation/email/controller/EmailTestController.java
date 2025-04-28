package com.vgtu.reservation.email.controller;

import com.vgtu.reservation.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Used to define endpoints of the emails.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/test-email")
public class EmailTestController {

    private final EmailService emailService;

    @GetMapping
    public String sendTestEmail() {
        emailService.sendSimpleEmail(
                "rezervis.bakalauras@gmail.com",  // test by sending to yourself
                "Test email from Spring Boot",
                "Sveikas! Čia testinis el. laiškas iš tavo Spring aplikacijos 🚀"
        );
        return "Email sent!";
    }
}
