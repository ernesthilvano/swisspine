# SwissPine Azure Deployment & Security Guide

This document outlines the strategy for deploying the SwissPine Connection Planner to Microsoft Azure, focusing on a secure, scalable, and manageable architecture.

## 1. Deployment Architecture

We utilize **Azure Container Apps** for the compute layer and **Azure Database for PostgreSQL (Flexible Server)** for the data layer. This serverless approach minimizes infrastructure management while providing auto-scaling and high availability.

### Core Components
- **Azure Container Registry (ACR)**: Stores private Docker images for Frontend and Backend.
- **Azure Container Apps Environment**: specific implementation of Kubernetes (AKS) tailored for microservices.
- **PostgreSQL Flexible Server**: Managed database service with high availability.
- **Azure Key Vault**: Secure storage for secrets (DB passwords, API keys).
- **Log Analytics Workspace**: Centralized logging and monitoring.

## 2. Security Implementation Strategy

To ensure the highest level of security for this financial application, we implement the "Defense in Depth" approach.

### 2.1. Network Security (VNET Integration)
*Current Terraform is simplified for demonstration. For production:*
- **Private VNET**: Container Apps and Database should communicate entirely within a private Virtual Network.
- **Private Endpoints**: Disable public access to the PostgreSQL database. Only the specific subnet containing the Backend Container App can access port 5432.
- **Application Gateway + WAF**: Front the UI with Azure Application Gateway enabled with Web Application Firewall (WAF) to protect against common web attacks (SQLi, XSS, etc.).

### 2.2. Identity & Access Management (Managed Identities)
- **Problem**: Storing database credentials in code or environment variables is risky.
- **Solution (Azure Way)**: Use **User-Assigned Managed Identities**.
    1. Assign a Managed Identity to the Backend Container App.
    2. Grant this identity access to the Key Vault.
    3. Grant this identity `PostgreSQL User` role on the database (using Azure AD Auth).
    - **Result**: Zero secrets in the application code. The app authenticates via its Azure identity.

### 2.3. Secrets Management (Key Vault)
- **Key Vault References**: Instead of actual values, Container Apps environment variables should reference Key Vault secrets.
- **Deployment**: Terraform creates the Key Vault. The admin populates it. The App just references `secretref:db-password`.

### 2.4. Container Security
- **Image Scanning**: Enable **Microsoft Defender for Containers** on the ACR to automatically scan images for vulnerabilities (CVEs) whenever they are pushed.
- **Least Privilege**: Containers should run as non-root users (already configured in our Dockerfiles).

## 3. Deployment Instructions

### Prerequisites
- [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli)
- [Terraform](https://www.terraform.io/downloads)
- Active Azure Subscription

### Step 1: Initialize Terraform
Navigate to the deployment directory:
```bash
cd deployment/azure/terraform
terraform init
```

### Step 2: Configure Variables
Create a `terraform.tfvars` file to store your sensitive inputs (do not commit this file):
```hcl
project_name        = "swisspine"
environment         = "prod"
location            = "westeurope"
resource_group_name = "rg-swisspine-prod"
db_admin_username   = "swisspine_admin"
db_admin_password   = "Use_A_Strong_Complex_Password_123!"
tenant_id           = "your-azure-tenant-id"
```

### Step 3: Preview & Apply
Review the plan:
```bash
terraform plan
```

Deploy infrastructure:
```bash
terraform apply
```

### Step 4: Build & Push Images
Once infrastructure is ready, push your images to the new ACR:
```bash
# Login to ACR
az acr login --name acrswisspineprod

# Tag Images
docker tag swisspine-backend:latest acrswisspineprod.azurecr.io/backend:v1
docker tag swisspine-frontend:latest acrswisspineprod.azurecr.io/frontend:v1

# Push
docker push acrswisspineprod.azurecr.io/backend:v1
docker push acrswisspineprod.azurecr.io/frontend:v1
```

### Step 5: Deploy Container Apps
Update your Container Apps config (via Azure Portal or CLI) to use the new images from ACR.

## 4. Monitoring & Maintenance
- **Application Insights**: Enable App Insights in the Java application (adding the agent) to get deep APM (Application Performance Monitoring) in Azure Monitor.
- **Alerts**: Set up alerts in Azure Monitor for:
    - High CPU/Memory usage.
    - Container crashes.
    - Database connection failures.
