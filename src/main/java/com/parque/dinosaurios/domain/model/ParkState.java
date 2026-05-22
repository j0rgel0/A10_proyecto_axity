package com.parque.dinosaurios.domain.model;

import com.parque.dinosaurios.domain.enums.EventStatus;
import com.parque.dinosaurios.domain.enums.VehicleStatus;
import com.parque.dinosaurios.domain.enums.ZoneType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ParkState {

    private final List<Tourist> activeTourists = new ArrayList<>();
    private final List<Dinosaur> dinosaurs;
    private final List<Worker> workers;
    private final List<Vehicle> vehicles;
    private final Map<ZoneType, Zone> zones;
    private final List<ParkEvent> events = new ArrayList<>();
    private int simulationStep;
    private int processedTourists;
    private int energyAvailable;

    public ParkState(
            List<Dinosaur> dinosaurs,
            List<Worker> workers,
            List<Vehicle> vehicles,
            Map<ZoneType, Zone> zones,
            int energyAvailable) {
        this.dinosaurs = new ArrayList<>(dinosaurs);
        this.workers = new ArrayList<>(workers);
        this.vehicles = new ArrayList<>(vehicles);
        this.zones = new EnumMap<>(zones);
        this.energyAvailable = Math.max(0, energyAvailable);
    }

    public int getSimulationStep() {
        return simulationStep;
    }

    public void advanceStep() {
        simulationStep++;
    }

    public List<Tourist> getActiveTourists() {
        return Collections.unmodifiableList(activeTourists);
    }

    public List<Dinosaur> getDinosaurs() {
        return Collections.unmodifiableList(dinosaurs);
    }

    public List<Worker> getWorkers() {
        return Collections.unmodifiableList(workers);
    }

    public List<Vehicle> getVehicles() {
        return Collections.unmodifiableList(vehicles);
    }

    public Map<ZoneType, Zone> getZones() {
        return Collections.unmodifiableMap(zones);
    }

    public List<ParkEvent> getEvents() {
        return Collections.unmodifiableList(events);
    }

    public int getProcessedTourists() {
        return processedTourists;
    }

    public int getEnergyAvailable() {
        return energyAvailable;
    }

    public void admitTourist(Tourist tourist) {
        activeTourists.add(tourist);
        processedTourists++;
    }

    public int releaseTourists(int amount) {
        int departures = Math.min(Math.max(0, amount), activeTourists.size());
        for (int index = 0; index < departures; index++) {
            activeTourists.remove(0).leave();
        }
        return departures;
    }

    public void consumeEnergy(int units) {
        energyAvailable = Math.max(0, energyAvailable - Math.max(0, units));
    }

    public void adjustEnergy(int delta) {
        energyAvailable = Math.max(0, energyAvailable + delta);
    }

    public void registerEvent(ParkEvent event) {
        events.add(event);
    }

    public long countActiveEvents() {
        return events.stream().filter(event -> event.status() == EventStatus.ACTIVE).count();
    }

    public long countDinosaursInEnclosures() {
        return dinosaurs.stream()
                .filter(dinosaur -> dinosaur.getStatus() != com.parque.dinosaurios.domain.enums.DinosaurStatus.ESCAPED)
                .count();
    }

    public long countVehiclesInUse() {
        return vehicles.stream().filter(vehicle -> vehicle.getStatus() == VehicleStatus.IN_USE).count();
    }

    public double averageSatisfaction() {
        if (activeTourists.isEmpty()) {
            return 0.0;
        }
        return activeTourists.stream().mapToInt(Tourist::getSatisfaction).average().orElse(0.0);
    }

    public void changeSatisfactionForActiveTourists(int delta) {
        activeTourists.forEach(tourist -> tourist.changeSatisfaction(delta));
    }

    public Optional<Vehicle> findAvailableVehicle() {
        return vehicles.stream().filter(Vehicle::isAvailable).findFirst();
    }

    public Optional<Dinosaur> findContainedDinosaur() {
        return dinosaurs.stream()
                .filter(dinosaur -> dinosaur.getStatus() != com.parque.dinosaurios.domain.enums.DinosaurStatus.ESCAPED)
                .findFirst();
    }
}
