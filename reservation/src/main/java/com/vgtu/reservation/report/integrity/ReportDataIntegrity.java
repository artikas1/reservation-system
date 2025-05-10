package com.vgtu.reservation.report.integrity;

import com.vgtu.reservation.common.exception.DtoValidationException;
import com.vgtu.reservation.report.dto.SubmitReportRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReportDataIntegrity {

    public void validateSubmitDto(SubmitReportRequestDto dto) {
        if (dto == null) {
            throw new DtoValidationException("Report DTO cannot be null");
        }
        if (dto.getReportedEntityId() == null) {
            throw new DtoValidationException("Reported entity ID is required");
        }
        if (dto.getEntityType() == null) {
            throw new DtoValidationException("Entity type (CAR, ROOM, EQUIPMENT) is required");
        }
        validateContent(dto.getContent());
    }

    private void validateContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new DtoValidationException("Report content cannot be empty");
        }
        if (content.length() > 2000) {
            throw new DtoValidationException("Report content must not exceed 2000 characters");
        }
    }

}
