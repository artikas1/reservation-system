package com.vgtu.reservation.review.dao;

import com.vgtu.reservation.review.entity.Review;
import com.vgtu.reservation.review.repository.ReviewRepository;
import com.vgtu.reservation.review.type.EntityType;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewDaoTests {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewDao reviewDao;

    private UUID reviewId;
    private UUID entityId;
    private Review review;

    @BeforeEach
    void setUp() {
        reviewId = UUID.randomUUID();
        entityId = UUID.randomUUID();

        review = Review.builder()
                .id(reviewId)
                .content("Neblogas kambarys")
                .entityType(EntityType.ROOM)
                .reviewedEntityId(entityId)
                .build();
    }

    @Test
    void saveReview_shouldReturnSavedReview() {
        when(reviewRepository.save(review)).thenReturn(review);

        Review saved = reviewDao.saveReview(review);

        assertEquals(review, saved);
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    void findById_shouldReturnReview_whenFound() {
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        Review found = reviewDao.findById(reviewId);

        assertEquals(review, found);
        verify(reviewRepository, times(1)).findById(reviewId);
    }

    @Test
    void findById_shouldThrow_whenNotFound() {
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> reviewDao.findById(reviewId));
        verify(reviewRepository, times(1)).findById(reviewId);
    }

    @Test
    void deleteReview_shouldCallRepositoryDelete() {
        reviewDao.deleteReview(reviewId);

        verify(reviewRepository, times(1)).deleteById(reviewId);
    }

    @Test
    void findByReviewedEntityIdAndType_shouldReturnList() {
        List<Review> reviews = List.of(review);
        when(reviewRepository.findByReviewedEntityIdAndEntityType(entityId, EntityType.ROOM)).thenReturn(reviews);

        List<Review> result = reviewDao.findByReviewedEntityIdAndType(entityId, EntityType.ROOM);

        assertEquals(reviews, result);
        verify(reviewRepository, times(1)).findByReviewedEntityIdAndEntityType(entityId, EntityType.ROOM);
    }
}
