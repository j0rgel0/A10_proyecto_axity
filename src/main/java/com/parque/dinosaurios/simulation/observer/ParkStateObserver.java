package com.parque.dinosaurios.simulation.observer;

import com.parque.dinosaurios.domain.model.ParkState;

public interface ParkStateObserver {

    void onStateChanged(ParkState state);
}
