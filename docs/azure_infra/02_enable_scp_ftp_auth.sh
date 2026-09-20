az resource update \
  --resource-group rg-azuser8168_mml.local-IxXPn \
  --namespace Microsoft.Web \
  --parent sites/storeops-app-azuser8168-IxXPn \
  --resource-type basicPublishingCredentialsPolicies \
  --name scm \
  --set properties.allow=true

az resource update \
  --resource-group rg-azuser8168_mml.local-IxXPn \
  --namespace Microsoft.Web \
  --parent sites/storeops-app-azuser8168-IxXPn \
  --resource-type basicPublishingCredentialsPolicies \
  --name ftp \
  --set properties.allow=true

az resource show \
  --resource-group rg-azuser8168_mml.local-IxXPn \
  --namespace Microsoft.Web \
  --parent sites/storeops-app-azuser8168-IxXPn \
  --resource-type basicPublishingCredentialsPolicies \
  --name scm \
  --query "properties.allow"
