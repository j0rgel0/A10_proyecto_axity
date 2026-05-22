package com.parque.dinosaurios.support;

import com.parque.dinosaurios.domain.enums.DinosaurType;
import com.parque.dinosaurios.domain.enums.TouristType;
import com.parque.dinosaurios.domain.enums.ZoneType;
import com.parque.dinosaurios.domain.model.Dinosaur;
import com.parque.dinosaurios.domain.model.ParkState;
import com.parque.dinosaurios.domain.model.Tourist;
import com.parque.dinosaurios.domain.model.Vehicle;
import com.parque.dinosaurios.domain.model.Worker;
import com.parque.dinosaurios.domain.model.Zone;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class ParkStateFixtures {

    private ParkStateFixtures() {
    }

    public static ParkState basicParkState() {
        Map<ZoneType, Zone> zones = new EnumMap<>(ZoneType.class);
        zones.put(ZoneType.ARRIVAL, new Zone(ZoneType.ARRIVAL, 20, 2));
        zones.put(ZoneType.CENTRAL_PLAZA, new Zone(ZoneType.CENTRAL_PLAZA, 40, 5));
        zones.put(ZoneType.BATHROOMS, new Zone(ZoneType.BATHROOMS, 5, 2));
        zones.put(ZoneType.ENERGY_PLANT, new Zone(ZoneType.ENERGY_PLANT, 2, 8));
        zones.put(ZoneType.OBSERVATION_ENCLOSURE, new Zone(ZoneType.OBSERVATION_ENCLOSURE, 30, 10));
        return new ParkState(
                List.of(new Dinosaur("D-1", "Rex", DinosaurType.CARNIVORE, ZoneType.OBSERVATION_ENCLOSURE)),
                List.of(new Worker("W-1", "ranger")),
                List.of(new Vehicle("V-1", 10)),
                zones,
                500);
    }

    public static ParkState parkStateWithTourists() {
        ParkState state = basicParkState();
        state.advanceStep();
        state.admitTourist(new Tourist("T-1", TouristType.ADULT, 70));
        state.admitTourist(new Tourist("T-2", TouristType.CHILD, 80));
        return state;
    }
}
