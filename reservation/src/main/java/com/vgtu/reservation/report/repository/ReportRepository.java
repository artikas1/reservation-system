package com.vgtu.reservation.report.repository;

import com.vgtu.reservation.common.type.EntityType;
import com.vgtu.reservation.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, UUID> {
    List<Report> findByReportedEntityIdAndEntityType(UUID reportedEntityId, EntityType entityType);
}
