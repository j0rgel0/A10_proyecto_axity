# Decisiones tecnicas

## Java 17 y Spring Boot

El proyecto usa Java 17 como version objetivo mediante `java.version` en Maven. Aunque la maquina local tenga un JDK superior, el compilador genera bytecode compatible con Java 17.

## Maven Wrapper

Se incluye Maven Wrapper para que el proyecto no dependa de una instalacion global de Maven. El archivo `.mvn/maven.config` usa un repositorio local dentro del workspace para evitar problemas de permisos con el cache global.

## PostgreSQL y H2

PostgreSQL es la base operativa, levantada por Docker Compose. H2 se usa solo en pruebas, en modo compatible con PostgreSQL, para mantener el ciclo rapido y reproducible.

Docker publica PostgreSQL en el puerto local `5433` por default para no chocar con instalaciones locales en `5432`; internamente la aplicacion usa `db:5432`.

## Liquibase

La estructura de ingresos, gastos y eventos se crea por migraciones versionadas. Los changesets usan `A10 Jorge Lopez` como autor.

## Arquitectura

La aplicacion separa dominio, servicios de aplicacion, infraestructura y simulacion. La API REST queda delgada y delega en servicios.

## Patrones

Se usan Factory, Strategy y Observer porque encajan con el problema: creacion controlada de actores, eventos intercambiables y monitoreo del estado del parque.
