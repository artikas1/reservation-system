package com.vgtu.reservation.review.controller;

import com.vgtu.reservation.review.dto.ReviewResponseDto;
import com.vgtu.reservation.review.dto.SubmitReviewRequestDto;
import com.vgtu.reservation.review.service.ReviewService;
import com.vgtu.reservation.review.type.EntityType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Used to define endpoints of the reviews.
 */
@RequestMapping("/review")
@RestController
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "Get reviews for an entity", description = "Retrieves all reviews for a car, room or equipment by ID and type")
    @GetMapping("/get")
    public ResponseEntity<List<ReviewResponseDto>> getReviews(
            @Parameter(description = "ID of the entity (car, room or equipment)") @RequestParam UUID reviewedEntityId,
            @Parameter(description = "Type of the entity (CAR, ROOM, EQUIPMENT)") @RequestParam EntityType entityType) {
        return ResponseEntity.ok(reviewService.getEntityReviews(reviewedEntityId, entityType));
    }

    @Operation(summary = "Create a review", description = "Creates a review for a selected entity (car, room or equipment)")
    @PostMapping("/create")
    public ResponseEntity<ReviewResponseDto> createReview(
            @Parameter(description = "Request body containing review details") @Valid @RequestBody SubmitReviewRequestDto submitReviewRequestDto) {
        return ResponseEntity.ok(reviewService.createReview(submitReviewRequestDto));
    }
}
