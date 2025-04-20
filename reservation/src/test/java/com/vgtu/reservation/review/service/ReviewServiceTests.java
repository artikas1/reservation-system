package com.vgtu.reservation.review.service;

import com.vgtu.reservation.auth.service.authentication.AuthenticationService;
import com.vgtu.reservation.review.dao.ReviewDao;
import com.vgtu.reservation.review.dto.ReviewResponseDto;
import com.vgtu.reservation.review.dto.SubmitReviewRequestDto;
import com.vgtu.reservation.review.entity.Review;
import com.vgtu.reservation.review.integrity.ReviewDataIntegrity;
import com.vgtu.reservation.review.mapper.ReviewMapper;
import com.vgtu.reservation.review.type.EntityType;
import com.vgtu.reservation.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTests {

    @Mock
    private ReviewDao reviewDao;

    @Mock
    private ReviewMapper reviewMapper;

    @Mock
    private ReviewDataIntegrity reviewDataIntegrity;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private ReviewService reviewService;

    private User testUser;
    private SubmitReviewRequestDto reviewRequestDto;
    private Review testReview;
    private ReviewResponseDto reviewResponseDto;

    @BeforeEach
    void setUp() {
        testUser = User.builder().id(UUID.randomUUID()).build();

        reviewRequestDto = SubmitReviewRequestDto.builder()
                .reviewedEntityId(UUID.randomUUID())
                .entityType(EntityType.ROOM)
                .content("Patalpa buvo švari ir gerai vėdinama. Baldai patogūs, tačiau trūko papildomo apšvietimo.")
                .build();

        testReview = Review.builder()
                .id(UUID.randomUUID())
                .user(testUser)
                .reviewedEntityId(reviewRequestDto.getReviewedEntityId())
                .entityType(reviewRequestDto.getEntityType())
                .content(reviewRequestDto.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        reviewResponseDto = ReviewResponseDto.builder()
                .content(testReview.getContent())
                .createdAt(testReview.getCreatedAt())
                .build();
    }

    @Test
    void createReview_shouldReturnResponseDto_whenValidRequest() {
        when(authenticationService.getAuthenticatedUser()).thenReturn(testUser);
        when(reviewMapper.toEntity(reviewRequestDto, testUser)).thenReturn(testReview);
        when(reviewDao.saveReview(testReview)).thenReturn(testReview);
        when(reviewMapper.toDto(testReview)).thenReturn(reviewResponseDto);

        ReviewResponseDto result = reviewService.createReview(reviewRequestDto);

        assertNotNull(result);
        assertEquals(reviewRequestDto.getContent(), result.getContent());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    void getEntityReviews_shouldReturnMappedDtos() {
        UUID entityId = UUID.randomUUID();
        List<Review> reviews = List.of(testReview);

        when(reviewDao.findByReviewedEntityIdAndType(entityId, EntityType.ROOM)).thenReturn(reviews);
        when(reviewMapper.toDto(testReview)).thenReturn(reviewResponseDto);

        List<ReviewResponseDto> result = reviewService.getEntityReviews(entityId, EntityType.ROOM);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testReview.getContent(), result.get(0).getContent());
    }
}
