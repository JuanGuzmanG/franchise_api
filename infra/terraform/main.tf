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

resource "railway_service" "app" {
  project_id  = var.project_id
  name        = "franchise-api"
  source_repo = var.github_repo
  source_repo_branch = "main"
}

resource "railway_variable" "r2dbc_url" {
  environment_id = var.environment_id
  service_id     = railway_service.app.id
  name           = "R2DBC_URL"
  value          = "r2dbc:pool:mysql://mysql.railway.internal:3306/railway?sslMode=required"
}

resource "railway_variable" "r2dbc_username" {
  environment_id = var.environment_id
  service_id     = railway_service.app.id
  name           = "R2DBC_USERNAME"
  value          = "root"
}

resource "railway_variable" "r2dbc_password" {
  environment_id = var.environment_id
  service_id     = railway_service.app.id
  name           = "R2DBC_PASSWORD"
  value          = var.r2dbc_password
}