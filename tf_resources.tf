resource "azurerm_resource_group" "coviddemo" {
  name     = "COVIDDEMO"
  location = "East US 2"

  tags = {
    environment = "Covid Demo"
  }
}

resource "azurerm_log_analytics_workspace" "coviddemo" {
  name                = "covid-law"
  location            = azurerm_resource_group.coviddemo.location
  resource_group_name = azurerm_resource_group.coviddemo.name
  sku                 = "PerGB2018"
}

resource "azurerm_log_analytics_solution" "coviddemo" {
  solution_name         = "ContainerInsights"
  location              = azurerm_resource_group.coviddemo.location
  resource_group_name   = azurerm_resource_group.coviddemo.name
  workspace_resource_id = azurerm_log_analytics_workspace.coviddemo.id
  workspace_name        = azurerm_log_analytics_workspace.coviddemo.name

  plan {
    publisher = "Microsoft"
    product   = "OMSGallery/ContainerInsights"
  }
}

resource "azurerm_kubernetes_cluster" "coviddemo" {
  name                = "covid-aks"
  location            = azurerm_resource_group.coviddemo.location
  resource_group_name = azurerm_resource_group.coviddemo.name
  dns_prefix          = "covid-aks"

  default_node_pool {
    name            = "nodepool"
    node_count      = 1
    vm_size         = "Standard_B2s"
    os_disk_size_gb = 30
  }

  service_principal {
    client_id     = var.Azure_AppID
    client_secret = var.Azure_AppIDPW
  }

  role_based_access_control {
    enabled = true
  }

  network_profile {
    network_plugin    = "kubenet"
    load_balancer_sku = "Standard"
  }

  addon_profile {
    oms_agent {
      enabled                    = true
      log_analytics_workspace_id = azurerm_log_analytics_workspace.coviddemo.id
    }
  }

  tags = {
    environment = "Covid Demo"
  }
}

resource "azurerm_mssql_server" "coviddemo" {
  name                         = "covid-sql-devops-playground"
  resource_group_name          = azurerm_resource_group.coviddemo.name
  location                     = azurerm_resource_group.coviddemo.location
  version                      = "12.0"
  administrator_login          = var.Azure_SQLADMIN
  administrator_login_password = var.Azure_SQLADMINPW
  minimum_tls_version          = 1.2

  tags = {
    environment = "Covid Demo"
  }
}

resource "azurerm_mssql_database" "coviddemo" {
  name           = "covid-sql-db-devops-playground"
  server_id      = azurerm_mssql_server.coviddemo.id
  collation      = "SQL_Latin1_General_CP1_CI_AS"
  sku_name       = "Basic"
  max_size_gb    = 2

  tags = {
    environment = "Covid Demo"
  }
}

resource "azurerm_mssql_firewall_rule" "coviddemo" {
  name                = "AKS-SQL-Rule"
  server_id           = azurerm_mssql_server.coviddemo.id
  start_ip_address    = data.azurerm_public_ip.coviddemo.ip_address
  end_ip_address      = data.azurerm_public_ip.coviddemo.ip_address
}
