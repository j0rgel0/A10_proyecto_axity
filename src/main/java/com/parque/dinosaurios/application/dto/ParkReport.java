package com.parque.dinosaurios.application.dto;

import com.parque.dinosaurios.domain.enums.EventType;
import com.parque.dinosaurios.domain.enums.ExpenseType;
import com.parque.dinosaurios.domain.enums.RevenueType;
import java.math.BigDecimal;
import java.util.Map;

public record ParkReport(
        BigDecimal totalRevenues,
        BigDecimal totalExpenses,
        BigDecimal netProfit,
        Map<RevenueType, BigDecimal> revenuesByType,
        Map<ExpenseType, BigDecimal> expensesByType,
        Map<EventType, Long> eventsByType,
        long activeEvents,
        long resolvedEvents,
        double averageSatisfaction,
        int processedVisitors,
        int finalEnergy,
        long vehiclesInUse) {
}
