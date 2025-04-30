package com.vgtu.reservation.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthUsageDto {
    private String month;
    private Long hours;
}
