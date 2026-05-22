package com.parque.dinosaurios.infrastructure.web;

import com.parque.dinosaurios.application.dto.MonitoringSnapshot;
import com.parque.dinosaurios.application.dto.SimulationRequest;
import com.parque.dinosaurios.application.dto.SimulationResult;
import com.parque.dinosaurios.application.service.SimulationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/simulation")
public class SimulationController {

    private final SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping("/run")
    public SimulationResult run(@Valid @RequestBody(required = false) SimulationRequest request) {
        return simulationService.run(request);
    }

    @GetMapping("/status")
    public MonitoringSnapshot status() {
        return simulationService.status();
    }
}
