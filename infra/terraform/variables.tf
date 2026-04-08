variable "railway_token" {
  type      = string
  sensitive = true
}

variable "project_id" {
  type    = string
  default = "8886ea43-c307-4049-9bd4-6b391c54d857"
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