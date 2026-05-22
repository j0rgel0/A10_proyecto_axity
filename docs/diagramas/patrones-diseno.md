# Patrones de diseno

```mermaid
flowchart LR
    subgraph Factory
        A[DinosaurFactory] --> B[Dinosaur]
        C[TouristFactory] --> D[Tourist]
    end

    subgraph Strategy
        E[EventService] --> F[ParkEventStrategy]
        F --> G[DinosaurEscapeStrategy]
        F --> H[BlackoutStrategy]
        F --> I[StormStrategy]
        F --> J[OfferHourStrategy]
        F --> K[VehicleFailureStrategy]
    end

    subgraph Observer
        L[SimulationEngine] --> M[ParkStateObserver]
        M --> N[MonitoringService]
    end
```

## Justificacion

- Factory evita duplicar reglas de creacion de turistas y dinosaurios.
- Strategy permite agregar nuevos eventos sin modificar el motor de simulacion.
- Observer desacopla el monitoreo del procesamiento de pasos.
