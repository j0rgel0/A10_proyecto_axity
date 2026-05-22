package com.parque.dinosaurios.domain.model;

import com.parque.dinosaurios.domain.enums.DinosaurStatus;
import com.parque.dinosaurios.domain.enums.DinosaurType;
import com.parque.dinosaurios.domain.enums.ZoneType;
import java.util.Objects;

public class Dinosaur {

    private final String id;
    private final String name;
    private final DinosaurType type;
    private DinosaurStatus status;
    private int hungerLevel;
    private ZoneType enclosure;

    public Dinosaur(String id, String name, DinosaurType type, ZoneType enclosure) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.type = Objects.requireNonNull(type);
        this.enclosure = Objects.requireNonNull(enclosure);
        this.status = DinosaurStatus.HEALTHY;
        this.hungerLevel = 10;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DinosaurType getType() {
        return type;
    }

    public DinosaurStatus getStatus() {
        return status;
    }

    public int getHungerLevel() {
        return hungerLevel;
    }

    public ZoneType getEnclosure() {
        return enclosure;
    }

    public void feed(int foodUnits) {
        hungerLevel = Math.max(0, hungerLevel - Math.max(0, foodUnits));
        if (status == DinosaurStatus.HUNGRY && hungerLevel < 70) {
            status = DinosaurStatus.HEALTHY;
        }
    }

    public void increaseHunger(int units) {
        hungerLevel = Math.min(100, hungerLevel + Math.max(0, units));
        if (hungerLevel >= 70 && status == DinosaurStatus.HEALTHY) {
            status = DinosaurStatus.HUNGRY;
        }
    }

    public void escape() {
        status = DinosaurStatus.ESCAPED;
        enclosure = ZoneType.CENTRAL_PLAZA;
    }

    public void contain(ZoneType enclosure) {
        this.enclosure = Objects.requireNonNull(enclosure);
        this.status = DinosaurStatus.CONTAINED;
    }
}
