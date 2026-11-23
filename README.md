# crud-productos

Microservicio RESTful CRUD desarrollado con **Java 21**, **Spring Boot 4.2.0** y **MongoDB**, enfocado en la separación de contratos mediante DTOs, manejo global de excepciones, pruebas automatizadas con **JUnit 5 / Mockito**, medición de cobertura con **JaCoCo** y análisis estático con **SonarQube**.

## Arquitectura y características principales

- **Operaciones CRUD sobre MongoDB:** Gestión de productos (`POST`, `GET`, `PUT`, `DELETE`) sobre el endpoint `/api/productos`.
- **Separación con DTOs:** Uso de `ProductoRequest` y `ProductoResponse` para evitar exponer directamente la entidad persistente en los contratos de entrada y salida de la API.
- **Validaciones de entrada:** Validación de datos en las peticiones entrantes.
- **Manejo global de excepciones:** Excepciones de dominio (como `DatoDuplicadoException` y `RecursoNoEncontradoException`) interceptadas por un manejador global (`@RestControllerAdvice`) con respuestas JSON y códigos HTTP adecuados (`400`, `404`, `409`).
- **Calidad y cobertura:** Pruebas unitarias y de integración ejecutadas con Maven Wrapper (`.\mvnw`), reporte de cobertura con **JaCoCo** y análisis de calidad en **SonarQube**.

## Requisitos previos

- **Java 21**
- **MongoDB** en ejecución y accesible desde la configuración de la aplicación
- **SonarQube** en ejecución (por defecto en `http://localhost:9000` para análisis local)
- **Maven Wrapper** incluido en el repositorio (`.\mvnw` en Windows / `./mvnw` en Linux o macOS)

## Ejecución del microservicio

En la raíz del proyecto (`crud-productos`), inicia la aplicación con:

```powershell
.\mvnw spring-boot:run