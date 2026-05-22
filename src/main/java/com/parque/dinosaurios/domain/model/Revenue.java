package com.parque.dinosaurios.domain.model;

import com.parque.dinosaurios.domain.enums.RevenueType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Revenue(
        Long id,
        RevenueType type,
        BigDecimal amount,
        String description,
        int simulationStep,
        int touristCount,
        LocalDateTime createdAt) {
}
