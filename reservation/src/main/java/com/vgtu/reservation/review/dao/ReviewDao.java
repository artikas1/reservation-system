package com.vgtu.reservation.review.dao;

import com.vgtu.reservation.review.entity.Review;
import com.vgtu.reservation.review.repository.ReviewRepository;
import com.vgtu.reservation.review.type.EntityType;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ReviewDao {

    private final ReviewRepository reviewRepository;

    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    public Review findById(UUID reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
    }

    public void deleteReview(UUID reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    public List<Review> findByReviewedEntityIdAndType(UUID entityId, EntityType entityType) {
        return reviewRepository.findByReviewedEntityIdAndEntityType(entityId, entityType);
    }

}
