package com.parque.dinosaurios.domain.model;

import com.parque.dinosaurios.domain.enums.TouristType;
import java.util.Objects;

public class Tourist {

    private final String id;
    private final TouristType type;
    private int satisfaction;
    private boolean active;
    private boolean ticketPaid;

    public Tourist(String id, TouristType type, int satisfaction) {
        this.id = Objects.requireNonNull(id);
        this.type = Objects.requireNonNull(type);
        this.satisfaction = clamp(satisfaction);
        this.active = true;
    }

    public String getId() {
        return id;
    }

    public TouristType getType() {
        return type;
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isTicketPaid() {
        return ticketPaid;
    }

    public void payTicket() {
        ticketPaid = true;
    }

    public void changeSatisfaction(int delta) {
        satisfaction = clamp(satisfaction + delta);
    }

    public void leave() {
        active = false;
    }

    private int clamp(int value) {
        return Math.max(0, Math.min(100, value));
    }
}
