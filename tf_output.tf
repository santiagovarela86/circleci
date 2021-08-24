output "resource_group_name" {
  value = azurerm_resource_group.coviddemo.name
}

output "kubernetes_cluster_name" {
  value = azurerm_kubernetes_cluster.coviddemo.name
}

data "azurerm_public_ip" "coviddemo" {
  name                = reverse(split("/", tolist(azurerm_kubernetes_cluster.coviddemo.network_profile.0.load_balancer_profile.0.effective_outbound_ips)[0]))[0]
  resource_group_name = azurerm_kubernetes_cluster.coviddemo.node_resource_group
}

output "kubernetes_cluster_outbound_ip" {
  value = data.azurerm_public_ip.coviddemo.ip_address
}

output "sql_server_name" {
  value = azurerm_mssql_server.coviddemo.name
}

output "sql_server_fqdn" {
  value = azurerm_mssql_server.coviddemo.fully_qualified_domain_name
}

output "sql_db_name" {
  value = azurerm_mssql_database.coviddemo.name
}
