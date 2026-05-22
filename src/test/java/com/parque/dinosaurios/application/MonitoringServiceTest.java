package com.parque.dinosaurios.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.parque.dinosaurios.application.service.MonitoringService;
import com.parque.dinosaurios.domain.enums.EventStatus;
import com.parque.dinosaurios.domain.enums.EventType;
import com.parque.dinosaurios.domain.model.ParkEvent;
import com.parque.dinosaurios.support.ParkStateFixtures;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class MonitoringServiceTest {

    @Test
    void currentSnapshotStartsEmpty() {
        MonitoringService service = new MonitoringService();

        var snapshot = service.currentSnapshot();

        assertThat(snapshot.simulationStep()).isZero();
        assertThat(snapshot.activeTourists()).isZero();
        assertThat(snapshot.energyAvailable()).isZero();
    }

    @Test
    void onStateChangedStoresOperationalSnapshot() {
        MonitoringService service = new MonitoringService();
        var state = ParkStateFixtures.parkStateWithTourists();
        state.consumeEnergy(100);
        state.getVehicles().get(0).board(2);
        state.registerEvent(new ParkEvent(
                null,
                EventType.BLACKOUT,
                EventStatus.ACTIVE,
                "Apagon de prueba",
                4,
                -100,
                -10,
                state.getSimulationStep(),
                LocalDateTime.now(),
                null));

        service.onStateChanged(state);

        var snapshot = service.currentSnapshot();
        assertThat(snapshot.simulationStep()).isEqualTo(1);
        assertThat(snapshot.activeTourists()).isEqualTo(2);
        assertThat(snapshot.processedTourists()).isEqualTo(2);
        assertThat(snapshot.energyAvailable()).isEqualTo(400);
        assertThat(snapshot.activeEvents()).isEqualTo(1);
        assertThat(snapshot.vehiclesInUse()).isEqualTo(1);
        assertThat(snapshot.averageSatisfaction()).isEqualTo(75.0);
    }
}
