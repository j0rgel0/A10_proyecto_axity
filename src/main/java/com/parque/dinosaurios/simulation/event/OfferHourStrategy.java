package com.parque.dinosaurios.simulation.event;

import com.parque.dinosaurios.domain.enums.EventStatus;
import com.parque.dinosaurios.domain.enums.EventType;
import com.parque.dinosaurios.domain.model.ParkEvent;
import com.parque.dinosaurios.domain.model.ParkState;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class OfferHourStrategy implements ParkEventStrategy {

    @Override
    public EventType type() {
        return EventType.OFFER_HOUR;
    }

    @Override
    public ParkEvent execute(ParkState state) {
        LocalDateTime now = LocalDateTime.now();
        state.changeSatisfactionForActiveTourists(9);
        return new ParkEvent(
                null,
                type(),
                EventStatus.RESOLVED,
                "Hora de ofertas en souvenirs y experiencias",
                2,
                0,
                9,
                state.getSimulationStep(),
                now,
                now.plusMinutes(30));
    }
}
