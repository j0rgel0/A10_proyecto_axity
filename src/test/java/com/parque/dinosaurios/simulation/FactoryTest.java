package com.parque.dinosaurios.simulation;

import static org.assertj.core.api.Assertions.assertThat;

import com.parque.dinosaurios.config.ParkProperties;
import com.parque.dinosaurios.domain.enums.DinosaurType;
import com.parque.dinosaurios.domain.enums.TouristType;
import com.parque.dinosaurios.simulation.factory.DinosaurFactory;
import com.parque.dinosaurios.simulation.factory.TouristFactory;
import com.parque.dinosaurios.support.StubRandomProvider;
import org.junit.jupiter.api.Test;

class FactoryTest {

    @Test
    void dinosaurFactoryCreatesConfiguredPopulation() {
        ParkProperties properties = new ParkProperties();
        properties.getDinosaurs().setCarnivores(2);
        properties.getDinosaurs().setHerbivores(1);

        var dinosaurs = new DinosaurFactory(properties).createInitialDinosaurs();

        assertThat(dinosaurs).hasSize(3);
        assertThat(dinosaurs).filteredOn(dinosaur -> dinosaur.getType() == DinosaurType.CARNIVORE).hasSize(2);
    }

    @Test
    void touristFactoryUsesRandomTypeAndSatisfaction() {
        TouristFactory factory = new TouristFactory(new StubRandomProvider().addDouble(0.10).addInt(3));

        var tourist = factory.create(7);

        assertThat(tourist.getId()).isEqualTo("TUR-7");
        assertThat(tourist.getType()).isEqualTo(TouristType.CHILD);
        assertThat(tourist.getSatisfaction()).isEqualTo(81);
    }
}
