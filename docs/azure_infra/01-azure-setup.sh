#!/usr/bin/env bash
# ---------------------------------------------------------------------------
# Standalone script: Create just the App Service Plan + Web App
# Use this if 01-azure-setup.sh never completed, or if the web app name
# collided with an existing globally-unique name.
# ---------------------------------------------------------------------------
set -euo pipefail

RESOURCE_GROUP="rg-azuser8168_mml.local-IxXPn"   # your existing sandbox RG
LOCATION="centralindia"                           # change if your sandbox is pinned to a region
APP_SERVICE_PLAN="storeops-plan-azuser8168-IxXPn"
WEBAPP_NAME="storeops-app-azuser8168-IxXPn"       # matches your resource group naming pattern
JAVA_RUNTIME="JAVA|21-java21"
SKU="B1"                                          # sandbox subscriptions usually only allow Free/Basic tiers

# 1. Check if resource group exists (it should, since you're already using it)
az group show --name "$RESOURCE_GROUP" --output table

# 2. Create App Service Plan (skips if it already exists)
az appservice plan create \
  --name "$APP_SERVICE_PLAN" \
  --resource-group "$RESOURCE_GROUP" \
  --location "$LOCATION" \
  --sku "$SKU" \
  --is-linux

# 3. Create the Web App
az webapp create \
  --name "$WEBAPP_NAME" \
  --resource-group "$RESOURCE_GROUP" \
  --plan "$APP_SERVICE_PLAN" \
  --runtime "$JAVA_RUNTIME"

# 4. App settings for Spring Boot
az webapp config appsettings set \
  --name "$WEBAPP_NAME" \
  --resource-group "$RESOURCE_GROUP" \
  --settings \
      WEBSITES_PORT=8080 \
      SPRING_PROFILES_ACTIVE=prod

echo "==================================================================="
echo "Web App created: https://${WEBAPP_NAME}.azurewebsites.net"
echo ""
echo "IMPORTANT: Update WEBAPP_NAME in your other scripts and the GitHub"
echo "workflow YAML (env: AZURE_WEBAPP_NAME) to: ${WEBAPP_NAME}"
echo "==================================================================="
