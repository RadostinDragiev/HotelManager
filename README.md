# HotelManager

HotelManager is a Spring Boot–based backend application for managing hotel operations such as rooms, reservations, payments, consumptions, users, tasks, and more.  
It is developed as a SoftUni Spring Advanced exam project and is designed around a modular, API-first architecture with proper security, monitoring, and CI/CD in mind. :contentReference[oaicite:0]{index=0}

---

## Table of Contents

- [Tech Stack](#tech-stack)
  - [Backend](#backend)
  - [Data Layer](#data-layer)
  - [Monitoring & Observability](#monitoring--observability)
  - [DevOps & CI/CD](#devops--cicd)
  - [Developer Productivity](#developer-productivity)
- [Features & Functionality](#features--functionality)
  - [Room Management](#room-management)
  - [Reservation Management](#reservation-management)
  - [Reservation Requests](#reservation-requests)
  - [Payments](#payments)
  - [Consumptions & Services](#consumptions--services)
  - [User & Access Management](#user--access-management)
  - [Files & Documents](#files--documents)
  - [Email](#email)
  - [Tasks](#tasks)
  - [Exports](#exports)
  - [Error Handling & Validation](#error-handling--validation)
- [Integrations](#integrations)
  - [Databases](#databases)
  - [Monitoring & Dashboards](#monitoring--dashboards)
  - [Containerization & Deployment](#containerization--deployment)
  - [Code Quality & CI/CD](#code-quality--cicd)
- [Project Structure & Branching](#project-structure--branching)
- [Getting Started](#getting-started)
- [Development Workflow](#development-workflow)

---

## Tech Stack

### Backend

- **Language:** Java 21 :contentReference[oaicite:1]{index=1}  
- **Framework:** Spring Boot
  - Spring Web (REST APIs)
  - **Spring Data JPA** – ORM and repository abstraction
  - **Spring Security** – authentication, authorization & endpoint protection
  - **Spring HATEOAS** – hypermedia-driven REST responses
  - **Spring Actuator** – health checks & operational endpoints :contentReference[oaicite:2]{index=2}
- **Object Mapping:** ModelMapper – DTO ↔ entity mapping
- **Boilerplate Reduction:** Lombok – annotations for getters, setters, builders, etc. :contentReference[oaicite:3]{index=3}

### Data Layer

- **Primary Database:** MariaDB – production data storage :contentReference[oaicite:4]{index=4}  
- **Test Database:** HSQLDB – in-memory database used for tests :contentReference[oaicite:5]{index=5}  
- **Persistence:** JPA/Hibernate via Spring Data JPA

### Monitoring & Observability

- **Spring Actuator** for health, metrics, and info endpoints.
- **Prometheus** for metrics scraping and collection. :contentReference[oaicite:6]{index=6}  
- **Grafana** for dashboards and visualizing application and system metrics. :contentReference[oaicite:7]{index=7}  

### DevOps & CI/CD

- **Docker** – containerization of the application (with versioned images for the `main` branch). :contentReference[oaicite:8]{index=8}  
- **Render** – deployment target for the `main` branch. :contentReference[oaicite:9]{index=9}  
- **GitHub Actions** – CI/CD pipeline:
  - Compile on every commit to `develop`
  - Run tests on every merge request
  - On push to `main`: build, run tests, and deploy to Render :contentReference[oaicite:10]{index=10}  
- **SonarQube** – static code analysis and code quality gate. :contentReference[oaicite:11]{index=11}  

### Developer Productivity

- **Spring DevTools** – hot reload and development-time conveniences.
- Standard IDE support for Java/Spring projects.
- GitHub Projects & Issues for backlog and story tracking. :contentReference[oaicite:12]{index=12}  

---

## Features & Functionality

Business functionality is organized into “epics” (modules) focusing on a specific domain of hotel management. :contentReference[oaicite:13]{index=13}  

### Room Management

Manage hotel rooms and their lifecycle:

- **Create room** – register new rooms with capacity, type, and pricing.
- **Read room** – retrieve room details and availability.
- **Update room** – change room properties, status, or pricing.
- **Delete room** – remove or logically deactivate rooms (depending on business rules). :contentReference[oaicite:14]{index=14}  

### Reservation Management

Handle end-to-end reservation workflows:

- **Create reservation** – book rooms for guests for given dates.
- **Read reservation** – list and view reservation details.
- **Update reservation** – modify dates, room allocation, or guest details.
- **Soft delete reservation** – cancel or logically delete while preserving history. :contentReference[oaicite:15]{index=15}  

### Reservation Requests

Support pre-reservation “request” flows (e.g., from a public-facing channel or call center):

- **Create reservation request**
- **Read reservation request**
- **Update reservation request** :contentReference[oaicite:16]{index=16}  

This allows staff to convert requests into confirmed reservations later.

### Payments

Track financial operations related to reservations or stays:

- **Create payment** – register payments linked to reservations/guests.
- **Read payment** – view payment history and details.
- **Update payment** – adjust payment records as needed (e.g., corrections). :contentReference[oaicite:17]{index=17}  

### Consumptions & Services

Record guest consumptions during their stay (e.g., minibar, restaurant, spa):

- **Create consumption**
- **Read consumption**
- **Update consumption**
- **Delete consumption** :contentReference[oaicite:18]{index=18}  

These can be associated with reservations and included in final billing.

### User & Access Management

Manage hotel staff and system users:

- **Create user**
- **Read user**
- **Update user**
- **Soft delete / deactivate user** :contentReference[oaicite:19]{index=19}  

Combined with **Spring Security**, this supports role-based access control and secure access to the API.

### Files & Documents

Attach and manage files related to reservations, guests, or internal processes:

- **Create file** – upload and store files (e.g., ID scans, invoices).
- **Read file** – retrieve/download files.
- **Delete file** – remove obsolete or sensitive documents. :contentReference[oaicite:20]{index=20}  

### Email

Basic email capabilities for guest communications or internal notifications:

- **Send email** – trigger outbound emails (e.g., confirmations, reminders).
- **Read email** – track or log sent messages. :contentReference[oaicite:21]{index=21}  

### Tasks

Internal task management for hotel staff:

- **Create task**
- **Read task**
- **Update task**
- **Delete task** :contentReference[oaicite:22]{index=22}  

Useful for housekeeping, maintenance, and front-desk workflows.

### Exports

Support exporting data for reporting or accounting:

- **Create export** – generate export files/datasets (e.g., CSV, reports). :contentReference[oaicite:23]{index=23}  

### Error Handling & Validation

- Centralized **exception handler** for REST APIs, returning consistent error responses.
- Standardized validation errors to improve API consumer experience. :contentReference[oaicite:24]{index=24}  

---

## Integrations

### Databases

- **MariaDB**
  - Primary relational database for production usage.
  - Stores all core hotel entities (rooms, reservations, payments, users, etc.). :contentReference[oaicite:25]{index=25}  

- **HSQLDB**
  - In-memory database used for automated tests, enabling fast and isolated test runs. :contentReference[oaicite:26]{index=26}  

### Monitoring & Dashboards

- **Spring Actuator**
  - exposes health, metrics, and info endpoints used by monitoring systems.

- **Prometheus**
  - scrapes metrics from the application (Actuator /metrics endpoints). :contentReference[oaicite:27]{index=27}  

- **Grafana**
  - visualizes metrics on dashboards for operational monitoring and performance tracking. :contentReference[oaicite:28]{index=28}  

### Containerization & Deployment

- **Docker**
  - Application is packaged as a Docker image.
  - Versioning strategy for images built from the `main` branch (to be pushed to DockerHub or JFrog). :contentReference[oaicite:29]{index=29}  

- **Render**
  - Deployed as a service on Render on every successful push to `main` (via GitHub Actions pipeline). :contentReference[oaicite:30]{index=30}  

### Code Quality & CI/CD

- **GitHub Actions**
  - **On every commit to `develop`:** compile the project.
  - **On every merge request:** run the test suite.
  - **On every push to `main`:** build the project, run tests, and deploy to Render. :contentReference[oaicite:31]{index=31}  

- **SonarQube**
  - Used to analyze code quality (coverage, smells, vulnerabilities) and enforce quality gates. :contentReference[oaicite:32]{index=32}  

---

## Project Structure & Branching

Branching rules (as described in the project wiki): :contentReference[oaicite:33]{index=33}  

- **`main`**
  - Stable branch.
  - Deployed automatically to Render.
  - Docker images are versioned and published from this branch.

- **`develop`**
  - Active development of new features.
  - CI runs compilation (and possibly tests) on every commit.

- **`test`**
  - Used for weekly integration or regression testing.

GitHub Projects and Issues are used to track epics and user stories (e.g., Room, Payment, Reservation, etc.).

---

## Getting Started

> Note: Commands and configuration below may need to be adjusted depending on your local environment.

### Prerequisites

- Java 21
- Maven
- Docker (optional but recommended)
- Access to a MariaDB instance (local or remote)

### Local Run (without Docker)

1. **Configure Database**

   Set up a MariaDB database and configure the connection properties (e.g. in `application.properties` or `application.yml`):

   ```properties
   spring.datasource.url=jdbc:mariadb://localhost:3306/hotel_manager
   spring.datasource.username=your_user
   spring.datasource.password=your_password
