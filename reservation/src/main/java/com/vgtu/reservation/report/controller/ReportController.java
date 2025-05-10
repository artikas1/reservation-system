package com.vgtu.reservation.report.controller;

import com.vgtu.reservation.common.type.EntityType;
import com.vgtu.reservation.report.dto.ReportResponseDto;
import com.vgtu.reservation.report.dto.SubmitReportRequestDto;
import com.vgtu.reservation.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Used to define endpoints of the reports.
 */
@RequestMapping("/report")
@RestController
@AllArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "Get reports for an entity", description = "Retrieves all reports for a car, room or equipment by ID and type")
    @GetMapping("/get")
    public ResponseEntity<List<ReportResponseDto>> getReportsForEntity(
            @Parameter(description = "ID of the entity (car, room or equipment)") @RequestParam UUID reportedEntityId,
            @Parameter(description = "Type of the entity (CAR, ROOM, EQUIPMENT)") @RequestParam EntityType entityType) {
        return ResponseEntity.ok(reportService.getEntityReports(reportedEntityId, entityType));
    }

    @Operation(summary = "Create a report", description = "Creates a report for a selected entity (car, room or equipment)")
    @PostMapping("/create")
    public ResponseEntity<ReportResponseDto> createReport(
            @Parameter(description = "Request body containing review details") @Valid @RequestBody SubmitReportRequestDto submitReportRequestDto) {
        return ResponseEntity.ok(reportService.createReport(submitReportRequestDto));
    }
}
