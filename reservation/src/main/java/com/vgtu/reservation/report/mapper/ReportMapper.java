package com.vgtu.reservation.report.mapper;

import com.vgtu.reservation.report.dto.ReportResponseDto;
import com.vgtu.reservation.report.dto.SubmitReportRequestDto;
import com.vgtu.reservation.report.entity.Report;
import com.vgtu.reservation.report.type.ReportStatus;
import com.vgtu.reservation.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ReportMapper {

    public Report toEntity(SubmitReportRequestDto dto, User user) {
        return Report.builder()
                .content(dto.getContent())
                .reportedEntityId(dto.getReportedEntityId())
                .entityType(dto.getEntityType())
                .reportStatus(ReportStatus.PENDING)
                .user(user)
                .build();
    }

    public ReportResponseDto toDto(Report report) {
        return ReportResponseDto.builder()
                .content(report.getContent())
                .createdAt(report.getCreatedAt())
                .entityType(report.getEntityType())
                .reportedEntityId(report.getReportedEntityId())
                .build();
    }

}
