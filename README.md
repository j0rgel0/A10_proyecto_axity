# Parque Turistico de Dinosaurios

Simulacion secuencial de un parque turistico de dinosaurios construida con Java 17, Spring Boot, Maven, PostgreSQL, Docker, Liquibase, JUnit 5, Mockito y JaCoCo.

El sistema modela turistas, dinosaurios, trabajadores, zonas, vehiculos, ingresos, gastos operativos, eventos aleatorios, monitoreo y reportes.

Responsable tecnico: A10 Jorge Lopez.

## Herramientas

- Java 17
- Spring Boot 3.5.0
- Maven Wrapper
- PostgreSQL 16
- Docker y Docker Compose
- Liquibase
- Spring Data JPA
- JUnit 5
- Mockito
- JaCoCo

## Configuracion

La configuracion principal vive en `src/main/resources/application.yml`.

Variables relevantes:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `park.simulation.steps`
- `park.simulation.min-tourists-per-step`
- `park.simulation.max-tourists-per-step`
- `park.tickets.base-price`
- `park.events.base-probability`
- `park.energy.initial-units`

Los cambios de base de datos estan en `src/main/resources/db/changelog/` y usan `A10 Jorge Lopez` como autor de los changesets.

## Ejecucion Local

Levanta PostgreSQL local con base `dinopark`, usuario `dinouser` y password `dinopass`, o exporta las variables de conexion.

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

## Ejecucion Con Docker

```bash
docker compose up --build
```

La API queda disponible en:

```text
http://localhost:8080
```

PostgreSQL queda publicado en `localhost:5433` por default para evitar choques con instalaciones locales. Dentro de Docker la aplicacion usa `db:5432`.

## API

Ejecutar simulacion:

```bash
curl -X POST http://localhost:8080/api/simulation/run -H "Content-Type: application/json" -d "{\"steps\": 5}"
```

Consultar monitoreo:

```bash
curl http://localhost:8080/api/simulation/status
```

Reportes:

```bash
curl http://localhost:8080/api/reports/revenues
curl http://localhost:8080/api/reports/expenses
curl http://localhost:8080/api/reports/events
curl http://localhost:8080/api/reports/summary
```

## Pruebas Y Cobertura

Windows:

```powershell
.\mvnw.cmd clean verify
```

Linux/macOS:

```bash
./mvnw clean verify
```

JaCoCo falla el build si la cobertura de lineas baja de 65%. El reporte se genera en:

```text
target/site/jacoco/index.html
```

### Mapa De Pruebas Automatizadas

La suite actual ejecuta 30 pruebas y esta organizada por capa para validar reglas de negocio, persistencia y superficie REST:

- `DomainModelTest`: reglas de turista, dinosaurio, vehiculo y zona.
- `FactoryTest`: creacion inicial de turistas y dinosaurios desde configuracion.
- `EventStrategyTest`: comportamiento de apagon, tormenta, ofertas, escape y falla de vehiculos.
- `FinanceServiceTest`: registro y totalizacion de ingresos, gastos y boletos.
- `EventServiceTest`: seleccion, persistencia y gasto operativo generado por eventos.
- `MonitoringServiceTest`: snapshot operativo con turistas activos, energia, eventos y vehiculos.
- `ReportServiceTest`: resumen financiero y operativo con utilidad, eventos y satisfaccion.
- `SimulationServiceTest`: delegacion al motor, estado actual y ultimo resultado.
- `SimulationControllerTest` y `ReportControllerTest`: contrato basico de endpoints REST.
- `SimulationIntegrationTest`: ejecucion completa con Spring Boot, H2 en modo PostgreSQL y Liquibase.

Las pruebas de integracion usan `src/test/resources/application-test.yml` con H2 en modo compatible con PostgreSQL para mantener el ciclo rapido sin depender de Docker.

## CI/CD

El workflow de GitHub Actions vive en `.github/workflows/ci.yml` y se ejecuta en cada push a `main` y en pull requests. El pipeline:

