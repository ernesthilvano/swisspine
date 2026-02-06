# Future Roadmap, Considerations & Best Practices

This document outlines the strategic direction for the **SwissPine Connection Planner**, identifying key areas for technical improvement, scalability considerations, and engineering best practices for the next phase of development.

## 1. Critical Feature Enhancements

### 🔒 Security & Authentication (High Priority)
The current MVP does not enforce authentication.
*   **OIDC Integration**: Integrate with an Identity Provider (Auth0, Azure AD, or Keycloak) using OAuth2/OpenID Connect.
*   **Role-Based Access Control (RBAC)**:
    *   `ADMIN`: Full access to configure Connections and Master Data.
    *   `PLANNER`: Can create/edit Plans but not underlying metadata.
    *   `VIEWER`: Read-only access to dashboards and search.
*   **Audit Logging**: Implement persistent logging of *who* changed *what* and *when* for compliance (essential for financial apps).

### 🚀 Performance Optimizations
*   **Caching Strategy**: Implement `Redis` or `Caffeine` caching for:
    *   Static Master Data (Runs, Sources, Types) which rarely changes.
    *   Expensive statistical aggregations.
*   **Database Indexing**: As data grows (>1M records), review `EXPLAIN ANALYZE` outputs on the `planner` table search queries and add composite indexes where necessary.
*   **Frontend State Management**: Introduce **NgRx** or **Elf** if the application complexity grows, to handle cached state and reduce API calls.

## 2. Scalability Architecture

### Database Scalability
*   **Read Replicas**: For the `Statistics Dashboard` and heavy `Search` operations, route traffic to a Postgres Read Replica to offload the primary writer.
*   **Connection Pooling**: Fine-tune `HikariCP` settings based on actual production traffic patterns (monitor `active_connections` vs `idle_connections`).

### Application Scalability
*   **Horizontal Scaling**: The Spring Boot backend is stateless. In Azure Container Apps, configure **KEDA** rules to auto-scale replicas based on HTTP request count or CPU usage.
*   **Asynchronous Processing**: Move heavy operations (like "Bulk Import" or "Report Generation") to a background job queue (e.g., using **RabbitMQ** or **Azure Service Bus**).

## 3. Engineering Best Practices

### ✅ Code Quality & Standards
*   **Backend**:
    *   Use **MapStruct** for all Entity-DTO mappings to improve performance and code cleanliness.
    *   Enforce **Spotless** or **Checkstyle** in the build pipeline to maintain formatting standards.
*   **Frontend**:
    *   Strictly follow **Angular Style Guide**.
    *   Use **Storybook** for developing and documenting UI components in isolation (especially for the Complex Forms).

### 🧪 Testing Strategy
*   **Unit Tests**: Maintain >80% code coverage. Use `JUnit 5` and `Mockito`.
*   **Integration Tests**: Use **Testcontainers** to spin up real PostgreSQL containers for testing Repository and Service layers.
*   **E2E Tests**: Implement **Playwright** or **Cypress** to test critical user flows (e.g., "Create Planner -> Search Planner -> Verify in Dashboard").
*   **Performance Testing**: Use **k6** or **JMeter** to simulate load on the API endpoints before major releases.

### 🔄 CI/CD Pipeline
*   **Automated Validation**: pipeline should run `mvn test`, `npm test`, and `docker build` on every Pull Request.
*   **Security Scanning**: Integrate **SonarQube** and **OWASP Dependency Check** to catch vulnerabilities early.
*   **GitOps**: Manage infrastructure changes (Terraform) via Pull Requests, applying changes automatically upon merge.

---

## 4. UI/UX Improvements
*   **Advanced Visualizations**: Replace simple metric cards with interactive charts (e.g., **Chart.js** or **Apache ECharts**) for historical trend analysis.
*   **Bulk Operations**: Allow users to select multiple Planners to "Archive" or "Delete" in bulk.
*   **User Preferences**: Persist user settings (Dark/Light mode, default sort order, hidden columns) to local storage or user profile.
