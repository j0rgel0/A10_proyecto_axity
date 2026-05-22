package com.parque.dinosaurios.application.service;

import com.parque.dinosaurios.application.dto.MonitoringSnapshot;
import com.parque.dinosaurios.domain.model.ParkState;
import com.parque.dinosaurios.simulation.observer.ParkStateObserver;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.stereotype.Service;

@Service
public class MonitoringService implements ParkStateObserver {

    private final AtomicReference<MonitoringSnapshot> currentSnapshot =
            new AtomicReference<>(MonitoringSnapshot.empty());

    @Override
    public void onStateChanged(ParkState state) {
        currentSnapshot.set(MonitoringSnapshot.from(state));
    }

    public MonitoringSnapshot currentSnapshot() {
        return currentSnapshot.get();
    }
}
