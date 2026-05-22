package com.parque.dinosaurios.infrastructure.web;

import com.parque.dinosaurios.application.dto.ParkReport;
import com.parque.dinosaurios.application.service.ExpenseService;
import com.parque.dinosaurios.application.service.ReportService;
import com.parque.dinosaurios.application.service.RevenueService;
import com.parque.dinosaurios.application.service.SimulationService;
import com.parque.dinosaurios.domain.model.OperationalExpense;
import com.parque.dinosaurios.domain.model.ParkEvent;
import com.parque.dinosaurios.domain.model.Revenue;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final RevenueService revenueService;
    private final ExpenseService expenseService;
    private final ReportService reportService;
    private final SimulationService simulationService;

    public ReportController(
            RevenueService revenueService,
            ExpenseService expenseService,
            ReportService reportService,
            SimulationService simulationService) {
        this.revenueService = revenueService;
        this.expenseService = expenseService;
        this.reportService = reportService;
        this.simulationService = simulationService;
    }

    @GetMapping("/revenues")
    public List<Revenue> revenues() {
        return revenueService.findAll();
    }

    @GetMapping("/expenses")
    public List<OperationalExpense> expenses() {
        return expenseService.findAll();
    }

    @GetMapping("/events")
    public List<ParkEvent> events() {
        return simulationService.lastResult()
                .map(result -> result.generatedEvents())
                .orElse(List.of());
    }

    @GetMapping("/summary")
    public ParkReport summary() {
        return simulationService.lastResult()
                .map(SimulationResultAccessor::report)
                .orElseGet(() -> reportService.build(null));
    }

    private static class SimulationResultAccessor {
        private static ParkReport report(com.parque.dinosaurios.application.dto.SimulationResult result) {
            return result.report();
        }
    }
}
