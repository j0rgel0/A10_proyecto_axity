package com.parque.dinosaurios.infrastructure.persistence.entity;

import com.parque.dinosaurios.domain.enums.RevenueType;
import com.parque.dinosaurios.domain.model.Revenue;
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
@Table(name = "revenues")
public class RevenueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private RevenueType type;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(name = "simulation_step", nullable = false)
    private int simulationStep;

    @Column(name = "tourist_count", nullable = false)
    private int touristCount;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected RevenueEntity() {
    }

    private RevenueEntity(Revenue revenue) {
        this.id = revenue.id();
        this.type = revenue.type();
        this.amount = revenue.amount();
        this.description = revenue.description();
        this.simulationStep = revenue.simulationStep();
        this.touristCount = revenue.touristCount();
        this.createdAt = revenue.createdAt();
    }

    public static RevenueEntity from(Revenue revenue) {
        return new RevenueEntity(revenue);
    }

    public Revenue toDomain() {
        return new Revenue(id, type, amount, description, simulationStep, touristCount, createdAt);
    }
}
