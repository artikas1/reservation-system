package com.vgtu.reservation.report.dao;

import com.vgtu.reservation.common.type.EntityType;
import com.vgtu.reservation.report.entity.Report;
import com.vgtu.reservation.report.repository.ReportRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ReportDao {

    private final ReportRepository reportRepository;

    public Report saveReport(Report report) {
        return reportRepository.save(report);
    }

    public List<Report> findByReportedEntityIdAndType(UUID entityId, EntityType entityType) {
      return reportRepository.findByReportedEntityIdAndEntityType(entityId, entityType);
    }
}
