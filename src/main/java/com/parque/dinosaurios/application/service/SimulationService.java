package com.parque.dinosaurios.application.service;

import com.parque.dinosaurios.application.dto.MonitoringSnapshot;
import com.parque.dinosaurios.application.dto.SimulationRequest;
import com.parque.dinosaurios.application.dto.SimulationResult;
import com.parque.dinosaurios.simulation.SimulationEngine;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class SimulationService {

    private final SimulationEngine simulationEngine;
    private final MonitoringService monitoringService;
    private volatile SimulationResult lastResult;

    public SimulationService(SimulationEngine simulationEngine, MonitoringService monitoringService) {
        this.simulationEngine = simulationEngine;
        this.monitoringService = monitoringService;
    }

    public SimulationResult run(SimulationRequest request) {
        int steps = request == null || request.steps() == null ? 0 : request.steps();
        lastResult = simulationEngine.run(steps);
        return lastResult;
    }

    public MonitoringSnapshot status() {
        return monitoringService.currentSnapshot();
    }

    public Optional<SimulationResult> lastResult() {
        return Optional.ofNullable(lastResult);
    }
}
