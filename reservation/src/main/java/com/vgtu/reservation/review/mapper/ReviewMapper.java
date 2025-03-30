package com.vgtu.reservation.review.mapper;

import com.vgtu.reservation.review.dto.ReviewResponseDto;
import com.vgtu.reservation.review.dto.SubmitReviewRequestDto;
import com.vgtu.reservation.review.entity.Review;
import com.vgtu.reservation.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ReviewMapper {

    public Review toEntity(SubmitReviewRequestDto dto, User user) {
        return Review.builder()
                .content(dto.getContent())
                .reviewedEntityId(dto.getReviewedEntityId())
                .entityType(dto.getEntityType())
                .user(dto.isAnonymous() ? null : user)
                .build();
    }

    public ReviewResponseDto toDto(Review review) {
        return ReviewResponseDto.builder()
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
