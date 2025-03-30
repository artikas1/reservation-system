package com.vgtu.reservation.review.entity;

import com.vgtu.reservation.review.type.EntityType;
import com.vgtu.reservation.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String content;

    @Column(nullable = false)
    private UUID reviewedEntityId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EntityType entityType;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
