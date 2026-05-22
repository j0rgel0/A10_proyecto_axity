package com.parque.dinosaurios.domain.model;

import com.parque.dinosaurios.domain.enums.ExpenseType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OperationalExpense(
        Long id,
        ExpenseType type,
        BigDecimal amount,
        String description,
        int simulationStep,
        LocalDateTime createdAt) {
}
