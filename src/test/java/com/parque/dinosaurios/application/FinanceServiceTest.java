package com.parque.dinosaurios.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.parque.dinosaurios.application.service.ExpenseService;
import com.parque.dinosaurios.application.service.RevenueService;
import com.parque.dinosaurios.application.service.TicketService;
import com.parque.dinosaurios.config.ParkProperties;
import com.parque.dinosaurios.domain.enums.ExpenseType;
import com.parque.dinosaurios.domain.enums.RevenueType;
import com.parque.dinosaurios.domain.enums.TouristType;
import com.parque.dinosaurios.domain.model.OperationalExpense;
import com.parque.dinosaurios.domain.model.Revenue;
import com.parque.dinosaurios.domain.model.Tourist;
import com.parque.dinosaurios.infrastructure.persistence.entity.OperationalExpenseEntity;
import com.parque.dinosaurios.infrastructure.persistence.entity.RevenueEntity;
import com.parque.dinosaurios.infrastructure.persistence.repository.OperationalExpenseRepository;
import com.parque.dinosaurios.infrastructure.persistence.repository.RevenueRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FinanceServiceTest {

    @Mock
    private RevenueRepository revenueRepository;

    @Mock
    private OperationalExpenseRepository expenseRepository;

    @Test
    void revenueServiceRecordsAndTotalsRevenues() {
        RevenueService service = new RevenueService(revenueRepository);
        when(revenueRepository.save(any(RevenueEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(revenueRepository.findAll()).thenReturn(List.of(
                RevenueEntity.from(new Revenue(null, RevenueType.TICKET, new BigDecimal("80.00"), "boleto", 1, 1, LocalDateTime.now())),
                RevenueEntity.from(new Revenue(null, RevenueType.SOUVENIR, new BigDecimal("20.00"), "souvenir", 1, 1, LocalDateTime.now()))));

        Revenue saved = service.record(RevenueType.TICKET, new BigDecimal("80.00"), "boleto", 1, 1);

        assertThat(saved.amount()).isEqualByComparingTo("80.00");
        assertThat(service.totalAmount()).isEqualByComparingTo("100.00");
        assertThat(service.totalsByType().get(RevenueType.SOUVENIR)).isEqualByComparingTo("20.00");
        verify(revenueRepository).save(any(RevenueEntity.class));
    }

    @Test
    void expenseServiceRecordsAndTotalsExpenses() {
        ExpenseService service = new ExpenseService(expenseRepository);
        when(expenseRepository.save(any(OperationalExpenseEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(expenseRepository.findAll()).thenReturn(List.of(
                OperationalExpenseEntity.from(new OperationalExpense(null, ExpenseType.ENERGY, new BigDecimal("30.00"), "energia", 1, LocalDateTime.now())),
                OperationalExpenseEntity.from(new OperationalExpense(null, ExpenseType.REPAIR, new BigDecimal("45.00"), "reparacion", 1, LocalDateTime.now()))));

        OperationalExpense saved = service.record(ExpenseType.ENERGY, new BigDecimal("30.00"), "energia", 1);

        assertThat(saved.amount()).isEqualByComparingTo("30.00");
        assertThat(service.totalAmount()).isEqualByComparingTo("75.00");
        assertThat(service.totalsByType().get(ExpenseType.REPAIR)).isEqualByComparingTo("45.00");
        verify(expenseRepository).save(any(OperationalExpenseEntity.class));
    }

    @Test
    void ticketServiceAppliesChildDiscountAndMarksTicketPaid() {
        ParkProperties properties = new ParkProperties();
        properties.getTickets().setBasePrice(new BigDecimal("100.00"));
        properties.getTickets().setChildDiscount(new BigDecimal("0.50"));
        RevenueService revenueService = org.mockito.Mockito.mock(RevenueService.class);
        when(revenueService.record(any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(new Revenue(null, RevenueType.TICKET, new BigDecimal("50.00"), "boleto", 1, 1, LocalDateTime.now()));
        TicketService ticketService = new TicketService(properties, revenueService);
        Tourist tourist = new Tourist("T-1", TouristType.CHILD, 70);

        Revenue revenue = ticketService.sellTicket(tourist, 1);

        assertThat(revenue.amount()).isEqualByComparingTo("50.00");
        assertThat(ticketService.calculatePrice(TouristType.CHILD)).isEqualByComparingTo("50.00");
        assertThat(tourist.isTicketPaid()).isTrue();
    }
}
