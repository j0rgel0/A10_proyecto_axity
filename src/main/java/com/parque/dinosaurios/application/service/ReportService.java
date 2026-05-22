package com.parque.dinosaurios.application.service;

import com.parque.dinosaurios.application.dto.ParkReport;
import com.parque.dinosaurios.domain.enums.EventStatus;
import com.parque.dinosaurios.domain.model.ParkEvent;
import com.parque.dinosaurios.domain.model.ParkState;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    private final RevenueService revenueService;
    private final ExpenseService expenseService;
    private final EventService eventService;

    public ReportService(RevenueService revenueService, ExpenseService expenseService, EventService eventService) {
        this.revenueService = revenueService;
        this.expenseService = expenseService;
        this.eventService = eventService;
    }

    public ParkReport build(ParkState state) {
        BigDecimal totalRevenues = revenueService.totalAmount();
        BigDecimal totalExpenses = expenseService.totalAmount();
        List<ParkEvent> events = eventService.findAll();
        long activeEvents = events.stream().filter(event -> event.status() == EventStatus.ACTIVE).count();
        long resolvedEvents = events.stream().filter(event -> event.status() == EventStatus.RESOLVED).count();
        return new ParkReport(
                totalRevenues,
                totalExpenses,
                totalRevenues.subtract(totalExpenses),
                revenueService.totalsByType(),
                expenseService.totalsByType(),
                eventService.countByType(),
                activeEvents,
                resolvedEvents,
                state == null ? 0.0 : state.averageSatisfaction(),
                state == null ? 0 : state.getProcessedTourists(),
                state == null ? 0 : state.getEnergyAvailable(),
                state == null ? 0 : state.countVehiclesInUse());
    }
}
