#!/bin/bash

# SwissPine Connection Planner - Quick Start Script
# This script starts the entire application stack using Docker Compose

set -e

echo "🚀 Starting SwissPine Connection Planner..."
echo ""

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Error: Docker is not running. Please start Docker Desktop and try again."
    exit 1
fi

# Check if docker-compose is available
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Error: docker-compose is not installed. Please install Docker Compose and try again."
    exit 1
fi

echo "📦 Building Docker images..."
docker-compose build

echo ""
echo "🔧 Starting services..."
docker-compose up -d

echo ""
echo "⏳ Waiting for services to be healthy..."
echo "   This may take 60-90 seconds on first run..."

# Wait for postgres
echo -n "   - PostgreSQL: "
timeout=60
count=0
until docker-compose exec -T postgres pg_isready -U app -d swisspine > /dev/null 2>&1; do
    sleep 2
    count=$((count + 2))
    if [ $count -ge $timeout ]; then
        echo "❌ Timeout"
        docker-compose logs postgres
        exit 1
    fi
done
echo "✅ Ready"

# Wait for backend
echo -n "   - Backend API: "
timeout=90
count=0
until curl -sf http://localhost:8080/actuator/health > /dev/null 2>&1; do
    sleep 3
    count=$((count + 3))
    if [ $count -ge $timeout ]; then
        echo "❌ Timeout"
        docker-compose logs backend
        exit 1
    fi
done
echo "✅ Ready"

echo ""
echo "✅ All services are running!"
echo ""
echo "📍 Access Points:"
echo "   - Backend API:    http://localhost:8080/api"
echo "   - Swagger UI:     http://localhost:8080/swagger-ui.html"
echo "   - Health Check:   http://localhost:8080/actuator/health"
echo "   - PostgreSQL:     localhost:5432 (user: app, db: swisspine)"
echo ""
echo "📊 Useful Commands:"
echo "   View backend logs:    docker-compose logs -f backend"
echo "   View all logs:        docker-compose logs -f"
echo "   Stop services:        docker-compose down"
echo "   Restart backend:      docker-compose restart backend"
echo ""
echo "🎉 Happy coding!"
