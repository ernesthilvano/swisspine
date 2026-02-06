# SwissPine Connection Planner

## 1. Project Overview & Problem Statement

### The Problem
Financial institutions manage hundreds of external data connections (Bloomberg, Refinitiv, MSCI, etc.) to source critical market data. Managing these connections manually or via disparate spreadsheets leads to:
*   **Operational Risk**: Misconfigured connections causing data outages.
*   **Lack of Visibility**: Difficulty in tracking which funds utilize which data sources.
*   **Performance Blindspots**: No centralized view of API latency or system health.
*   **Maintenance Overhead**: Hard to update configurations across multiple environments.

### The Solution
**SwissPine Connection Planner** is a centralized, full-stack web application designed to govern the lifecycle of external data connections. It provides a single pane of glass for Data Managers to:
*   **Define & Configure** complex data connections and their parameters.
*   **Link Funds & Planners** to specific data sources.
*   **Monitor Performance** of the system via real-time dashboards.
*   **Search & Audit** connection configurations instantly.

---
### Screenshots
<img width="811" height="783" alt="image" src="https://github.com/user-attachments/assets/0d10d2e0-6b8d-49cd-8f62-3653fa8e720f" />
<img width="863" height="861" alt="image" src="https://github.com/user-attachments/assets/eec3f173-da3c-4c70-a907-df5a6e1b4860" />

## Planner with 5k seed data
<img width="923" height="778" alt="image" src="https://github.com/user-attachments/assets/dc0d3a86-7627-421a-9230-ffc6b9d73d6d" />

<img width="795" height="868" alt="image" src="https://github.com/user-attachments/assets/ac15dded-0848-4514-bd8a-efde4f65aad1" />

## Stat dashboard
<img width="896" height="886" alt="image" src="https://github.com/user-attachments/assets/295cc9a1-7be8-4ed9-8986-0aa1d311e73e" />

---

## 2. What Has Been Delivered

We have successfully implemented a robust, production-ready MVP with the following core capabilities:

### ✅ Full-Stack Application
*   **Backend**: Java 17 / Spring Boot 3.2 microservice architecture.
*   **Frontend**: Angular 17 SPA with Material Design.
*   **Database**: PostgreSQL 15 relational schema with optimized indexing.

### ✅ Key Features Implemented
1.  **Connection Management**: CRUD operations for External Connections, Planners, and associated metadata (Run Names, Source Names).
2.  **Advanced Search**: Global search functionality to find planners by name, ID, or status.
3.  **Performance Dashboard**: Real-time monitoring of:
    *   System Uptime & JVM Health (Memory, Threads).
    *   Database Performance (Connection Pool usage, Query latency).
    *   API Endpoint Performance (Request rates, P95 latency).
4.  **Data Seeding**: Automated tools to generate realistic test data (Funds, Aliases, Connections).
5.  **Infrastructure as Code**: Complete Terraform configuration for Azure deployment.

---

## 3. Technical Documentation References

Detailed technical documentation has been generated to support operations, development, and auditing.

### 🏗️ Architecture & Design
*   **[System Architecture](docs/SYSTEM_ARCHITECTURE.md)**: High-level C4 diagrams, technology stack details, and component design.
*   **[Database Schema](docs/DATABASE_SCHEMA.md)**: Entity Relationship Diagrams (ERD) and detailed table definitions.

### 🚀 Deployment & Security
*   **[Azure Deployment Guide](docs/AZURE_DEPLOYMENT_GUIDE.md)**: Architecture for Azure Container Apps, security best practices (VNETs, Managed Identities), and deployment steps.

### 📊 Data & Implementation
*   **[Seed Data Summary](docs/SEED_DATA.md)**: details on the test data generation strategy.
*   **[Future Roadmap & Best Practices](docs/FUTURE_ROADMAP.md)**: Strategic plan for security (Auth, RBAC), scalability, and engineering standards.

### 🧪 Testing & Quality Assurance
*   **[Backend Testing Guide](docs/BACKEND_TESTING.md)**: JUnit 5 unit tests, MockMvc integration tests, coverage reports.
*   **[Frontend Testing Guide](docs/FRONTEND_TESTING.md)**: Vitest test patterns, async handling, best practices (28/28 tests passing).


---

## 4. Quick Start

### 🐳 Option 1: Orchestrated Run (Recommended)
Run the entire stack (Frontend, Backend, Database) with a single command:

```bash
docker compose up --build
```

## Pull from dockerhub
```
Images are now available at:
  🔗 https://hub.docker.com/r/ernesthilvano9223/swisspine_backend
  🔗 https://hub.docker.com/r/ernesthilvano9223/swisspine_frontend

To pull and run these images:
  docker pull ernesthilvano9223/swisspine_backend:1.0.0
  docker pull ernesthilvano9223/swisspine_frontend:1.0.0
```


*   **Frontend**: http://localhost:4200
*   **Backend API**: http://localhost:8080/swagger-ui.html
*   **Database**: localhost:5432

### 💻 Option 2: Manual Run (Development)

```bash
# 1. Start Infrastructure (Postgres)
./start-docker.sh

# 2. Run Backend (in separate terminal)
cd backend
./mvnw spring-boot:run

# 3. Run Frontend (in separate terminal)
cd frontend
npm start
```

