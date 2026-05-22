package com.parque.dinosaurios.simulation;

import com.parque.dinosaurios.application.dto.MonitoringSnapshot;
import com.parque.dinosaurios.application.dto.ParkReport;
import com.parque.dinosaurios.application.dto.SimulationResult;
import com.parque.dinosaurios.application.service.EventService;
import com.parque.dinosaurios.application.service.ExpenseService;
import com.parque.dinosaurios.application.service.ReportService;
import com.parque.dinosaurios.application.service.RevenueService;
import com.parque.dinosaurios.application.service.TicketService;
import com.parque.dinosaurios.config.ParkProperties;
import com.parque.dinosaurios.domain.enums.ExpenseType;
import com.parque.dinosaurios.domain.enums.RevenueType;
import com.parque.dinosaurios.domain.enums.ZoneType;
import com.parque.dinosaurios.domain.model.Dinosaur;
import com.parque.dinosaurios.domain.model.ParkEvent;
import com.parque.dinosaurios.domain.model.ParkState;
import com.parque.dinosaurios.domain.model.Tourist;
import com.parque.dinosaurios.domain.model.Vehicle;
import com.parque.dinosaurios.domain.model.Worker;
import com.parque.dinosaurios.domain.model.Zone;
import com.parque.dinosaurios.simulation.factory.DinosaurFactory;
import com.parque.dinosaurios.simulation.factory.TouristFactory;
import com.parque.dinosaurios.simulation.observer.ParkStateObserver;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SimulationEngine {

    private final ParkProperties properties;
    private final DinosaurFactory dinosaurFactory;
    private final TouristFactory touristFactory;
    private final TicketService ticketService;
    private final RevenueService revenueService;
    private final ExpenseService expenseService;
    private final EventService eventService;
    private final ReportService reportService;
    private final RandomProvider randomProvider;
    private final List<ParkStateObserver> observers;

    public SimulationEngine(
            ParkProperties properties,
            DinosaurFactory dinosaurFactory,
            TouristFactory touristFactory,
            TicketService ticketService,
            RevenueService revenueService,
            ExpenseService expenseService,
            EventService eventService,
            ReportService reportService,
            RandomProvider randomProvider,
            List<ParkStateObserver> observers) {
        this.properties = properties;
        this.dinosaurFactory = dinosaurFactory;
        this.touristFactory = touristFactory;
        this.ticketService = ticketService;
        this.revenueService = revenueService;
        this.expenseService = expenseService;
        this.eventService = eventService;
        this.reportService = reportService;
        this.randomProvider = randomProvider;
        this.observers = observers;
    }

    public SimulationResult run(int requestedSteps) {
        int steps = requestedSteps > 0 ? requestedSteps : properties.getSimulation().getSteps();
        ParkState state = initializePark();
        notifyObservers(state);
        for (int index = 0; index < steps; index++) {
            state.advanceStep();
            processArrivals(state);
            processCentralPlaza(state);
            processBathrooms(state);
            processObservationEnclosure(state);
            processEnergyPlant(state);
            processDinosaurCare(state);
            eventService.maybeGenerate(state);
            releaseVisitors(state);
            notifyObservers(state);
        }
        ParkReport report = reportService.build(state);
        MonitoringSnapshot snapshot = MonitoringSnapshot.from(state);
        return new SimulationResult(snapshot, report, List.copyOf(state.getEvents()));
    }

    private ParkState initializePark() {
        List<Dinosaur> dinosaurs = dinosaurFactory.createInitialDinosaurs();
        List<Vehicle> vehicles = createVehicles();
        Map<ZoneType, Zone> zones = createZones();
        return new ParkState(dinosaurs, createWorkers(), vehicles, zones, properties.getEnergy().getInitialUnits());
    }

    private List<Worker> createWorkers() {
        List<Worker> workers = new ArrayList<>();
        addWorkers(workers, "ranger", properties.getWorkers().getRangers());
        addWorkers(workers, "mechanic", properties.getWorkers().getMechanics());
        addWorkers(workers, "vendor", properties.getWorkers().getVendors());
        return workers;
    }

    private void addWorkers(List<Worker> workers, String role, int amount) {
        for (int index = 0; index < amount; index++) {
            workers.add(new Worker(role.toUpperCase() + "-" + (index + 1), role));
        }
    }

    private List<Vehicle> createVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        for (int index = 0; index < properties.getVehicles().getSafariTrucks(); index++) {
            vehicles.add(new Vehicle("SAF-" + (index + 1), properties.getVehicles().getCapacity()));
        }
        return vehicles;
    }

    private Map<ZoneType, Zone> createZones() {
        Map<ZoneType, Zone> zones = new EnumMap<>(ZoneType.class);
        zones.put(ZoneType.ARRIVAL, new Zone(ZoneType.ARRIVAL, 120, 5));
        zones.put(ZoneType.CENTRAL_PLAZA, new Zone(ZoneType.CENTRAL_PLAZA, 160, 12));
        zones.put(ZoneType.BATHROOMS, new Zone(ZoneType.BATHROOMS, 25, 6));
        zones.put(ZoneType.ENERGY_PLANT, new Zone(ZoneType.ENERGY_PLANT, 12, 20));
        zones.put(ZoneType.OBSERVATION_ENCLOSURE, new Zone(ZoneType.OBSERVATION_ENCLOSURE, 90, 18));
        return zones;
    }

    private void processArrivals(ParkState state) {
        int arrivals = randomProvider.nextIntBetween(
                properties.getSimulation().getMinTouristsPerStep(),
                properties.getSimulation().getMaxTouristsPerStep());
        Zone arrivalZone = state.getZones().get(ZoneType.ARRIVAL);
        int admitted = arrivalZone.admit(arrivals);
        for (int index = 0; index < admitted; index++) {
            Tourist tourist = touristFactory.create(state.getProcessedTourists() + 1);
            ticketService.sellTicket(tourist, state.getSimulationStep());
            state.admitTourist(tourist);
        }
        arrivalZone.release(admitted);
    }

    private void processCentralPlaza(ParkState state) {
        if (state.getActiveTourists().isEmpty()) {
            return;
        }
        Zone centralPlaza = state.getZones().get(ZoneType.CENTRAL_PLAZA);
        int visitors = centralPlaza.admit(state.getActiveTourists().size());
        if (visitors > 0 && randomProvider.chance(0.65)) {
            BigDecimal amount = BigDecimal.valueOf((long) visitors * randomProvider.nextIntBetween(12, 35));
            revenueService.record(
                    RevenueType.SOUVENIR,
                    amount,
                    "Venta de souvenirs en recinto central",
                    state.getSimulationStep(),
                    visitors);
            state.changeSatisfactionForActiveTourists(4);
        }
        centralPlaza.release(visitors);
    }

    private void processBathrooms(ParkState state) {
        Zone bathrooms = state.getZones().get(ZoneType.BATHROOMS);
        int visitors = bathrooms.admit(Math.max(0, state.getActiveTourists().size() / 3));
        if (visitors > 0) {
            revenueService.record(
                    RevenueType.SERVICE,
                    BigDecimal.valueOf((long) visitors * 5),
                    "Servicios de banos con capacidad limitada",
                    state.getSimulationStep(),
                    visitors);
            state.changeSatisfactionForActiveTourists(2);
        }
        int waitingVisitors = Math.max(0, state.getActiveTourists().size() / 3 - visitors);
        if (waitingVisitors > 0) {
            state.changeSatisfactionForActiveTourists(-3);
        }
        bathrooms.release(visitors);
    }

    private void processObservationEnclosure(ParkState state) {
        if (state.getActiveTourists().isEmpty()) {
            return;
        }
        Zone observationZone = state.getZones().get(ZoneType.OBSERVATION_ENCLOSURE);
        int visitors = observationZone.admit(state.getActiveTourists().size());
        Vehicle vehicle = state.findAvailableVehicle().orElse(null);
        if (vehicle == null || visitors == 0) {
            state.changeSatisfactionForActiveTourists(-5);
            observationZone.release(visitors);
            return;
        }
        int passengers = vehicle.board(Math.min(visitors, vehicle.getCapacity()));
        revenueService.record(
                RevenueType.EXPERIENCE,
                BigDecimal.valueOf((long) passengers * 40),
                "Recorrido guiado por recintos de observacion",
                state.getSimulationStep(),
                passengers);
        state.consumeEnergy(observationZone.getEnergyDemand());
        state.changeSatisfactionForActiveTourists(state.countDinosaursInEnclosures() > 0 ? 7 : -4);
        vehicle.release();
        observationZone.release(visitors);
    }

    private void processEnergyPlant(ParkState state) {
        int consumption = properties.getEnergy().getConsumptionPerStep();
        state.consumeEnergy(consumption);
        expenseService.record(
                ExpenseType.ENERGY,
                BigDecimal.valueOf(consumption).multiply(new BigDecimal("1.50")),
                "Consumo operativo de energia del parque",
                state.getSimulationStep());
        if (state.getSimulationStep() % 3 == 0) {
            expenseService.record(
                    ExpenseType.MAINTENANCE,
                    properties.getEnergy().getMaintenanceCost(),
                    "Mantenimiento preventivo de planta de energia",
                    state.getSimulationStep());
            state.adjustEnergy(120);
        }
    }

    private void processDinosaurCare(ParkState state) {
        for (Dinosaur dinosaur : state.getDinosaurs()) {
            dinosaur.increaseHunger(randomProvider.nextIntBetween(3, 9));
            if (dinosaur.getHungerLevel() > 70) {
                dinosaur.feed(35);
                expenseService.record(
                        ExpenseType.MAINTENANCE,
                        new BigDecimal("30.00"),
                        "Alimentacion y control de " + dinosaur.getName(),
                        state.getSimulationStep());
            }
        }
    }

    private void releaseVisitors(ParkState state) {
        int activeVisitors = state.getActiveTourists().size();
        if (activeVisitors == 0) {
            return;
        }
        int departures = randomProvider.nextIntBetween(1, Math.max(1, activeVisitors / 2));
        state.releaseTourists(departures);
    }

    private void notifyObservers(ParkState state) {
        observers.forEach(observer -> observer.onStateChanged(state));
    }
}
