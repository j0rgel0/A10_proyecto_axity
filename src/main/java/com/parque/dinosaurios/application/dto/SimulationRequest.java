package com.parque.dinosaurios.application.dto;

import jakarta.validation.constraints.Min;

public record SimulationRequest(@Min(1) Integer steps) {
}
