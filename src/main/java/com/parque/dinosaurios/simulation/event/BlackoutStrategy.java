package com.parque.dinosaurios.simulation.event;

import com.parque.dinosaurios.domain.enums.EventStatus;
import com.parque.dinosaurios.domain.enums.EventType;
import com.parque.dinosaurios.domain.model.ParkEvent;
import com.parque.dinosaurios.domain.model.ParkState;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class BlackoutStrategy implements ParkEventStrategy {

    @Override
    public EventType type() {
        return EventType.BLACKOUT;
    }

    @Override
    public ParkEvent execute(ParkState state) {
        int energyDelta = -220;
        state.adjustEnergy(energyDelta);
        state.changeSatisfactionForActiveTourists(-12);
        LocalDateTime now = LocalDateTime.now();
        return new ParkEvent(
                null,
                type(),
                EventStatus.ACTIVE,
                "Apagon masivo en la planta de energia",
                4,
                energyDelta,
                -12,
                state.getSimulationStep(),
                now,
                null);
    }
}
