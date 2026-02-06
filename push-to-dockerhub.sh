#!/bin/bash

# SwissPine Docker Hub Push Script
# Usage: ./push-to-dockerhub.sh your-dockerhub-username

# Configuration
DOCKER_HUB_USER="${1:-your-dockerhub-username}"
VERSION="1.0.0"

if [ "$DOCKER_HUB_USER" = "your-dockerhub-username" ]; then
    echo "❌ Error: Please provide your Docker Hub username"
    echo "Usage: ./push-to-dockerhub.sh your-dockerhub-username"
    exit 1
fi

echo "🐳 Pushing SwissPine images to Docker Hub"
echo "User: $DOCKER_HUB_USER"
echo "Version: $VERSION"
echo ""

# Check if logged in
echo "Checking Docker login status..."
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker daemon not running"
    exit 1
fi

echo "⚠️  Please ensure you're logged in to Docker Hub"
echo "Run 'docker login' if you haven't already"
read -p "Press enter to continue..."

echo ""
echo "📦 Building images..."
docker compose build

if [ $? -ne 0 ]; then
    echo "❌ Build failed"
    exit 1
fi

echo ""
echo "🏷️  Tagging images..."

# Backend
echo "Tagging backend..."
docker tag swisspine_backend:latest ${DOCKER_HUB_USER}/swisspine_backend:${VERSION}
docker tag swisspine_backend:latest ${DOCKER_HUB_USER}/swisspine_backend:latest

# Frontend
echo "Tagging frontend..."
docker tag swisspine_frontend:latest ${DOCKER_HUB_USER}/swisspine_frontend:${VERSION}
docker tag swisspine_frontend:latest ${DOCKER_HUB_USER}/swisspine_frontend:latest

echo ""
echo "⬆️  Pushing to Docker Hub..."
echo "This may take several minutes depending on your connection..."
echo ""

# Push Backend
echo "Pushing backend:${VERSION}..."
docker push ${DOCKER_HUB_USER}/swisspine_backend:${VERSION}
if [ $? -ne 0 ]; then
    echo "❌ Failed to push backend:${VERSION}"
    exit 1
fi

echo "Pushing backend:latest..."
docker push ${DOCKER_HUB_USER}/swisspine_backend:latest
if [ $? -ne 0 ]; then
    echo "❌ Failed to push backend:latest"
    exit 1
fi

# Push Frontend
echo "Pushing frontend:${VERSION}..."
docker push ${DOCKER_HUB_USER}/swisspine_frontend:${VERSION}
if [ $? -ne 0 ]; then
    echo "❌ Failed to push frontend:${VERSION}"
    exit 1
fi

echo "Pushing frontend:latest..."
docker push ${DOCKER_HUB_USER}/swisspine_frontend:latest
if [ $? -ne 0 ]; then
    echo "❌ Failed to push frontend:latest"
    exit 1
fi

echo ""
echo "✅ All images pushed successfully!"
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "Your images are now available at:"
echo "  🔗 https://hub.docker.com/r/${DOCKER_HUB_USER}/swisspine_backend"
echo "  🔗 https://hub.docker.com/r/${DOCKER_HUB_USER}/swisspine_frontend"
echo ""
echo "To pull and run these images:"
echo "  docker pull ${DOCKER_HUB_USER}/swisspine_backend:${VERSION}"
echo "  docker pull ${DOCKER_HUB_USER}/swisspine_frontend:${VERSION}"
echo ""
echo "Or use the provided docker-compose.registry.yml:"
echo "  DOCKER_HUB_USER=${DOCKER_HUB_USER} docker compose -f docker-compose.registry.yml up"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
