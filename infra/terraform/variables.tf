variable "railway_token" {
  description = "Railway API token"
  type        = string
  sensitive   = true
}

variable "r2dbc_password" {
  description = "MySQL R2DBC password"
  type        = string
  sensitive   = true
}