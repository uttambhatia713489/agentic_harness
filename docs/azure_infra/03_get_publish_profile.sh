#!/usr/bin/env bash
# ---------------------------------------------------------------------------
# Step 2 (Sandbox-Compatible): Download the Web App publish profile.
# Use this INSTEAD of OIDC setup when your subscription doesn't allow
# role assignments (e.g., Microsoft Learn sandbox subscriptions).
#
# Requires only "Website Contributor" role, which sandbox users have.
# ---------------------------------------------------------------------------
set -euo pipefail

RESOURCE_GROUP="rg-azuser8168_mml.local-IxXPn"    # your actual sandbox RG
WEBAPP_NAME="storeops-app-azuser8168-IxXPn"       # matches the Web App you created

az webapp deployment list-publishing-profiles \
  --name "$WEBAPP_NAME" \
  --resource-group "$RESOURCE_GROUP" \
  --xml > publish-profile.xml

echo "==================================================================="
echo "publish-profile.xml has been saved in the current directory."
echo ""
echo "NEXT STEPS:"
echo "1. Open publish-profile.xml and copy its ENTIRE contents."
echo "2. Go to GitHub repo -> Settings -> Secrets and variables -> Actions"
echo "3. Create a new repository secret named:"
echo "     AZURE_WEBAPP_PUBLISH_PROFILE"
echo "4. Paste the full XML content as the secret value."
echo "5. Delete publish-profile.xml locally afterward (it is a credential!)."
echo "==================================================================="
