package com.parque.dinosaurios.simulation;

import static org.assertj.core.api.Assertions.assertThat;

import com.parque.dinosaurios.domain.enums.EventStatus;
import com.parque.dinosaurios.domain.enums.EventType;
import com.parque.dinosaurios.domain.enums.VehicleStatus;
import com.parque.dinosaurios.simulation.event.BlackoutStrategy;
import com.parque.dinosaurios.simulation.event.DinosaurEscapeStrategy;
import com.parque.dinosaurios.simulation.event.OfferHourStrategy;
import com.parque.dinosaurios.simulation.event.StormStrategy;
import com.parque.dinosaurios.simulation.event.VehicleFailureStrategy;
import com.parque.dinosaurios.support.ParkStateFixtures;
import org.junit.jupiter.api.Test;

class EventStrategyTest {

    @Test
    void blackoutConsumesEnergyAndCreatesActiveEvent() {
        var state = ParkStateFixtures.parkStateWithTourists();

        var event = new BlackoutStrategy().execute(state);

        assertThat(event.type()).isEqualTo(EventType.BLACKOUT);
        assertThat(event.status()).isEqualTo(EventStatus.ACTIVE);
        assertThat(state.getEnergyAvailable()).isEqualTo(280);
        assertThat(state.averageSatisfaction()).isLessThan(75);
    }

    @Test
    void dinosaurEscapeIsContainedAndResolved() {
        var state = ParkStateFixtures.parkStateWithTourists();

        var event = new DinosaurEscapeStrategy().execute(state);

        assertThat(event.type()).isEqualTo(EventType.DINOSAUR_ESCAPE);
        assertThat(event.status()).isEqualTo(EventStatus.RESOLVED);
        assertThat(state.countDinosaursInEnclosures()).isEqualTo(1);
    }

    @Test
    void offerHourImprovesSatisfaction() {
        var state = ParkStateFixtures.parkStateWithTourists();

        var event = new OfferHourStrategy().execute(state);

        assertThat(event.type()).isEqualTo(EventType.OFFER_HOUR);
        assertThat(state.averageSatisfaction()).isGreaterThan(75);
    }

    @Test
    void stormConsumesEnergyAndLeavesActiveEvent() {
        var state = ParkStateFixtures.parkStateWithTourists();

        var event = new StormStrategy().execute(state);

        assertThat(event.type()).isEqualTo(EventType.STORM);
        assertThat(event.status()).isEqualTo(EventStatus.ACTIVE);
        assertThat(state.getEnergyAvailable()).isEqualTo(420);
    }

    @Test
    void vehicleFailureMarksAvailableVehicleAsFailed() {
        var state = ParkStateFixtures.parkStateWithTourists();

        var event = new VehicleFailureStrategy().execute(state);

        assertThat(event.type()).isEqualTo(EventType.VEHICLE_FAILURE);
        assertThat(state.getVehicles().get(0).getStatus()).isEqualTo(VehicleStatus.FAILED);
    }
}
