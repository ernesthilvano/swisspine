variable "project_name" {
  default = "swisspine"
}

variable "environment" {
  default = "dev"
}

variable "location" {
  default = "East US"
}

variable "resource_group_name" {
  default = "rg-swisspine-dev"
}

variable "db_admin_username" {
  description = "Database administrator username"
  type        = string
  sensitive   = true
}

variable "db_admin_password" {
  description = "Database administrator password"
  type        = string
  sensitive   = true
}

variable "tenant_id" {
  description = "Azure Tenant ID"
  type        = string
}
