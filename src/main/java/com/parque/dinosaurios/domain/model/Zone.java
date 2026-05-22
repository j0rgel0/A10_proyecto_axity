package com.parque.dinosaurios.domain.model;

import com.parque.dinosaurios.domain.enums.ZoneType;
import java.util.Objects;

public class Zone {

    private final ZoneType type;
    private final int capacity;
    private final int energyDemand;
    private int currentVisitors;

    public Zone(ZoneType type, int capacity, int energyDemand) {
        this.type = Objects.requireNonNull(type);
        this.capacity = Math.max(1, capacity);
        this.energyDemand = Math.max(0, energyDemand);
    }

    public ZoneType getType() {
        return type;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getEnergyDemand() {
        return energyDemand;
    }

    public int getCurrentVisitors() {
        return currentVisitors;
    }

    public boolean hasCapacityFor(int visitors) {
        return currentVisitors + Math.max(0, visitors) <= capacity;
    }

    public int admit(int visitors) {
        int admitted = Math.min(Math.max(0, visitors), capacity - currentVisitors);
        currentVisitors += admitted;
        return admitted;
    }

    public void release(int visitors) {
        currentVisitors = Math.max(0, currentVisitors - Math.max(0, visitors));
    }
}
