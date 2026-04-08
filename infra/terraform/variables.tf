variable "railway_token" {
  type      = string
  sensitive = true
}

variable "project_id" {
  type    = string
  default = "78daf110-143a-4406-95a2-847a543e35c2"
}

variable "environment_id" {
  description = "Railway Environment ID (production)"
  type        = string
}

variable "github_repo" {
  type    = string
  default = "JuanGuzmanG/franchise_api"
}

variable "r2dbc_password" {
  type      = string
  sensitive = true
}

variable "service_id" {
  description = "ID del servicio franchise-api creado en Railway UI"
  type        = string
}