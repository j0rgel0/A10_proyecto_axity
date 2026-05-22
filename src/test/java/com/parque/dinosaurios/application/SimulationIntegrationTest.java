package com.parque.dinosaurios.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.parque.dinosaurios.application.dto.SimulationRequest;
import com.parque.dinosaurios.application.service.EventService;
import com.parque.dinosaurios.application.service.ExpenseService;
import com.parque.dinosaurios.application.service.RevenueService;
import com.parque.dinosaurios.application.service.SimulationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SimulationIntegrationTest {

    @Autowired
    private SimulationService simulationService;

    @Autowired
    private RevenueService revenueService;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private EventService eventService;

    @Test
    void runSimulationPersistsRevenuesExpensesAndEvents() {
        var result = simulationService.run(new SimulationRequest(2));

        assertThat(result.snapshot().processedTourists()).isGreaterThan(0);
        assertThat(revenueService.findAll()).isNotEmpty();
        assertThat(expenseService.findAll()).isNotEmpty();
        assertThat(eventService.findAll()).isNotEmpty();
        assertThat(result.report().totalRevenues()).isPositive();
    }
}
