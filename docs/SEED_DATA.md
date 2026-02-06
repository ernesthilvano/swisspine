# Seed Data Generator

## Overview

The seed data generator creates thousands of realistic records for performance testing and demonstration purposes.

## Data Generated

| Entity | Count | Description |
|--------|-------|-------------|
| **Funds** | 2,000 | Investment fund master data |
| **Fund Aliases** | 3,000 | Alternative names for funds |
| **Source Names** | 200 | Data source identifiers |
| **Run Names** | 300 | Run configuration names |
| **Report Types** | 50 | Report categories |
| **Report Names** | 500 | Specific report definitions |
| **External Connections** | 100 | API connection configurations |
| **Planners** | 5,000 | Planning instances with full relationships |

**Total Junction Table Entries:**
- Planner Funds: ~15,000+ (avg 3 per planner)
- Planner Sources: ~10,000+ (avg 2 per planner)
- Planner Runs: ~15,000+ (avg 1.5 per source)
- Planner Reports: ~15,000+ (avg 1.5 per source)

**Grand Total: ~60,000+ database rows**

## Running the Seed Data Generator

### Option 1: Using Docker Compose (Recommended)

Modify `docker-compose.yml` to add the seed profile:

```yaml
backend:
  environment:
    - SPRING_PROFILES_ACTIVE=seed-data
```

Then run:

```bash
docker compose down -v  # Clear existing data
docker compose up --build
```

### Option 2: Using Maven

```bash
# From backend directory
mvn spring-boot:run -Dspring-boot.run.profiles=seed-data
```

### Option 3: Using JAR

```bash
java -jar target/connection-planner-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=seed-data
```

## Performance Expectations

**Generation Time:** ~30-60 seconds for full dataset

**Batch Processing:**
- Funds: 500 per batch
- Planners: 100 per batch (with all relationships)

**Progress Logging:**
The generator logs progress at regular intervals:
```
Generating 2000 funds...
  Saved 500 funds
  Saved 1000 funds
  Saved 1500 funds
  Saved 2000 funds
✓ Generated 2000 funds
```

## Implementation Details

### Realistic Data

The generator creates realistic financial system data:

- **Fund Names**: "Strategic Global Equity Fund 1234"
- **Planner Names**: "Innovative Asia Pacific Portfolio Planner #5432"
- **Connections**: "Bloomberg Production API 42"
- **Reports**: "Performance Summary Report 789"

### Status Distribution

Planners are created with realistic status distribution:
- 40% Finished
- 30% In Progress
- 20% Draft
- 10% Failed

### Relationships

Each planner includes:
- 1-5 random funds
- 1-3 data sources
- 1-2 runs per source
- 1-2 reports per source

## Database Impact

**Size Estimate:**
- PostgreSQL database size: ~200-300 MB
- Index memory usage: ~50-100 MB

**Query Performance:**
With proper indexes, queries should perform as follows:
- Simple lookups: <10ms
- Paginated lists: <50ms
- Complex joins: <100ms
- Full-text search: <200ms

## Performance Tuning

After generating seed data, use these queries to analyze performance:

### 1. Check Table Sizes

```sql
SELECT 
    schemaname,
    tablename,
    pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) AS size
FROM pg_tables
WHERE schemaname = 'public'
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;
```

### 2. Check Index Usage

```sql
SELECT 
    schemaname,
    tablename,
    indexname,
    idx_scan,
    idx_tup_read,
    idx_tup_fetch
FROM pg_stat_user_indexes
WHERE schemaname = 'public'
ORDER BY idx_scan DESC;
```

### 3. Slow Query Analysis

```sql
-- Enable slow query logging
ALTER SYSTEM SET log_min_duration_statement = 100; -- Log queries > 100ms
SELECT pg_reload_conf();
```

### 4. Vacuum and Analyze

After loading seed data, optimize the database:

```sql
VACUUM ANALYZE;
```

## Cleaning Up

To remove all seed data and start fresh:

```bash
# Stop containers
docker compose down -v

# Restart without seed profile
docker compose up
```

Or keep the data and just restart:

```bash
docker compose restart backend
```

## Customization

To modify data generation, edit `SeedDataConfiguration.java`:

```java
// Change quantities
for (int i = 1; i <= 10000; i++) {  // Generate 10k instead of 5k planners

// Adjust distribution
private String randomWeightedStatus() {
    int rand = RANDOM.nextInt(100);
    if (rand < 50) return "Finished";  // 50% instead of 40%
    // ...
}
```

## Troubleshooting

**Issue:** "Unique constraint violation"
- **Cause:** Seed data already exists
- **Fix:** Run `docker compose down -v` to clear database

**Issue:** "Out of memory"
- **Cause:** Batch size too large
- **Fix:** Reduce batch size in generator (currently 100 planners/batch)

**Issue:** Generation takes > 5 minutes
- **Cause:** Database connection slow or constraints checking
- **Fix:** Check PostgreSQL performance, increase heap size

## Next Steps

After generating seed data:

1. **Test pagination** - Verify UI with thousands of rows
2. **Benchmark queries** - Measure response times
3. **Optimize indexes** - Add/remove based on slow query log
4. **Test search** - Verify search performance across large dataset
5. **Monitor memory** - Check JVM and PostgreSQL memory usage
