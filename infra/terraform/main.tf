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

data "railway_project" "franchise" {
  id = "8886ea43-c307-4049-9bd4-6b391c54d857"
}

resource "railway_service" "app" {
  project_id = data.railway_project.franchise.id
  name       = "franchise-api"

  source_repo = "JuanGuzmanG/franchise-api"
}

resource "railway_variable" "r2dbc_url" {
  project_id     = data.railway_project.franchise.id
  environment_id = data.railway_project.franchise.default_environment_id
  service_id     = railway_service.app.id

  name  = "R2DBC_URL"
  value = "r2dbc:pool:mysql://mysql.railway.internal:3306/railway"
}

resource "railway_variable" "r2dbc_username" {
  project_id     = data.railway_project.franchise.id
  environment_id = data.railway_project.franchise.default_environment_id
  service_id     = railway_service.app.id

  name  = "R2DBC_USERNAME"
  value = "root"
}

resource "railway_variable" "r2dbc_password" {
  project_id     = data.railway_project.franchise.id
  environment_id = data.railway_project.franchise.default_environment_id
  service_id     = railway_service.app.id

  name  = "R2DBC_PASSWORD"
  value = var.r2dbc_password
}