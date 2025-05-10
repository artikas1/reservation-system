package com.vgtu.reservation.report.dto;

import com.vgtu.reservation.common.type.EntityType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponseDto {

    private String content;
    private LocalDateTime createdAt;
    private EntityType entityType;
    private UUID reportedEntityId;

}
