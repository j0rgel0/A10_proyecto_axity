package com.parque.dinosaurios.application.dto;

import com.parque.dinosaurios.domain.model.ParkEvent;
import java.util.List;

public record SimulationResult(
        MonitoringSnapshot snapshot,
        ParkReport report,
        List<ParkEvent> generatedEvents) {
}
