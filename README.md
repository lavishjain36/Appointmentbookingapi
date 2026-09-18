# Appointment Booking API

Minimal Java 21, Spring Boot 4, Maven, MySQL backend for a live coding session.

## Stack

- Java 21 and Spring Boot 4
- Spring Web, Spring Data JPA, Spring Security HTTP Basic
- Jakarta Validation, SpringDoc OpenAPI, Lombok
- MySQL database `appointmentdb`

## Run

1. Create or allow the `appointmentdb` database in MySQL.
2. Set `spring.datasource.username` and `spring.datasource.password` in `src/main/resources/application.properties`.
3. Start the API:

```text
mvn spring-boot:run
```

The API runs on `http://localhost:8080`. Hibernate creates or updates the two tables with `spring.jpa.hibernate.ddl-auto=update`.

## Swagger

Open `http://localhost:8080/swagger-ui/index.html`.

Use the Authorize button with one of these demo accounts. OpenAPI JSON is available at `/v3/api-docs`.

```text
ADMIN: admin@example.com / admin123
USER:  user@example.com  / user123
```

The accounts are in-memory for authentication. Matching `User` rows are seeded in MySQL at startup so booked appointments retain their user relationship.

## Request examples

Create a slot as the admin account:

```json
POST /api/appointments
{
  "doctorName": "Dr. Sharma",
  "appointmentDate": "2026-09-20",
  "startTime": "10:00",
  "endTime": "10:30"
}
```

A new slot returns `status: AVAILABLE`.

Authorize as the user account, then find slots:

```text
GET /api/appointments/available?doctorName=Sharma&appointmentDate=2026-09-20&page=0&size=5
```

Book a slot:

```text
POST /api/appointments/{id}/book
```

The first user changes the slot to `BOOKED`. A second booking attempt returns `409 Conflict` with `Appointment slot is already booked`. The booking transaction locks the slot row while checking and saving it.

View and cancel the logged-in user's appointments:

```text
GET /api/appointments/my?page=0&size=5
PUT /api/appointments/{id}/cancel
```

Admin listing is paginated:

```text
GET /api/appointments?page=0&size=5
```

## Swagger demonstration flow

1. Authorize Swagger with `admin@example.com` / `admin123`.
2. Create a slot and view the admin appointment list.
3. Replace authorization with `user@example.com` / `user123`.
4. Find available slots and book one.
5. Repeat the booking to see `409 Conflict`.
6. View `/api/appointments/my`, then cancel the appointment.
7. Try the admin list or create endpoint as the user to see `403 Forbidden`.

There are no registration or login APIs. Appointment endpoints are protected by HTTP Basic and role as described in the API request mappings.
