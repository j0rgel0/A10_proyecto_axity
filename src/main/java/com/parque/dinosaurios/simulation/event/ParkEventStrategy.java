package com.parque.dinosaurios.simulation.event;

import com.parque.dinosaurios.domain.enums.EventType;
import com.parque.dinosaurios.domain.model.ParkEvent;
import com.parque.dinosaurios.domain.model.ParkState;

public interface ParkEventStrategy {

    EventType type();

    ParkEvent execute(ParkState state);
}
