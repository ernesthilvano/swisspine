# Connection Planner Service

A production-grade Spring Boot application for managing external system connections and data planner configurations.

## 🏗️ Architecture

**Technology Stack:**
- **Framework**: Spring Boot 3.2.2
- **Language**: Java 17
- **Database**: PostgreSQL 15
- **ORM**: Spring Data JPA with Hibernate
- **API Documentation**: OpenAPI 3.0 (Swagger)
- **Build Tool**: Maven
- **Containerization**: Docker

**Key Features:**
- ✅ Server-side search and pagination
- ✅ Comprehensive validation and error handling
- ✅ Optimized database queries with EntityGraph
- ✅ Value field masking for security
- ✅ Optimistic locking for concurrency control
- ✅ Automatic audit timestamps
- ✅ RESTful API design
- ✅ OpenAPI/Swagger documentation

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.8+
- PostgreSQL 15+ (or use Docker Compose)
- Docker (optional, for containerized deployment)

### Local Development Setup

1. **Start PostgreSQL** (if not using Docker Compose):
```bash
# Create database
createdb swisspine

# Or docker postgres:
docker run --name postgres -e POSTGRES_DB=swisspine -e POSTGRES_USER=app -e POSTGRES_PASSWORD=password -p 5432:5432 -d postgres:15-alpine
```

2. **Configure Database Connection**:
Edit `src/main/resources/application.properties` if using different credentials:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/swisspine
spring.datasource.username=app
spring.datasource.password=password
```

3. **Build and Run**:
```bash
# Install dependencies and build
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

4. **Verify Application**:
- API Base URL: http://localhost:8080/api
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/api-docs
- Health Check: http://localhost:8080/actuator/health

## 📚 API Documentation

### External Connections API

**Base Path**: `/api/external-connections`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | List all connections (paginated, searchable) |
| GET | `/{id}` | Get connection by ID |
| POST | `/` | Create new connection |
| PUT | `/{id}` | Update connection |
| DELETE | `/{id}` | Delete connection |
| POST | `/{id}/copy` | Copy existing connection |

**Query Parameters for List**:
- `search` (optional): Search term for connection name
- `page` (default: 0): Page number (0-indexed)
- `size` (default: 10): Items per page (5, 10, or 25)

**Example Request**:
```bash
# Get all connections with search
curl "http://localhost:8080/api/external-connections?search=test&page=0&size=10"

# Create new connection
curl -X POST http://localhost:8080/api/external-connections \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Connection",
    "baseUrl": "https://api.example.com",
    "authenticationMethod": "API Key",
    "keyField": "x-api-key",
    "valueField": "secret-key-value",
    "authenticationPlace": "Header",
    "isDefault": false
  }'
```

### Master Data API

**Base Path**: `/api/master-data`

| Endpoint | Description |
|----------|-------------|
| GET `/source-names` | Get all source names (A-Z) |
| POST `/source-names` | Create source name |
| GET `/run-names` | Get all run names (A-Z) |
| POST `/run-names` | Create run name |
| GET `/report-types` | Get all report types (A-Z) |
| POST `/report-types` | Create report type |
| GET `/report-names` | Get all report names (A-Z) |
| POST `/report-names` | Create report name |
| GET `/funds` | Get all funds (A-Z) |
| POST `/funds` | Create fund |

## 🗄️ Database Schema

The application uses a normalized relational schema with 12 tables:

**Core Tables:**
- `external_connections` - External system configurations
- `planners` - Data planner configurations
- `planner_funds` - Planner-fund relationships
- `planner_sources` - Planner data sources
- `planner_runs` - Run configurations
- `planner_reports` - Report configurations

**Master Data Tables:**
- `source_names`
- `run_names`
- `report_types`
- `report_names`
- `funds`
- `fund_aliases`

**Migrations**: Managed by Flyway in `src/main/resources/db/migration/`

## 🔒 Security Features

1. **Value Field Protection**:
   - Automatically masked in API responses
   - Immutable once set (enforced by business logic)
   - Flagged with `valueFieldSet` boolean

2. **Database Constraints**:
   - Unique constraints on names
   - Foreign key constraints with appropriate cascades
   - Check constraints on enums

3. **Optimistic Locking**:
   - Version field prevents concurrent update conflicts
   - Returns 409 Conflict on version mismatch

## ⚡ Performance Optimizations

1. **Database Layer**:
   - Strategic indexes on search/filter columns
   - EntityGraph to prevent N+1 queries
   - Connection pooling with HikariCP (optimized settings)
   - JPA batch operations enabled

2. **Query Optimization**:
   - Lazy loading for relationships
   - Pageable support for all list endpoints
   - Server-side search (not in-memory)

3. **Caching**:
   - Master data suitable for caching (future enhancement)
   - HTTP caching headers (future enhancement)

## 🧪 Testing

```bash
# Run unit tests
./mvnw test

# Run integration tests
./mvnw verify

# Generate coverage report
./mvnw test jacoco:report
# Report location: target/site/jacoco/index.html
```

## 🐳 Docker Deployment

```bash
# Build Docker image
docker build -t connection-planner:1.0.0 .

# Run with docker-compose (includes PostgreSQL)
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

## 📊 Monitoring

**Actuator Endpoints** (Production-ready metrics):
- `/actuator/health` - Health check
- `/actuator/info` - Application info
- `/actuator/metrics` - Application metrics
- `/actuator/prometheus` - Prometheus metrics

## 🔧 Configuration

### Application Properties

Key configuration options in `application.properties`:

```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/swisspine
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5

# JPA
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.jdbc.batch_size=20

# Flyway
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
```

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/swisspine/
│   │   ├── entity/          # JPA entities
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── repository/      # Spring Data repositories
│   │   ├── service/         # Business logic layer
│   │   ├── controller/      # REST controllers
│   │   ├── exception/       # Custom exceptions
│   │   ├── config/          # Spring configuration
│   │   └── common/          # Shared utilities
│   └── resources/
│       ├── db/migration/    # Flyway SQL scripts
│       └── application.properties
└── test/
    └── java/com/swisspine/  # Unit and integration tests
```

## 🚀 Production Deployment

### Environment Variables

```bash
# Database
SPRING_DATASOURCE_URL=jdbc:postgresql://prod-db:5432/swisspine
SPRING_DATASOURCE_USERNAME=app_user
SPRING_DATASOURCE_PASSWORD=secure_password

# JPA
SPRING_JPA_SHOW_SQL=false
SPRING_JPA_HIBERNATE_DDL_AUTO=validate

# Logging
LOGGING_LEVEL_ROOT=INFO
LOGGING_LEVEL_COM_SWISSPINE=INFO
```

### Recommended Infrastructure (Azure)

- **Compute**: Azure App Service (Linux, Java 17)
- **Database**: Azure Database for PostgreSQL (Flexible Server)
- **Monitoring**: Application Insights
- **Secrets**: Azure Key Vault
- **Container Registry**: Azure Container Registry

## 👥 Team

Developed by SwissPine Engineering Team

## 📄 License

Proprietary - SwissPine © 2024

---

**For frontend integration**, ensure CORS origins are configured in `WebConfig.java`:
```java
.allowedOrigins("http://localhost:4200", "http://localhost:3000")
```
