package com.vgtu.reservation.review.integrity;

import com.vgtu.reservation.common.exception.DtoValidationException;
import com.vgtu.reservation.review.dto.SubmitReviewRequestDto;
import com.vgtu.reservation.review.type.EntityType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ReviewDataIntegrityTests {

    private ReviewDataIntegrity reviewDataIntegrity;

    @BeforeEach
    void setUp() {
        reviewDataIntegrity = new ReviewDataIntegrity();
    }

    @Test
    void validateSubmitDto_shouldThrow_whenDtoIsNull() {
        DtoValidationException ex = assertThrows(DtoValidationException.class,
                () -> reviewDataIntegrity.validateSubmitDto(null));

        assertEquals("Review DTO cannot be null", ex.getMessage());
    }

    @Test
    void validateSubmitDto_shouldThrow_whenEntityIdIsNull() {
        SubmitReviewRequestDto dto = SubmitReviewRequestDto.builder()
                .content("Nice room")
                .entityType(EntityType.ROOM)
                .build();

        DtoValidationException ex = assertThrows(DtoValidationException.class,
                () -> reviewDataIntegrity.validateSubmitDto(dto));

        assertEquals("Reviewed entity ID is required", ex.getMessage());
    }

    @Test
    void validateSubmitDto_shouldThrow_whenEntityTypeIsNull() {
        SubmitReviewRequestDto dto = SubmitReviewRequestDto.builder()
                .content("Nice room")
                .reviewedEntityId(UUID.randomUUID())
                .build();

        DtoValidationException ex = assertThrows(DtoValidationException.class,
                () -> reviewDataIntegrity.validateSubmitDto(dto));

        assertEquals("Entity type (CAR, ROOM, EQUIPMENT) is required", ex.getMessage());
    }

    @Test
    void validateSubmitDto_shouldThrow_whenContentIsNull() {
        SubmitReviewRequestDto dto = SubmitReviewRequestDto.builder()
                .reviewedEntityId(UUID.randomUUID())
                .entityType(EntityType.EQUIPMENT)
                .build();

        DtoValidationException ex = assertThrows(DtoValidationException.class,
                () -> reviewDataIntegrity.validateSubmitDto(dto));

        assertEquals("Review content cannot be empty", ex.getMessage());
    }

    @Test
    void validateSubmitDto_shouldThrow_whenContentIsBlank() {
        SubmitReviewRequestDto dto = SubmitReviewRequestDto.builder()
                .content("   ")
                .reviewedEntityId(UUID.randomUUID())
                .entityType(EntityType.CAR)
                .build();

        DtoValidationException ex = assertThrows(DtoValidationException.class,
                () -> reviewDataIntegrity.validateSubmitDto(dto));

        assertEquals("Review content cannot be empty", ex.getMessage());
    }

    @Test
    void validateSubmitDto_shouldThrow_whenContentIsTooLong() {
        String longContent = "a".repeat(2001);

        SubmitReviewRequestDto dto = SubmitReviewRequestDto.builder()
                .content(longContent)
                .reviewedEntityId(UUID.randomUUID())
                .entityType(EntityType.ROOM)
                .build();

        DtoValidationException ex = assertThrows(DtoValidationException.class,
                () -> reviewDataIntegrity.validateSubmitDto(dto));

        assertEquals("Review content must not exceed 2000 characters", ex.getMessage());
    }

    @Test
    void validateSubmitDto_shouldPass_whenValidDto() {
        SubmitReviewRequestDto dto = SubmitReviewRequestDto.builder()
                .content("Everything worked smoothly!")
                .reviewedEntityId(UUID.randomUUID())
                .entityType(EntityType.ROOM)
                .build();

        assertDoesNotThrow(() -> reviewDataIntegrity.validateSubmitDto(dto));
    }

}
