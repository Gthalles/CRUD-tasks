# Task API

Simple REST API for creating, updating, listing and deleting tasks. Built with Spring Boot + JPA. Default runtime uses MySQL; tests use an in-memory H2 database.

## Features
- CRUD for tasks (`description`, `finished`).
- Validation with Jakarta Bean Validation (DTO) and explicit business rules in the entity.
- Global error handling for validation errors, bad input and missing records.
- OpenAPI docs via springdoc (Swagger UI at `/swagger-ui.html`).
- `test` profile using H2 (useful for running locally without MySQL as well).

## Tech Stack
- Java 17
- Spring Boot 3.3.5
- springdoc-openapi (Swagger UI)
- MySQL 8.4 (default), H2 (test profile)
- Maven Wrapper (`./mvnw`)

## Requirements
- JDK 17+
- Docker (optional, for local MySQL)

## Getting Started

### Run with MySQL (default)
1. Start MySQL with Docker Compose:
   - `docker compose up -d mysql`
   - Compose file: [`docker-compose.yaml`](docker-compose.yaml)
2. Check DB config:
   - MySQL runtime config: [`src/main/resources/application.yaml`](src/main/resources/application.yaml)
3. Run the app:
   - `./mvnw spring-boot:run`
4. Base URL:
   - `http://localhost:8080`
5. Swagger UI:
   - `http://localhost:8080/swagger-ui.html` (OpenAPI JSON at `http://localhost:8080/v3/api-docs`)

### Run with H2 (no MySQL)
Run the application with the `test` profile:
```bash
SPRING_PROFILES_ACTIVE=test ./mvnw spring-boot:run
```

Profile config:
- [`src/test/resources/application-test.yaml`](src/test/resources/application-test.yaml)

## Configuration

### MySQL (default)
See [`src/main/resources/application.yaml`](src/main/resources/application.yaml):
- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`
- `spring.jpa.hibernate.ddl-auto`

### Docker Compose MySQL
From [`docker-compose.yaml`](docker-compose.yaml):
- DB: `task_db`
- User/password: `task_user` / `task_pass`
- Root password: `task_pass`

## API
Endpoints use JSON and are available under `/tasks`:
- GET `/tasks` list all tasks (sorted by description).
- GET `/tasks/{id}` get a single task.
- POST `/tasks` create a task.
- PATCH `/tasks/{id}` update a task.
- DELETE `/tasks/{id}` delete a task.

### Payloads
Create (`POST /tasks`):
```json
{ "description": "Write README" }
```

Update (`PATCH /tasks/{id}`):
```json
{ "description": "Reviewed docs", "finished": true }
```

Note: the current implementation expects both `description` and `finished` on update (see controller/service at
[`src/main/java/com/thallesgarbelotti/task/controller/TaskController.java`](src/main/java/com/thallesgarbelotti/task/controller/TaskController.java) and
[`src/main/java/com/thallesgarbelotti/task/service/TaskService.java`](src/main/java/com/thallesgarbelotti/task/service/TaskService.java)).

### cURL examples
There is a dedicated file with ready-to-run commands:
- [`curl.md`](curl.md)

## Tests
Run tests:
```bash
./mvnw test
```

Tests use the `test` profile (H2) configured in
[`src/test/resources/application-test.yaml`](src/test/resources/application-test.yaml).

## Project Layout
- Application code: [`src/main/java/com/thallesgarbelotti/task`](src/main/java/com/thallesgarbelotti/task)
- Runtime config: [`src/main/resources/application.yaml`](src/main/resources/application.yaml)
- Tests: [`src/test/java/com/thallesgarbelotti/task`](src/test/java/com/thallesgarbelotti/task)
- Test profile config: [`src/test/resources/application-test.yaml`](src/test/resources/application-test.yaml)
- Local MySQL: [`docker-compose.yaml`](docker-compose.yaml)

## Logging & Debug
SQL logging is enabled (see `logging.level.org.hibernate.SQL` in
[`src/main/resources/application.yaml`](src/main/resources/application.yaml)). Uncomment
`org.hibernate.orm.jdbc.bind` to inspect parameter binding when needed.