1. Clona el repositorio.
2. Configura Java 17 con Temurin.
3. Asegura permiso de ejecucion para `mvnw` con `chmod +x mvnw`.
4. Ejecuta `./mvnw -B clean verify`.

El archivo `mvnw` tambien esta marcado en Git con modo ejecutable para que Linux pueda correrlo directamente.

## Flujo De Simulacion

1. Se inicializa el parque con dinosaurios, trabajadores, vehiculos y zonas.
2. En cada paso entran turistas de forma no determinista.
3. Se venden boletos y se registran ingresos.
4. Los turistas pasan por recinto central, banos y recintos de observacion.
5. La planta de energia consume unidades y genera gastos.
6. Los dinosaurios incrementan hambre y se alimentan si hace falta.
7. Puede ocurrir un evento aleatorio.
8. Salen visitantes activos de forma no determinista.
9. Se actualiza el monitoreo y al final se genera el reporte.

## Zonas

- Lugar de arribo de turistas.
- Recinto central para souvenirs.
- Banos con capacidad limitada.
- Planta de energia.
- Recintos de observacion.

## Eventos Aleatorios

- Escape de dinosaurio.
- Apagon masivo.
- Tormenta torrencial.
- Hora de ofertas.
- Falla de vehiculos.

## Persistencia

Se persisten:

- Ingresos por boletos.
- Ingresos por souvenirs.
- Ingresos por servicios.
- Ingresos por experiencias.
- Gastos de energia.
- Gastos de mantenimiento.
- Gastos de reparacion.
- Gastos de respuesta a eventos.
- Eventos generados.

## Reportes

Los reportes incluyen:

- Total de ingresos, gastos y utilidad neta.
- Ventas por tipo: boletos, souvenirs, servicios y experiencias.
- Gastos por tipo: energia, mantenimiento, reparacion y respuesta a eventos.
- Eventos por tipo, activos y resueltos.
- Satisfaccion promedio, visitantes procesados, energia final y vehiculos en uso.

## Patrones De Diseno

### Factory

`DinosaurFactory` y `TouristFactory` centralizan la creacion de objetos iniciales desde configuracion y reglas controladas.

### Strategy

Cada evento aleatorio implementa `ParkEventStrategy`, por lo que la logica de apagon, tormenta, ofertas, escape y falla de vehiculos queda aislada y extensible.

### Observer

`MonitoringService` implementa `ParkStateObserver` y recibe notificaciones del `SimulationEngine` para actualizar el snapshot operativo.

## Diagramas

- [Flujo de simulacion](docs/diagramas/flujo-simulacion.md)
- [Secuencia de eventos](docs/diagramas/secuencia-eventos.md)
- [Patrones de diseno](docs/diagramas/patrones-diseno.md)
- [Modelo de entidades](docs/diagramas/modelo-entidades.md)

## Estructura

```text
src/main/java/com/parque/dinosaurios
|-- application
|   |-- dto
|   `-- service
|-- config
|-- domain
|   |-- enums
|   `-- model
|-- infrastructure
|   |-- cli
|   |-- persistence
|   `-- web
`-- simulation
    |-- event
    |-- factory
    `-- observer
```

## Checklist

- [x] Java 17
- [x] Maven
- [x] Spring Boot
- [x] Docker
- [x] PostgreSQL
- [x] Liquibase
- [x] Configuracion YAML
- [x] Simulacion no determinista
- [x] Turistas, dinosaurios, trabajadores, zonas y vehiculos
- [x] Entrada y salida de visitantes
- [x] Zonas del parque
- [x] Eventos aleatorios
- [x] Monitoreo
- [x] Satisfaccion de visitantes
- [x] Persistencia de ingresos
- [x] Persistencia de gastos
- [x] Persistencia de eventos
- [x] JUnit 5
- [x] Mockito
- [x] Cobertura minima 65%
- [x] Diagramas
- [x] README completo
