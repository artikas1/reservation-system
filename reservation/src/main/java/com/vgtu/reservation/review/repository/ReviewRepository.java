package com.vgtu.reservation.review.repository;

import com.vgtu.reservation.review.entity.Review;
import com.vgtu.reservation.review.type.EntityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findByReviewedEntityIdAndEntityType(UUID entityId, EntityType entityType);
}
