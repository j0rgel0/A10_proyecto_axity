package com.parque.dinosaurios.infrastructure.cli;

import com.parque.dinosaurios.application.dto.SimulationRequest;
import com.parque.dinosaurios.application.dto.SimulationResult;
import com.parque.dinosaurios.application.service.SimulationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("cli")
public class SimulationRunner implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationRunner.class);

    private final SimulationService simulationService;

    public SimulationRunner(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @Override
    public void run(String... args) {
        SimulationResult result = simulationService.run(new SimulationRequest(null));
        LOGGER.info("Simulacion finalizada: {}", result.snapshot());
        LOGGER.info("Reporte financiero: {}", result.report());
    }
}
