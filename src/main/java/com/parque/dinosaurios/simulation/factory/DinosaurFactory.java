package com.parque.dinosaurios.simulation.factory;

import com.parque.dinosaurios.config.ParkProperties;
import com.parque.dinosaurios.domain.enums.DinosaurType;
import com.parque.dinosaurios.domain.enums.ZoneType;
import com.parque.dinosaurios.domain.model.Dinosaur;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DinosaurFactory {

    private static final List<String> CARNIVORE_NAMES = List.of("Rex", "Delta", "Sierra", "Echo", "Fang");
    private static final List<String> HERBIVORE_NAMES = List.of("Bruma", "Luna", "Atlas", "Nopal", "Sauce");

    private final ParkProperties properties;

    public DinosaurFactory(ParkProperties properties) {
        this.properties = properties;
    }

    public List<Dinosaur> createInitialDinosaurs() {
        List<Dinosaur> dinosaurs = new ArrayList<>();
        for (int index = 0; index < properties.getDinosaurs().getCarnivores(); index++) {
            dinosaurs.add(new Dinosaur(
                    "CAR-" + (index + 1),
                    CARNIVORE_NAMES.get(index % CARNIVORE_NAMES.size()),
                    DinosaurType.CARNIVORE,
                    ZoneType.OBSERVATION_ENCLOSURE));
        }
        for (int index = 0; index < properties.getDinosaurs().getHerbivores(); index++) {
            dinosaurs.add(new Dinosaur(
                    "HER-" + (index + 1),
                    HERBIVORE_NAMES.get(index % HERBIVORE_NAMES.size()),
                    DinosaurType.HERBIVORE,
                    ZoneType.OBSERVATION_ENCLOSURE));
        }
        return dinosaurs;
    }
}
