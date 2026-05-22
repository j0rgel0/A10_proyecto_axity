# Checklist final del laboratorio

## Funcionalidad

- [x] Simulacion de parque turistico de dinosaurios
- [x] Registro o creacion de dinosaurios
- [x] Control de alimentacion y estado de dinosaurios
- [x] Creacion de trabajadores
- [x] Gestion de vehiculos
- [x] Gestion de visitantes
- [x] Registro de entrada de visitantes
- [x] Registro de salida de visitantes
- [x] Venta de entradas
- [x] Venta de souvenirs
- [x] Servicios adicionales de pago
- [x] Recintos de observacion
- [x] Planta de energia
- [x] Satisfaccion de visitantes
- [x] Eventos aleatorios
- [x] Eventos activos y resueltos segun corresponda
- [x] Reportes basicos

## Eventos

- [x] Escape de dinosaurio
- [x] Apagon masivo
- [x] Tormenta torrencial
- [x] Hora de ofertas
- [x] Falla de vehiculos

## Tecnico

- [x] Java 17
- [x] Maven
- [x] Spring Boot
- [x] Docker
- [x] PostgreSQL
- [x] Liquibase
- [x] Configuracion YAML
- [x] Minimo dos patrones de diseno
- [x] Pruebas unitarias
- [x] Mockito donde aplica
- [x] Cobertura minima 65%
- [x] Diagramas UML o flujo
- [x] README obligatorio
- [x] CI con GitHub Actions

## Persistencia

- [x] Ingresos por boletos
- [x] Ingresos por souvenirs
- [x] Ingresos por servicios
- [x] Ingresos por experiencias
- [x] Gastos de mantenimiento
- [x] Gastos de energia
- [x] Gastos de reparaciones
- [x] Otros costos asociados
- [x] Eventos generados

## Monitoreo

- [x] Turistas activos
- [x] Dinosaurios en recintos
- [x] Energia disponible
- [x] Eventos activos
- [x] Vehiculos en uso

## Validacion

- [x] `.\mvnw.cmd clean verify`
- [x] `docker compose up --build -d`
- [x] Prueba manual de endpoints

## Git

- [x] Trabajo directo en `main` por instruccion del usuario
- [x] Commits incrementales en espanol y una sola linea
- [x] Sin ramas `develop` ni PR por decision explicita del experimento
- [x] Entrega final sincronizada con `origin/main`
- [x] Tag final `v1.0.0`

## Evidencia de ejecucion Docker

- `docker compose ps`: `dino-app` arriba en `8080` y `dino-postgres` saludable en `5433`.
- `POST /api/simulation/run`: proceso 49 visitantes en 3 pasos.
- `GET /api/reports/summary`: total de ingresos `7166.50`, gastos `702.00`, utilidad neta `6464.50`.
- Validacion directa en PostgreSQL: ingresos, gastos y eventos persistidos.
