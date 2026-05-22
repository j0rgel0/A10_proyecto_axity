package com.parque.dinosaurios.simulation.event;

import com.parque.dinosaurios.domain.enums.EventStatus;
import com.parque.dinosaurios.domain.enums.EventType;
import com.parque.dinosaurios.domain.model.ParkEvent;
import com.parque.dinosaurios.domain.model.ParkState;
import com.parque.dinosaurios.domain.model.Vehicle;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class VehicleFailureStrategy implements ParkEventStrategy {

    @Override
    public EventType type() {
        return EventType.VEHICLE_FAILURE;
    }

    @Override
    public ParkEvent execute(ParkState state) {
        LocalDateTime now = LocalDateTime.now();
        return state.findAvailableVehicle()
                .map(vehicle -> failVehicle(state, vehicle, now))
                .orElseGet(() -> new ParkEvent(
                        null,
                        type(),
                        EventStatus.RESOLVED,
                        "Revision sin vehiculos disponibles para marcar falla",
                        1,
                        0,
                        0,
                        state.getSimulationStep(),
                        now,
                        now));
    }

    private ParkEvent failVehicle(ParkState state, Vehicle vehicle, LocalDateTime now) {
        vehicle.fail();
        state.changeSatisfactionForActiveTourists(-7);
        return new ParkEvent(
                null,
                type(),
                EventStatus.ACTIVE,
                "Falla mecanica del vehiculo " + vehicle.getId(),
                3,
                0,
                -7,
                state.getSimulationStep(),
                now,
                null);
    }
}
