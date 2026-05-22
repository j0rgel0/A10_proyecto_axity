package com.parque.dinosaurios.infrastructure.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.parque.dinosaurios.application.dto.MonitoringSnapshot;
import com.parque.dinosaurios.application.dto.ParkReport;
import com.parque.dinosaurios.application.dto.SimulationRequest;
import com.parque.dinosaurios.application.dto.SimulationResult;
import com.parque.dinosaurios.application.service.SimulationService;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SimulationControllerTest {

    @Mock
    private SimulationService simulationService;

    @Test
    void runDelegatesRequestToSimulationService() {
        SimulationRequest request = new SimulationRequest(4);
        SimulationResult expected = result(4);
        when(simulationService.run(request)).thenReturn(expected);
        SimulationController controller = new SimulationController(simulationService);

        var actual = controller.run(request);

        assertThat(actual).isSameAs(expected);
        verify(simulationService).run(request);
    }

    @Test
    void statusReturnsCurrentMonitoringSnapshot() {
        MonitoringSnapshot expected = new MonitoringSnapshot(3, 5, 12, 3, 180, 1, 0, 76.0);
        when(simulationService.status()).thenReturn(expected);
        SimulationController controller = new SimulationController(simulationService);

        var actual = controller.status();

        assertThat(actual).isSameAs(expected);
        verify(simulationService).status();
    }

    private SimulationResult result(int step) {
        return new SimulationResult(
                new MonitoringSnapshot(step, 2, 2, 1, 100, 0, 0, 70.0),
                new ParkReport(
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        Map.of(),
                        Map.of(),
                        Map.of(),
                        0,
                        0,
                        70.0,
                        2,
                        100,
                        0),
                java.util.List.of());
    }
}
