package com.parque.dinosaurios.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.parque.dinosaurios.domain.enums.DinosaurStatus;
import com.parque.dinosaurios.domain.enums.DinosaurType;
import com.parque.dinosaurios.domain.enums.TouristType;
import com.parque.dinosaurios.domain.enums.VehicleStatus;
import com.parque.dinosaurios.domain.enums.ZoneType;
import com.parque.dinosaurios.domain.model.Dinosaur;
import com.parque.dinosaurios.domain.model.Tourist;
import com.parque.dinosaurios.domain.model.Vehicle;
import com.parque.dinosaurios.domain.model.Zone;
import org.junit.jupiter.api.Test;

class DomainModelTest {

    @Test
    void touristKeepsSatisfactionInsideBoundsAndPaysTicket() {
        Tourist tourist = new Tourist("T-1", TouristType.ADULT, 95);

        tourist.changeSatisfaction(20);
        tourist.payTicket();
        tourist.leave();

        assertThat(tourist.getSatisfaction()).isEqualTo(100);
        assertThat(tourist.isTicketPaid()).isTrue();
        assertThat(tourist.isActive()).isFalse();
    }

    @Test
    void dinosaurCanBecomeHungryEscapeAndBeContained() {
        Dinosaur dinosaur = new Dinosaur("D-1", "Rex", DinosaurType.CARNIVORE, ZoneType.OBSERVATION_ENCLOSURE);

        dinosaur.increaseHunger(80);
        dinosaur.escape();
        dinosaur.contain(ZoneType.OBSERVATION_ENCLOSURE);
        dinosaur.feed(50);

        assertThat(dinosaur.getStatus()).isEqualTo(DinosaurStatus.CONTAINED);
        assertThat(dinosaur.getHungerLevel()).isLessThan(70);
        assertThat(dinosaur.getEnclosure()).isEqualTo(ZoneType.OBSERVATION_ENCLOSURE);
    }

    @Test
    void vehicleBoardsPassengersAndCanFailAndRepair() {
        Vehicle vehicle = new Vehicle("V-1", 4);

        int admitted = vehicle.board(10);
        vehicle.fail();
        vehicle.repair();

        assertThat(admitted).isEqualTo(4);
        assertThat(vehicle.getStatus()).isEqualTo(VehicleStatus.AVAILABLE);
        assertThat(vehicle.getOccupiedSeats()).isZero();
    }

    @Test
    void zoneAdmitsOnlyAvailableCapacity() {
        Zone zone = new Zone(ZoneType.BATHROOMS, 3, 1);

        int first = zone.admit(2);
        int second = zone.admit(5);
        zone.release(10);

        assertThat(first).isEqualTo(2);
        assertThat(second).isEqualTo(1);
        assertThat(zone.getCurrentVisitors()).isZero();
    }
}
