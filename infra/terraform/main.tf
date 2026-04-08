terraform {
  required_providers {
    railway = {
      source  = "terraform-community-providers/railway"
      version = "~> 0.4"
    }
  }
}

provider "railway" {
  token = var.railway_token
}

resource "railway_variable" "r2dbc_url" {
  environment_id = var.environment_id
  service_id     = var.service_id
  name           = "R2DBC_URL"
  value          = "r2dbc:pool:mysql://mysql.railway.internal:3306/railway?sslMode=preferred"
}

resource "railway_variable" "r2dbc_username" {
  environment_id = var.environment_id
  service_id     = var.service_id
  name           = "R2DBC_USERNAME"
  value          = "root"
}

resource "railway_variable" "r2dbc_password" {
  environment_id = var.environment_id
  service_id     = var.service_id
  name           = "R2DBC_PASSWORD"
  value          = var.r2dbc_password
}