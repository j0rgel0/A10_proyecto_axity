package com.parque.dinosaurios.infrastructure.persistence.entity;

import com.parque.dinosaurios.domain.enums.ExpenseType;
import com.parque.dinosaurios.domain.model.OperationalExpense;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "operational_expenses")
public class OperationalExpenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private ExpenseType type;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(name = "simulation_step", nullable = false)
    private int simulationStep;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected OperationalExpenseEntity() {
    }

    private OperationalExpenseEntity(OperationalExpense expense) {
        this.id = expense.id();
        this.type = expense.type();
        this.amount = expense.amount();
        this.description = expense.description();
        this.simulationStep = expense.simulationStep();
        this.createdAt = expense.createdAt();
    }

    public static OperationalExpenseEntity from(OperationalExpense expense) {
        return new OperationalExpenseEntity(expense);
    }

    public OperationalExpense toDomain() {
        return new OperationalExpense(id, type, amount, description, simulationStep, createdAt);
    }
}
