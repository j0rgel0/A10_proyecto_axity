package com.parque.dinosaurios.domain.model;

import com.parque.dinosaurios.domain.enums.VehicleStatus;
import java.util.Objects;

public class Vehicle {

    private final String id;
    private final int capacity;
    private VehicleStatus status;
    private int occupiedSeats;

    public Vehicle(String id, int capacity) {
        this.id = Objects.requireNonNull(id);
        this.capacity = Math.max(1, capacity);
        this.status = VehicleStatus.AVAILABLE;
    }

    public String getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public int getOccupiedSeats() {
        return occupiedSeats;
    }

    public boolean isAvailable() {
        return status == VehicleStatus.AVAILABLE;
    }

    public int board(int passengers) {
        if (!isAvailable()) {
            return 0;
        }
        int admitted = Math.min(Math.max(0, passengers), capacity);
        occupiedSeats = admitted;
        status = admitted > 0 ? VehicleStatus.IN_USE : VehicleStatus.AVAILABLE;
        return admitted;
    }

    public void release() {
        occupiedSeats = 0;
        if (status == VehicleStatus.IN_USE) {
            status = VehicleStatus.AVAILABLE;
        }
    }

    public void fail() {
        status = VehicleStatus.FAILED;
    }

    public void repair() {
        occupiedSeats = 0;
        status = VehicleStatus.AVAILABLE;
    }
}
