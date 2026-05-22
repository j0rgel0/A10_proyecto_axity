package com.parque.dinosaurios.simulation.event;

import com.parque.dinosaurios.domain.enums.EventStatus;
import com.parque.dinosaurios.domain.enums.EventType;
import com.parque.dinosaurios.domain.model.ParkEvent;
import com.parque.dinosaurios.domain.model.ParkState;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class StormStrategy implements ParkEventStrategy {

    @Override
    public EventType type() {
        return EventType.STORM;
    }

    @Override
    public ParkEvent execute(ParkState state) {
        LocalDateTime now = LocalDateTime.now();
        state.consumeEnergy(80);
        state.changeSatisfactionForActiveTourists(-10);
        return new ParkEvent(
                null,
                type(),
                EventStatus.ACTIVE,
                "Tormenta torrencial limita recorridos exteriores",
                3,
                -80,
                -10,
                state.getSimulationStep(),
                now,
                null);
    }
}
