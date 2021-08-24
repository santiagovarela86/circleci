output "resource_group_name" {
  value = azurerm_resource_group.coviddemo.name
}

output "kubernetes_cluster_name" {
  value = azurerm_kubernetes_cluster.coviddemo.name
}

output "sql_server_name" {
  value = azurerm_mssql_server.coviddemo.name
}

output "sql_db_name" {
  value = azurerm_mssql_database.coviddemo.name
}
