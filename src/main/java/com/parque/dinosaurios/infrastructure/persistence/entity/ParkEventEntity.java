package com.parque.dinosaurios.infrastructure.persistence.entity;

import com.parque.dinosaurios.domain.enums.EventStatus;
import com.parque.dinosaurios.domain.enums.EventType;
import com.parque.dinosaurios.domain.model.ParkEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "park_events")
public class ParkEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private EventType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private EventStatus status;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(nullable = false)
    private int severity;

    @Column(name = "energy_delta", nullable = false)
    private int energyDelta;

    @Column(name = "satisfaction_delta", nullable = false)
    private int satisfactionDelta;

    @Column(name = "simulation_step", nullable = false)
    private int simulationStep;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    protected ParkEventEntity() {
    }

    private ParkEventEntity(ParkEvent event) {
        this.id = event.id();
        this.type = event.type();
        this.status = event.status();
        this.description = event.description();
        this.severity = event.severity();
        this.energyDelta = event.energyDelta();
        this.satisfactionDelta = event.satisfactionDelta();
        this.simulationStep = event.simulationStep();
        this.createdAt = event.createdAt();
        this.resolvedAt = event.resolvedAt();
    }

    public static ParkEventEntity from(ParkEvent event) {
        return new ParkEventEntity(event);
    }

    public ParkEvent toDomain() {
        return new ParkEvent(
                id,
                type,
                status,
                description,
                severity,
                energyDelta,
                satisfactionDelta,
                simulationStep,
                createdAt,
                resolvedAt);
    }
}
