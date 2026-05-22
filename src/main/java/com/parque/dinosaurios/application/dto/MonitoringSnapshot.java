package com.parque.dinosaurios.application.dto;

import com.parque.dinosaurios.domain.model.ParkState;

public record MonitoringSnapshot(
        int simulationStep,
        int activeTourists,
        int processedTourists,
        long dinosaursInEnclosures,
        int energyAvailable,
        long activeEvents,
        long vehiclesInUse,
        double averageSatisfaction) {

    public static MonitoringSnapshot empty() {
        return new MonitoringSnapshot(0, 0, 0, 0, 0, 0, 0, 0.0);
    }

    public static MonitoringSnapshot from(ParkState state) {
        return new MonitoringSnapshot(
                state.getSimulationStep(),
                state.getActiveTourists().size(),
                state.getProcessedTourists(),
                state.countDinosaursInEnclosures(),
                state.getEnergyAvailable(),
                state.countActiveEvents(),
                state.countVehiclesInUse(),
                state.averageSatisfaction());
    }
}
