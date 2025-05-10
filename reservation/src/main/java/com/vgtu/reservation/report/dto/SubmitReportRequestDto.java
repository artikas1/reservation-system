package com.vgtu.reservation.report.dto;

import com.vgtu.reservation.common.type.EntityType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubmitReportRequestDto {
    
    private String content;
    
    @NotNull(message = "Reported entity ID is required")
    private UUID reportedEntityId;

    @NotNull(message = "Entity type is required")
    private EntityType entityType;
    
}
