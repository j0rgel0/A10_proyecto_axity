package com.parque.dinosaurios.simulation.event;

import com.parque.dinosaurios.domain.enums.EventStatus;
import com.parque.dinosaurios.domain.enums.EventType;
import com.parque.dinosaurios.domain.enums.ZoneType;
import com.parque.dinosaurios.domain.model.Dinosaur;
import com.parque.dinosaurios.domain.model.ParkEvent;
import com.parque.dinosaurios.domain.model.ParkState;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class DinosaurEscapeStrategy implements ParkEventStrategy {

    @Override
    public EventType type() {
        return EventType.DINOSAUR_ESCAPE;
    }

    @Override
    public ParkEvent execute(ParkState state) {
        LocalDateTime now = LocalDateTime.now();
        return state.findContainedDinosaur()
                .map(dinosaur -> escapeDinosaur(state, dinosaur, now))
                .orElseGet(() -> new ParkEvent(
                        null,
                        type(),
                        EventStatus.RESOLVED,
                        "Alerta revisada sin dinosaurios disponibles para escape",
                        1,
                        0,
                        0,
                        state.getSimulationStep(),
                        now,
                        now));
    }

    private ParkEvent escapeDinosaur(ParkState state, Dinosaur dinosaur, LocalDateTime now) {
        dinosaur.escape();
        dinosaur.contain(ZoneType.OBSERVATION_ENCLOSURE);
        state.changeSatisfactionForActiveTourists(-18);
        state.adjustEnergy(-30);
        return new ParkEvent(
                null,
                type(),
                EventStatus.RESOLVED,
                "Escape contenido del dinosaurio " + dinosaur.getName(),
                5,
                -30,
                -18,
                state.getSimulationStep(),
                now,
                now.plusMinutes(12));
    }
}
