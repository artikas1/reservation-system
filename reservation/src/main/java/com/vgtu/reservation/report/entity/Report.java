package com.vgtu.reservation.report.entity;

import com.vgtu.reservation.common.type.EntityType;
import com.vgtu.reservation.report.type.ReportStatus;
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
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String content;

    @Column(nullable = false)
    private UUID reportedEntityId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EntityType entityType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus reportStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}

