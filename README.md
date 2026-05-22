# Parque Turistico de Dinosaurios

Simulacion secuencial de un parque turistico de dinosaurios construida con Java 17, Spring Boot, Maven, PostgreSQL, Docker, Liquibase, JUnit 5, Mockito y JaCoCo.

El sistema modela turistas, dinosaurios, trabajadores, zonas, vehiculos, ingresos, gastos operativos, eventos aleatorios, monitoreo y reportes.

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

## Flujo De Simulacion

1. Se inicializa el parque con dinosaurios, trabajadores, vehiculos y zonas.
2. En cada paso entran turistas de forma no determinista.
3. Se venden boletos y se registran ingresos.
4. Los turistas pasan por recinto central, banos y recintos de observacion.
5. La planta de energia consume unidades y genera gastos.
6. Los dinosaurios incrementan hambre y se alimentan si hace falta.
7. Puede ocurrir un evento aleatorio.
8. Se actualiza el monitoreo y al final se genera el reporte.

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
- [x] Zonas del parque
- [x] Eventos aleatorios
- [x] Monitoreo
- [x] Persistencia de ingresos
- [x] Persistencia de gastos
- [x] Persistencia de eventos
- [x] JUnit 5
- [x] Mockito
- [x] Cobertura minima 65%
- [x] Diagramas
- [x] README completo
