package com.vgtu.reservation.statistics.controller;

import com.vgtu.reservation.statistics.dto.MonthUsageDto;
import com.vgtu.reservation.statistics.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Used to define endpoints for statistics aggregation
 */
@RequestMapping("/statistics")
@RestController
@AllArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @Operation(summary = "Get all reservations count by type", description = "Returns the number of car, equipment, and room reservations")
    @GetMapping("/reservations-count")
    public ResponseEntity<Map<String, Long>> getReservationsCount() {
        return ResponseEntity.ok(statisticsService.getReservationsCount());
    }

    @Operation(summary = "Get all resources count by type", description = "Returns the number of cars, equipments, and rooms available")
    @GetMapping("/resources-count")
    public ResponseEntity<Map<String, Long>> getResourcesCount() {
        return ResponseEntity.ok(statisticsService.getResourcesCount());
    }

    @Operation(summary = "Get total reservation hours for a resource", description = "Returns total reserved hours for a given resource in a specific month")
    @GetMapping("/resource/{resourceId}/reservation-hours")
    public ResponseEntity<Long> getResourceReservationHours(
            @PathVariable UUID resourceId,
            @RequestParam("year") int year,
            @RequestParam("month") int month) {
        return ResponseEntity.ok(statisticsService.getResourceReservationHours(resourceId, year, month));
    }

    @Operation(summary = "Get monthly reservation hours for a resource", description = "Returns how many hours a resource was reserved each month in a given year")
    @GetMapping("/resource-usage-history")
    public ResponseEntity<List<MonthUsageDto>> getResourceUsageHistory(
            @RequestParam UUID resourceId,
            @RequestParam int year
    ) {
        return ResponseEntity.ok(statisticsService.getResourceUsageHistory(resourceId, year));
    }

}
