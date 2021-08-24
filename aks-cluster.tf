terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "2.66.0"
    }
  }

  required_version = ">= 0.14"
}

#resource "random_pet" "prefix" {}

provider "azurerm" {
  features {}
}

resource "azurerm_resource_group" "default" {
  #name     = "${random_pet.prefix.id}-rg"
  name     = "akstest-rg"
  location = "East US 2"

  tags = {
    environment = "Demo"
  }
}

resource "azurerm_kubernetes_cluster" "default" {
  #name                = "${random_pet.prefix.id}-aks"
  name                = "akstest-aks"
  location            = azurerm_resource_group.default.location
  resource_group_name = azurerm_resource_group.default.name
  #dns_prefix          = "${random_pet.prefix.id}-k8s"
  dns_prefix          = "akstest-dns"

  default_node_pool {
    name            = "default"
    node_count      = 1
    vm_size         = "Standard_B2s"
    os_disk_size_gb = 30
  }

  service_principal {
    client_id     = var.Azure_AppID
    client_secret = var.Azure_PW
  }

  role_based_access_control {
    enabled = true
  }

  tags = {
    environment = "Demo"
  }
}

variable "Azure_AppID" {
  description = "Azure Kubernetes Service Cluster service principal"
}

variable "Azure_PW" {
  description = "Azure Kubernetes Service Cluster password"
}

output "resource_group_name" {
  value = azurerm_resource_group.default.name
}

output "kubernetes_cluster_name" {
  value = azurerm_kubernetes_cluster.default.name
}
