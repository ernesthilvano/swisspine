#!/bin/bash

# Seed Data Generator Runner
# Generates thousands of records for performance testing

set -e

echo "=================================================="
echo "   Connection Planner - Seed Data Generator"
echo "=================================================="
echo ""
echo "This will generate ~60,000 database rows:"
echo "  - 2,000 Funds"
echo "  - 3,000 Fund Aliases"
echo "  - 200 Source Names"
echo "  - 300 Run Names"
echo "  - 50 Report Types"
echo "  - 500 Report Names"
echo "  - 100 External Connections"
echo "  - 5,000 Planners (with relationships)"
echo ""
echo "Estimated time: 30-60 seconds"
echo ""

read -p "Clear existing data and regenerate? (y/N): " -n 1 -r
echo ""

if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "Cancelled."
    exit 0
fi

echo ""
echo "Step 1: Stopping containers..."
docker compose down -v

echo ""
echo "Step 2: Starting database..."
docker compose up -d db

echo ""
echo "Waiting for PostgreSQL to be ready..."
sleep 5

echo ""
echo "Step 3: Running seed data generator..."
docker compose run --rm \
    -e SPRING_PROFILES_ACTIVE=seed-data \
    backend

echo ""
echo "Step 4: Starting all services..."
docker compose up -d

echo ""
echo "=================================================="
echo "  Seed data generation complete!"
echo "=================================================="
echo ""
echo "Services starting at:"
echo "  - Frontend: http://localhost:4200"
echo "  - Backend:  http://localhost:8080"
echo ""
echo "Check logs with: docker compose logs -f"
echo ""
