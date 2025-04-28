package com.vgtu.reservation.statistics.controller;

import com.vgtu.reservation.statistics.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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

}
