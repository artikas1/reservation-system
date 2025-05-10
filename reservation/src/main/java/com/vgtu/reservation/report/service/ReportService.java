package com.vgtu.reservation.report.service;

import com.vgtu.reservation.auth.service.authentication.AuthenticationService;
import com.vgtu.reservation.common.type.EntityType;
import com.vgtu.reservation.report.dao.ReportDao;
import com.vgtu.reservation.report.dto.ReportResponseDto;
import com.vgtu.reservation.report.dto.SubmitReportRequestDto;
import com.vgtu.reservation.report.entity.Report;
import com.vgtu.reservation.report.integrity.ReportDataIntegrity;
import com.vgtu.reservation.report.mapper.ReportMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportService {

    private final ReportDao reportDao;
    private final ReportMapper reportMapper;
    private final ReportDataIntegrity reportDataIntegrity;
    private final AuthenticationService authenticationService;

    public List<ReportResponseDto> getEntityReports(UUID reportedEntityId, EntityType entityType) {
        var user = authenticationService.getAuthenticatedUser();

        if (!user.isAdmin()) {
            throw new AccessDeniedException("Only admins can view entity reports.");
        }

        List<Report> reports = reportDao.findByReportedEntityIdAndType(reportedEntityId, entityType);

        return reports.stream()
                .map(reportMapper::toDto)
                .collect(Collectors.toList());
    }

    public ReportResponseDto createReport(SubmitReportRequestDto dto) {
        reportDataIntegrity.validateSubmitDto(dto);
        var user = authenticationService.getAuthenticatedUser();

        var report = reportMapper.toEntity(dto, user);
        return reportMapper.toDto(reportDao.saveReport(report));

    }
}
