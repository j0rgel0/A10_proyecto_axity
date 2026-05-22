package com.parque.dinosaurios.infrastructure.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.parque.dinosaurios.application.dto.MonitoringSnapshot;
import com.parque.dinosaurios.application.dto.ParkReport;
import com.parque.dinosaurios.application.dto.SimulationResult;
import com.parque.dinosaurios.application.service.EventService;
import com.parque.dinosaurios.application.service.ExpenseService;
import com.parque.dinosaurios.application.service.ReportService;
import com.parque.dinosaurios.application.service.RevenueService;
import com.parque.dinosaurios.application.service.SimulationService;
import com.parque.dinosaurios.domain.enums.EventStatus;
import com.parque.dinosaurios.domain.enums.EventType;
import com.parque.dinosaurios.domain.enums.ExpenseType;
import com.parque.dinosaurios.domain.enums.RevenueType;
import com.parque.dinosaurios.domain.model.OperationalExpense;
import com.parque.dinosaurios.domain.model.ParkEvent;
import com.parque.dinosaurios.domain.model.Revenue;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

    @Mock
    private RevenueService revenueService;

    @Mock
    private ExpenseService expenseService;

    @Mock
    private EventService eventService;

    @Mock
    private ReportService reportService;

    @Mock
    private SimulationService simulationService;

    @Test
    void summaryReturnsLastSimulationReportWhenAvailable() {
        SimulationResult result = result();
        when(simulationService.lastResult()).thenReturn(Optional.of(result));
        ReportController controller = controller();

        var summary = controller.summary();

        assertThat(summary).isSameAs(result.report());
        verify(reportService, never()).build(null);
    }

    @Test
    void summaryBuildsFallbackReportWhenSimulationHasNotRun() {
        ParkReport fallback = report(new BigDecimal("25.00"));
        when(simulationService.lastResult()).thenReturn(Optional.empty());
        when(reportService.build(null)).thenReturn(fallback);
        ReportController controller = controller();

        var summary = controller.summary();

        assertThat(summary).isSameAs(fallback);
        verify(reportService).build(null);
    }

    @Test
    void listEndpointsDelegateToApplicationServices() {
        Revenue revenue = new Revenue(null, RevenueType.TICKET, new BigDecimal("80.00"), "boleto", 1, 1, LocalDateTime.now());
        OperationalExpense expense =
                new OperationalExpense(null, ExpenseType.ENERGY, new BigDecimal("30.00"), "energia", 1, LocalDateTime.now());
        ParkEvent event = new ParkEvent(
                null,
                EventType.BLACKOUT,
                EventStatus.ACTIVE,
                "apagon",
                4,
                -100,
                -10,
                1,
                LocalDateTime.now(),
                null);
        when(revenueService.findAll()).thenReturn(List.of(revenue));
        when(expenseService.findAll()).thenReturn(List.of(expense));
        when(eventService.findAll()).thenReturn(List.of(event));
        ReportController controller = controller();

        assertThat(controller.revenues()).containsExactly(revenue);
        assertThat(controller.expenses()).containsExactly(expense);
        assertThat(controller.events()).containsExactly(event);
    }

    private ReportController controller() {
        return new ReportController(revenueService, expenseService, eventService, reportService, simulationService);
    }

    private SimulationResult result() {
        return new SimulationResult(
                new MonitoringSnapshot(1, 2, 2, 3, 200, 0, 0, 80.0),
                report(new BigDecimal("100.00")),
                List.of());
    }

    private ParkReport report(BigDecimal totalRevenues) {
        return new ParkReport(
                totalRevenues,
                BigDecimal.ZERO,
                totalRevenues,
                Map.of(),
                Map.of(),
                Map.of(),
                0,
                0,
                80.0,
                2,
                200,
                0);
    }
}
