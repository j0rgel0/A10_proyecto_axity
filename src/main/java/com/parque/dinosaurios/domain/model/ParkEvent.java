package com.parque.dinosaurios.domain.model;

import com.parque.dinosaurios.domain.enums.EventStatus;
import com.parque.dinosaurios.domain.enums.EventType;
import java.time.LocalDateTime;

public record ParkEvent(
        Long id,
        EventType type,
        EventStatus status,
        String description,
        int severity,
        int energyDelta,
        int satisfactionDelta,
        int simulationStep,
        LocalDateTime createdAt,
        LocalDateTime resolvedAt) {
}
