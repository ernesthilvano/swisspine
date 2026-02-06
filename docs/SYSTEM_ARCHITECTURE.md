# System Architecture & Technology Stack
## SwissPine Connection Planner

This document provides a comprehensive overview of the technical architecture, technology stack, and component design of the SwissPine Connection Planner application.

## 1. Technology Stack

### Frontend (User Interface)
| Category | Technology | Version | Purpose |
|----------|------------|---------|---------|
| **Framework** | **Angular** | v17.x | Robust, component-based SPA framework |
| **UI Library** | **Angular Material** | v17.x | Material Design components for consistent UX |
| **State Mgt** | **RxJS** | v7.8.0 | Reactive extensions for asynchronous data handling |
| **Styling** | **CSS3** | - | Custom responsive grid layouts and theming |
| **Build Tool** | **Angular CLI** | - | Development server, build optimization, and testing |

### Backend (API & Business Logic)
| Category | Technology | Version | Purpose |
|----------|------------|---------|---------|
| **Language** | **Java** | 17 (LTS) | Robust, typed, object-oriented language |
| **Framework** | **Spring Boot** | 3.2.2 | Rapid application development, DI, and auto-config |
| **ORM** | **Spring Data JPA** | - | Abstraction over Hibernate for database interactions |
| **Validation** | **Hibernate Validator** | - | Bean validation for data integrity |
| **Monitoring** | **Actuator & Micrometer**| - | Health checks, metrics, and Prometheus integration |
| **Build Tool** | **Maven** | - | Dependency management and build lifecycle |

### Data & Infrastructure
| Category | Technology | Version | Purpose |
|----------|------------|---------|---------|
| **Database** | **PostgreSQL** | 15 (Alpine)| Reliable, ACID-compliant relational database |
| **Pooling** | **HikariCP** | - | High-performance JDBC connection pooling |
| **Container** | **Docker** | - | Application containerization for consistency |
| **Orchestrator**| **Docker Compose** | v3.8 | Multi-container environment definition |

---

## 2. High-Level Architecture (C4 Context)

This diagram shows the system in the context of its users and external dependencies.

```mermaid
C4Context
    title C4 Context Diagram - SwissPine Connection Planner

    Person(dataManager, "Data Manager", "Internal user who manages connections and plans.")
    
    System_Boundary(swisspine_boundary, "SwissPine Connection Planner") {
        System(swisspine, "Connection Planner System", "Allows users to define, manage, and plan external data connections.")
    }

    System_Ext(bloomberg, "Bloomberg API", "External Data Provider")
    System_Ext(reuters, "Refinitiv Workspace", "External Data Provider")
    System_Ext(msci, "MSCI Factor Data", "External Data Provider")
    System_Ext(others, "Other Data Providers", "Various financial data sources")

    Rel(dataManager, swisspine, "Views, Creates, Deletes Plans", "HTTPS")
    Rel(swisspine, bloomberg, "Configures Connection", "HTTPS/API")
    Rel(swisspine, reuters, "Configures Connection", "HTTPS/API")
    Rel(swisspine, msci, "Configures Connection", "HTTPS/API")
    Rel(swisspine, others, "Configures Connection", "HTTPS/API")
```

---

## 3. Container Architecture (C4 Container)

This view zooms in to show the independently deployable containers (Web App, API, Database) and their interactions.

```mermaid
C4Container
    title C4 Container Diagram - SwissPine System

    Person(user, "User", "Data Manager")

    System_Boundary(c1, "SwissPine Application") {
        Container(spa, "Single Page Application", "Angular, TypeScript", "Provides the interactive UI for dashboard, planners, and connections.")
        
        Container(api, "API Application", "Java, Spring Boot", "Handles business logic, validation, data processing, and metrics.")
        
        ContainerDb(database, "Database", "PostgreSQL", "Stores connection definitions, plans, funds, and aliases.")
    }

    Rel(user, spa, "Uses", "HTTPS")
    Rel(spa, api, "API Calls (JSON)", "HTTPS/RyJS")
    Rel(api, database, "Reads/Writes", "JDBC/JPA")
```

