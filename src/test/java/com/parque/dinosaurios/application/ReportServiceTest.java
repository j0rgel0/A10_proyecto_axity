package com.parque.dinosaurios.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.parque.dinosaurios.application.service.EventService;
import com.parque.dinosaurios.application.service.ExpenseService;
import com.parque.dinosaurios.application.service.ReportService;
import com.parque.dinosaurios.application.service.RevenueService;
import com.parque.dinosaurios.domain.enums.EventStatus;
import com.parque.dinosaurios.domain.enums.EventType;
import com.parque.dinosaurios.domain.enums.ExpenseType;
import com.parque.dinosaurios.domain.enums.RevenueType;
import com.parque.dinosaurios.domain.model.ParkEvent;
import com.parque.dinosaurios.support.ParkStateFixtures;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private RevenueService revenueService;

    @Mock
    private ExpenseService expenseService;

    @Mock
    private EventService eventService;

    @Test
    void buildCombinesFinanceEventsAndOperationalState() {
        when(revenueService.totalAmount()).thenReturn(new BigDecimal("250.00"));
        when(expenseService.totalAmount()).thenReturn(new BigDecimal("80.00"));
        when(revenueService.totalsByType()).thenReturn(Map.of(RevenueType.TICKET, new BigDecimal("120.00")));
        when(expenseService.totalsByType()).thenReturn(Map.of(ExpenseType.ENERGY, new BigDecimal("80.00")));
        when(eventService.findAll()).thenReturn(java.util.List.of(
                event(EventType.BLACKOUT, EventStatus.ACTIVE),
                event(EventType.OFFER_HOUR, EventStatus.RESOLVED)));
        when(eventService.countByType()).thenReturn(Map.of(
                EventType.BLACKOUT, 1L,
                EventType.OFFER_HOUR, 1L));
        var state = ParkStateFixtures.parkStateWithTourists();
        state.getVehicles().get(0).board(1);
        ReportService service = new ReportService(revenueService, expenseService, eventService);

        var report = service.build(state);

        assertThat(report.totalRevenues()).isEqualByComparingTo("250.00");
        assertThat(report.totalExpenses()).isEqualByComparingTo("80.00");
        assertThat(report.netProfit()).isEqualByComparingTo("170.00");
        assertThat(report.activeEvents()).isEqualTo(1);
        assertThat(report.resolvedEvents()).isEqualTo(1);
        assertThat(report.processedVisitors()).isEqualTo(2);
        assertThat(report.finalEnergy()).isEqualTo(500);
        assertThat(report.vehiclesInUse()).isEqualTo(1);
        assertThat(report.averageSatisfaction()).isEqualTo(75.0);
        assertThat(report.eventsByType()).containsEntry(EventType.BLACKOUT, 1L);
    }

    @Test
    void buildWithoutStateReturnsFinancialSummaryWithOperationalZeros() {
        when(revenueService.totalAmount()).thenReturn(new BigDecimal("40.00"));
        when(expenseService.totalAmount()).thenReturn(new BigDecimal("15.00"));
        when(revenueService.totalsByType()).thenReturn(Map.of());
        when(expenseService.totalsByType()).thenReturn(Map.of());
        when(eventService.findAll()).thenReturn(java.util.List.of());
        when(eventService.countByType()).thenReturn(Map.of());
        ReportService service = new ReportService(revenueService, expenseService, eventService);

        var report = service.build(null);

        assertThat(report.netProfit()).isEqualByComparingTo("25.00");
        assertThat(report.averageSatisfaction()).isZero();
        assertThat(report.processedVisitors()).isZero();
        assertThat(report.finalEnergy()).isZero();
        assertThat(report.vehiclesInUse()).isZero();
    }

    private ParkEvent event(EventType type, EventStatus status) {
        return new ParkEvent(
                null,
                type,
                status,
                "Evento de prueba",
                1,
                0,
                0,
                1,
                LocalDateTime.now(),
                status == EventStatus.RESOLVED ? LocalDateTime.now() : null);
    }
}
