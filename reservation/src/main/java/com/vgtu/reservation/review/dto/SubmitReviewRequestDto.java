package com.vgtu.reservation.review.dto;

import com.vgtu.reservation.review.type.EntityType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubmitReviewRequestDto {

    private String content;

    @NotNull(message = "Entity ID is required")
    private UUID reviewedEntityId;

    @NotNull(message = "Entity type is required")
    private EntityType entityType;

    private boolean anonymous; // defaults to false if not provided

}