---

## 4. Component Design (Backend)

Detailed view of the Spring Boot application structure, highlighting the separation of concerns.

```mermaid
classDiagram
    direction TB
    
    %% Controllers Layer
    class StatisticsController {
        +getPerformanceStatistics()
    }
    class PlannerController {
        +getAllPlanners()
        +createPlanner()
        +deletePlanner()
        +searchPlanners()
    }
    class ExternalConnectionController {
        +getAllConnections()
        +createConnection()
        +deleteConnection()
    }

    %% Service Layer
    class StatisticsService {
        +collectDatabaseStatistics()
        +collectJvmStatistics()
        +collectEndpointStatistics()
    }
    class PlannerService {
        +search()
        +findAll()
        +save()
    }
    class ConnectionService {
        +validateConnection()
        +save()
    }

    %% Data Access Layer
    class PlannerRepository {
        <<interface>>
        +findByNameContaining()
        +findByStatus()
    }
    class ConnectionRepository {
        <<interface>>
    }
    
    %% Relationships
    StatisticsController --> StatisticsService : Uses
    PlannerController --> PlannerService : Uses
    ExternalConnectionController --> ConnectionService : Uses
    
    StatisticsService --> PlannerRepository : Counts Records
    StatisticsService --> ConnectionRepository : Counts Records
    
    PlannerService --> PlannerRepository : Data Access
    ConnectionService --> ConnectionRepository : Data Access
```

---

## 5. Deployment Infrastructure

Visualizing how the application is deployed using Docker Compose.

```mermaid
graph TB
    subgraph Host ["Docker Host (Local Development Machine)"]
        subgraph Network ["Docker Network: swisspine-network"]
            
            subgraph FrontendContainer ["Container: swisspine-frontend"]
                Angular["Angular App (Nginx/Node)"]
                Port4200["Port 4200"]
            end

            subgraph BackendContainer ["Container: swisspine-backend"]
                SpringBoot["Spring Boot API"]
                Actuator["Actuator Metrics"]
                Port8080["Port 8080"]
            end

            subgraph PostgresContainer ["Container: swisspine-postgres"]
                Postgres["PostgreSQL 15"]
                DataVol["Volume: postgres_data"]
                Port5432["Port 5432"]
            end

            Angular -- "REST API (proxy)" --> SpringBoot
            SpringBoot -- "JDBC" --> Postgres
        end
        
        User((External User)) -- "Browser (http://localhost:4200)" --> Port4200
        User -- "Direct API (http://localhost:8080)" --> Port8080
    end
    
    style Angular fill:#dd0031,stroke:#333,stroke-width:2px,color:white
    style SpringBoot fill:#6db33f,stroke:#333,stroke-width:2px,color:white
    style Postgres fill:#336791,stroke:#333,stroke-width:2px,color:white
```

## 6. Performance Monitoring Architecture

Since performance is a key KPI, here is the flow of metrics in the system.

```mermaid
sequenceDiagram
    participant Clients as Client/Browser
    participant Controller as API Controller
    participant Service as Business Service
    participant Micrometer as Micrometer Registry
    participant Actuator as Spring Actuator
    participant Dashboard as Stats Dashboard

    Note over Micrometer, Actuator: Background Monitoring

    Clients->>Controller: HTTP Request (GET/POST/DELETE)
    activate Controller
    
    Controller->>Micrometer: Record Execution Time
    Controller->>Service: Process Request
    activate Service
    Service->>Micrometer: Record DB Query Stats
    Service-->>Controller: Return Result
    deactivate Service
    Controller-->>Clients: Response 200/204
    deactivate Controller

    Note over Dashboard: Periodic Refresh (5s)

    Dashboard->>Actuator: GET /api/statistics/performance
    activate Actuator
    Actuator->>Micrometer: Collect Aggregate Metrics
    Micrometer-->>Actuator: Returns (Uptime, Memory, Endpoint Stats)
    Actuator-->>Dashboard: JSON Data
    deactivate Actuator
    Dashboard->>Dashboard: Render Charts & Gauges
```
