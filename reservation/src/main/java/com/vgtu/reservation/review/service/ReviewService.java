package com.vgtu.reservation.review.service;

import com.vgtu.reservation.auth.service.authentication.AuthenticationService;
import com.vgtu.reservation.review.dao.ReviewDao;
import com.vgtu.reservation.review.dto.ReviewResponseDto;
import com.vgtu.reservation.review.dto.SubmitReviewRequestDto;
import com.vgtu.reservation.review.entity.Review;
import com.vgtu.reservation.review.integrity.ReviewDataIntegrity;
import com.vgtu.reservation.review.mapper.ReviewMapper;
import com.vgtu.reservation.review.type.EntityType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReviewService {

    private final ReviewDao reviewDao;
    private final ReviewMapper reviewMapper;
    private final ReviewDataIntegrity reviewDataIntegrity;
    private final AuthenticationService authenticationService;

    public List<ReviewResponseDto> getEntityReviews(UUID reviewedEntityId, EntityType entityType) {
        List<Review> reviews = reviewDao.findByReviewedEntityIdAndType(reviewedEntityId, entityType);

        return reviews.stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }

    public ReviewResponseDto createReview (SubmitReviewRequestDto dto) {
        reviewDataIntegrity.validateSubmitDto(dto);
        var user = authenticationService.getAuthenticatedUser();

        var review = reviewMapper.toEntity(dto, user);
        return reviewMapper.toDto(reviewDao.saveReview(review));
    }
}
