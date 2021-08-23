terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "2.66.0"
    }
  }
  
  backend "azurerm" {
    resource_group_name  = var.storage_account_resource_group
    storage_account_name = var.storage_account_name
    container_name       = var.storage_account_container_name
    key                  = var.storage_account_file_name
  }

  required_version = ">= 0.14"
}

resource "random_pet" "prefix" {}

provider "azurerm" {
  features {}
}

resource "azurerm_resource_group" "default" {
  name     = "${random_pet.prefix.id}-rg"
  location = "East US 2"

  tags = {
    environment = "Demo"
  }
}

resource "azurerm_kubernetes_cluster" "default" {
  name                = "${random_pet.prefix.id}-aks"
  location            = azurerm_resource_group.default.location
  resource_group_name = azurerm_resource_group.default.name
  dns_prefix          = "${random_pet.prefix.id}-k8s"

  default_node_pool {
    name            = "default"
    node_count      = 2
    vm_size         = "Standard_D2_v2"
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

variable "storage_account_resource_group" {
  description = "Resource Group where the Storage Account for the TF state is located"
}

variable "storage_account_name" {
  description = "Storage Account name"
}

variable "storage_account_container_name" {
  description = "Storage Account container name"
}

variable "terraform_state_file_name" {
  description = "Terraform State file name"
}

output "resource_group_name" {
  value = azurerm_resource_group.default.name
}

output "kubernetes_cluster_name" {
  value = azurerm_kubernetes_cluster.default.name
}
