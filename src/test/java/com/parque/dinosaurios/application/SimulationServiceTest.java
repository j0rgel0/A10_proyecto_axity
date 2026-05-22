package com.parque.dinosaurios.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.parque.dinosaurios.application.dto.MonitoringSnapshot;
import com.parque.dinosaurios.application.dto.ParkReport;
import com.parque.dinosaurios.application.dto.SimulationRequest;
import com.parque.dinosaurios.application.dto.SimulationResult;
import com.parque.dinosaurios.application.service.MonitoringService;
import com.parque.dinosaurios.application.service.SimulationService;
import com.parque.dinosaurios.simulation.SimulationEngine;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SimulationServiceTest {

    @Mock
    private SimulationEngine simulationEngine;

    @Mock
    private MonitoringService monitoringService;

    @Test
    void runWithNullRequestUsesConfiguredDefaultAndStoresLastResult() {
        SimulationResult expected = result(3);
        when(simulationEngine.run(0)).thenReturn(expected);
        SimulationService service = new SimulationService(simulationEngine, monitoringService);

        var actual = service.run(null);

        assertThat(actual).isSameAs(expected);
        assertThat(service.lastResult()).contains(expected);
        verify(simulationEngine).run(0);
    }

    @Test
    void runWithRequestDelegatesRequestedSteps() {
        SimulationResult expected = result(5);
        when(simulationEngine.run(5)).thenReturn(expected);
        SimulationService service = new SimulationService(simulationEngine, monitoringService);

        var actual = service.run(new SimulationRequest(5));

        assertThat(actual).isSameAs(expected);
        verify(simulationEngine).run(5);
    }

    @Test
    void statusDelegatesToMonitoringSnapshot() {
        MonitoringSnapshot expected = new MonitoringSnapshot(2, 4, 8, 3, 250, 1, 0, 82.5);
        when(monitoringService.currentSnapshot()).thenReturn(expected);
        SimulationService service = new SimulationService(simulationEngine, monitoringService);

        var snapshot = service.status();

        assertThat(snapshot).isSameAs(expected);
        verify(monitoringService).currentSnapshot();
    }

    private SimulationResult result(int step) {
        return new SimulationResult(
                new MonitoringSnapshot(step, 1, 1, 1, 100, 0, 0, 75.0),
                new ParkReport(
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        Map.of(),
                        Map.of(),
                        Map.of(),
                        0,
                        0,
                        75.0,
                        1,
                        100,
                        0),
                java.util.List.of());
    }
}
