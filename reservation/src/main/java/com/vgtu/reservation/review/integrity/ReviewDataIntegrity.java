package com.vgtu.reservation.review.integrity;

import com.vgtu.reservation.common.exception.exceptions.DtoValidationException;
import com.vgtu.reservation.review.dto.SubmitReviewRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReviewDataIntegrity {

    public void validateSubmitDto(SubmitReviewRequestDto dto) {
        if (dto == null) {
            throw new DtoValidationException("Review DTO cannot be null");
        }
        if (dto.getReviewedEntityId() == null) {
            throw new DtoValidationException("Reviewed entity ID is required");
        }
        if (dto.getEntityType() == null) {
            throw new DtoValidationException("Entity type (CAR, ROOM, EQUIPMENT) is required");
        }
        validateContent(dto.getContent());
    }

    private void validateContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new DtoValidationException("Review content cannot be empty");
        }
        if (content.length() > 2000) {
            throw new DtoValidationException("Review content must not exceed 2000 characters");
        }
    }

}
