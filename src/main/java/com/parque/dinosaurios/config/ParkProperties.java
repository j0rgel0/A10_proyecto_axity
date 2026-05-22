package com.parque.dinosaurios.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "park")
public class ParkProperties {

    @Valid
    private Simulation simulation = new Simulation();

    @Valid
    private Tickets tickets = new Tickets();

    @Valid
    private Dinosaurs dinosaurs = new Dinosaurs();

    @Valid
    private Workers workers = new Workers();

    @Valid
    private Vehicles vehicles = new Vehicles();

    @Valid
    private Energy energy = new Energy();

    @Valid
    private Events events = new Events();

    public Simulation getSimulation() {
        return simulation;
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    public Tickets getTickets() {
        return tickets;
    }

    public void setTickets(Tickets tickets) {
        this.tickets = tickets;
    }

    public Dinosaurs getDinosaurs() {
        return dinosaurs;
    }

    public void setDinosaurs(Dinosaurs dinosaurs) {
        this.dinosaurs = dinosaurs;
    }

    public Workers getWorkers() {
        return workers;
    }

    public void setWorkers(Workers workers) {
        this.workers = workers;
    }

    public Vehicles getVehicles() {
        return vehicles;
    }

    public void setVehicles(Vehicles vehicles) {
        this.vehicles = vehicles;
    }

    public Energy getEnergy() {
        return energy;
    }

    public void setEnergy(Energy energy) {
        this.energy = energy;
    }

    public Events getEvents() {
        return events;
    }

    public void setEvents(Events events) {
        this.events = events;
    }

    public static class Simulation {
        @Min(1)
        private int steps = 8;

        @Min(0)
        private int minTouristsPerStep = 8;

        @Min(1)
        private int maxTouristsPerStep = 22;

        public int getSteps() {
            return steps;
        }

        public void setSteps(int steps) {
            this.steps = steps;
        }

        public int getMinTouristsPerStep() {
            return minTouristsPerStep;
        }

        public void setMinTouristsPerStep(int minTouristsPerStep) {
            this.minTouristsPerStep = minTouristsPerStep;
        }

        public int getMaxTouristsPerStep() {
            return maxTouristsPerStep;
        }

        public void setMaxTouristsPerStep(int maxTouristsPerStep) {
            this.maxTouristsPerStep = maxTouristsPerStep;
        }
    }

    public static class Tickets {
        @DecimalMin("0.01")
        private BigDecimal basePrice = new BigDecimal("85.00");

        @DecimalMin("0.00")
        @Max(1)
        private BigDecimal childDiscount = new BigDecimal("0.50");

        public BigDecimal getBasePrice() {
            return basePrice;
        }

        public void setBasePrice(BigDecimal basePrice) {
            this.basePrice = basePrice;
        }

        public BigDecimal getChildDiscount() {
            return childDiscount;
        }

        public void setChildDiscount(BigDecimal childDiscount) {
            this.childDiscount = childDiscount;
        }
    }

    public static class Dinosaurs {
        @Min(0)
        private int carnivores = 3;

        @Min(0)
        private int herbivores = 7;

        public int getCarnivores() {
            return carnivores;
        }

        public void setCarnivores(int carnivores) {
            this.carnivores = carnivores;
        }

        public int getHerbivores() {
            return herbivores;
        }

        public void setHerbivores(int herbivores) {
            this.herbivores = herbivores;
        }
    }

    public static class Workers {
        @Min(0)
        private int rangers = 6;

        @Min(0)
        private int mechanics = 3;

        @Min(0)
        private int vendors = 5;

        public int getRangers() {
            return rangers;
        }

        public void setRangers(int rangers) {
            this.rangers = rangers;
        }

        public int getMechanics() {
            return mechanics;
        }

        public void setMechanics(int mechanics) {
            this.mechanics = mechanics;
        }

        public int getVendors() {
            return vendors;
        }

        public void setVendors(int vendors) {
            this.vendors = vendors;
        }
    }

    public static class Vehicles {
        @Min(0)
        private int safariTrucks = 4;

        @Min(1)
        private int capacity = 12;

        public int getSafariTrucks() {
            return safariTrucks;
        }

        public void setSafariTrucks(int safariTrucks) {
            this.safariTrucks = safariTrucks;
        }

        public int getCapacity() {
            return capacity;
        }

        public void setCapacity(int capacity) {
            this.capacity = capacity;
        }
    }

    public static class Energy {
        @Min(1)
        private int initialUnits = 1000;

        @Min(0)
        private int consumptionPerStep = 46;

        @DecimalMin("0.00")
        private BigDecimal maintenanceCost = new BigDecimal("120.00");

        public int getInitialUnits() {
            return initialUnits;
        }

        public void setInitialUnits(int initialUnits) {
            this.initialUnits = initialUnits;
        }

        public int getConsumptionPerStep() {
            return consumptionPerStep;
        }

        public void setConsumptionPerStep(int consumptionPerStep) {
            this.consumptionPerStep = consumptionPerStep;
        }

        public BigDecimal getMaintenanceCost() {
            return maintenanceCost;
        }

        public void setMaintenanceCost(BigDecimal maintenanceCost) {
            this.maintenanceCost = maintenanceCost;
        }
    }

    public static class Events {
        @DecimalMin("0.00")
        private double baseProbability = 0.38;

        public double getBaseProbability() {
            return baseProbability;
        }

        public void setBaseProbability(double baseProbability) {
            this.baseProbability = baseProbability;
        }
    }
}
