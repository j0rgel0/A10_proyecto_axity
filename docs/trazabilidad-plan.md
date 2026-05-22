# Trazabilidad contra el plan maestro

Este documento cruza los criterios globales del plan maestro con la evidencia implementada en el repositorio.

## Resultado de auditoria

No se detectaron faltantes funcionales mayores contra el plan. Los ajustes pendientes eran de cierre documental y metadatos:

- Se completo la trazabilidad requisito-evidencia.
- Se amplio el checklist final con los criterios globales del plan.
- Se completo el metadato Maven de developer como `A10 Jorge Lopez`.
- Se deja documentado que el flujo Git se hizo directo en `main` por instruccion del usuario.
- Se prepara el tag final `v1.0.0` sobre el ultimo commit de entrega.

## Funcionalidad

| Requisito | Estado | Evidencia |
|---|---|---|
| Simulacion de parque turistico de dinosaurios | Hecho | `SimulationEngine` |
| Turistas | Hecho | `Tourist`, `TouristFactory` |
| Dinosaurios | Hecho | `Dinosaur`, `DinosaurFactory` |
| Trabajadores | Hecho | `Worker`, `ParkProperties.workers` |
| Zonas | Hecho | `Zone`, `ZoneType`, zonas creadas en `SimulationEngine` |
| Vehiculos | Hecho | `Vehicle`, uso en recintos de observacion |
| Entrada de turistas | Hecho | `processArrivals` y venta de boletos |
| Salida de turistas | Hecho | `releaseVisitors` y `ParkState.releaseTourists` |
| Venta de boletos | Hecho | `TicketService` |
| Venta de souvenirs | Hecho | `processCentralPlaza` |
| Servicios adicionales de pago | Hecho | `processBathrooms` |
| Recintos de observacion | Hecho | `processObservationEnclosure` |
| Planta de energia | Hecho | `processEnergyPlant` |
| Satisfaccion de visitantes | Hecho | `Tourist.changeSatisfaction`, `MonitoringSnapshot.averageSatisfaction` |
| Eventos aleatorios | Hecho | Estrategias bajo `simulation/event` |
| Eventos activos y resueltos | Hecho | `EventStatus`, `ParkReport.activeEvents`, `ParkReport.resolvedEvents` |
| Reportes basicos | Hecho | `ReportService`, `ReportController` |

## Eventos

| Evento requerido | Estado | Evidencia |
|---|---|---|
| Escape de dinosaurio | Hecho | `DinosaurEscapeStrategy` |
| Apagon masivo | Hecho | `BlackoutStrategy` |
| Tormenta torrencial | Hecho | `StormStrategy` |
| Hora de ofertas | Hecho | `OfferHourStrategy` |
| Falla de vehiculos | Hecho | `VehicleFailureStrategy` |

## Monitoreo

| Requisito | Estado | Evidencia |
|---|---|---|
| Turistas activos | Hecho | `MonitoringSnapshot.activeTourists` |
| Dinosaurios en recintos | Hecho | `MonitoringSnapshot.dinosaursInEnclosures` |
| Energia disponible | Hecho | `MonitoringSnapshot.energyAvailable` |
| Eventos activos | Hecho | `MonitoringSnapshot.activeEvents` |
| Vehiculos en uso | Hecho | `MonitoringSnapshot.vehiclesInUse` |

## Persistencia

| Requisito | Estado | Evidencia |
|---|---|---|
| Ingresos por boletos | Hecho | `RevenueType.TICKET`, `revenues` |
| Ingresos por souvenirs | Hecho | `RevenueType.SOUVENIR`, `revenues` |
| Ingresos por servicios | Hecho | `RevenueType.SERVICE`, `revenues` |
| Ingresos por experiencias | Hecho | `RevenueType.EXPERIENCE`, `revenues` |
| Gastos de mantenimiento | Hecho | `ExpenseType.MAINTENANCE`, `operational_expenses` |
| Gastos de energia | Hecho | `ExpenseType.ENERGY`, `operational_expenses` |
| Gastos de reparaciones | Hecho | `ExpenseType.REPAIR`, `operational_expenses` |
| Otros costos asociados | Hecho | `ExpenseType.EVENT_RESPONSE`, `operational_expenses` |
| Eventos generados | Hecho | `park_events` |

## Tecnico

| Requisito | Estado | Evidencia |
|---|---|---|
| Java 17 | Hecho | `pom.xml` |
| Spring Boot | Hecho | Spring Boot 3.5.0 |
| Maven | Hecho | Maven Wrapper |
| Docker | Hecho | `Dockerfile`, `docker-compose.yml` |
| PostgreSQL | Hecho | Servicio `db` en Docker Compose |
| Liquibase | Hecho | `db/changelog` |
| Configuracion externa YAML | Hecho | `application.yml`, `ParkProperties` |
| Minimo dos patrones de diseno | Hecho | Factory, Strategy, Observer |
| JUnit | Hecho | `src/test/java` |
| Mockito | Hecho | Pruebas de servicios con mocks |
| Cobertura minima 65% | Hecho | JaCoCo en `pom.xml` |
| Diagramas | Hecho | `docs/diagramas` |
| README completo | Hecho | `README.md` |
| CI | Hecho | `.github/workflows/ci.yml` |

## Git

El plan maestro proponia `develop`, ramas por tarea, release branch y merge final a `main`. Para este experimento el usuario pidio explicitamente trabajar todo directo en `main`, sin PR ni ramas, por lo que la evidencia Git se adapto asi:

| Criterio adaptado | Estado | Evidencia |
|---|---|---|
| Trabajo directo en `main` | Hecho | Instruccion explicita del usuario |
| Commits pequenos | Hecho | Historial con commits incrementales |
| Mensajes en espanol y una linea | Hecho | `git log --oneline` |
| Entrega final en `main` | Hecho | `origin/main` sincronizado |
| Tag final | Hecho | `v1.0.0` |
