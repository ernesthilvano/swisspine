#!/bin/bash

# Script to stop local PostgreSQL and start Docker containers

echo "🛑 Stopping local PostgreSQL service..."

# Try different methods to stop PostgreSQL
if command -v brew &> /dev/null; then
    echo "  Trying Homebrew..."
    sudo brew services stop postgresql || true
    sudo brew services stop postgresql@16 || true
fi

# Stop via launchctl
echo "  Trying launchctl..."
sudo launchctl stop com.edb.launchdaemon.postgresql-16 || true
sudo launchctl unload /Library/LaunchDaemons/com.edb.launchdaemon.postgresql-16.plist 2>/dev/null || true

# Kill postgres processes
echo "  Stopping postgres processes..."
sudo pkill -9 postgres || true

# Wait a moment
sleep 2

# Verify port is free
if lsof -i :5432 > /dev/null 2>&1; then
    echo "❌ Port 5432 is still in use. Please manually stop PostgreSQL:"
    echo "   1. Open System Preferences / Settings"
    echo "   2. Search for 'postgres' or check Login Items" 
    echo "   3. Stop the PostgreSQL service"
    echo ""
    lsof -i :5432
    exit 1
fi

echo "✅ Port 5432 is now free"
echo ""
echo "🐳 Starting Docker containers..."
docker compose up -d

echo ""
echo "⏳ Waiting for services to start (30 seconds)..."
sleep 30

echo ""
echo "📊 Container status:"
docker compose ps

echo ""
echo "🏥 Checking backend health..."
if curl -sf http://localhost:8080/actuator/health > /dev/null 2>&1; then
    echo "✅ Backend is healthy!"
    echo ""
    echo "📍 Access Points:"
    echo "   - Swagger UI: http://localhost:8080/swagger-ui.html"
    echo "   - API: http://localhost:8080/api"
    echo "   - Health: http://localhost:8080/actuator/health"
else
    echo "⚠️  Backend is starting... check logs with:"
    echo "   docker compose logs -f backend"
fi
