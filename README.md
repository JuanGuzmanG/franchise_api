# franchise_api

API reactiva (Spring WebFlux + R2DBC MySQL) para gestion de franquicias, sucursales y productos.

## Requisitos

- Java 21
- Maven Wrapper (`mvnw.cmd`)
- MySQL (Azure MySQL Flexible Server o local)
- Docker Desktop (opcional para ejecucion en contenedores)
- Terraform >= 1.6 (para IaC)

## Ejecutar local

```powershell
Set-Location "C:\Users\juanj\Documents\Dev\Java\Frameworks\SpringBoot\franchise_api"
.\mvnw.cmd clean test
.\mvnw.cmd spring-boot:run
```

## Ejecutar con Docker

Levanta MySQL y la API en una sola red usando `docker-compose.yml`.

```powershell
Set-Location "C:\Users\juanj\Documents\Dev\Java\Frameworks\SpringBoot\franchise_api"
docker compose up -d --build
```

- API: `http://localhost:8080`
- MySQL: `localhost:3306`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

Detener y limpiar contenedores:

```powershell
docker compose down
```

## Infraestructura como codigo (Terraform)

Se incluye base IaC en `infra/terraform` para aprovisionar:
- Resource Group
- Azure Database for MySQL Flexible Server
- Base de datos `franchisedb`
- Regla de firewall para IP de desarrollo
- Azure App Service Plan (Linux)
- Azure Linux Web App para ejecutar la API como contenedor

Pasos:

```powershell
Set-Location "C:\Users\juanj\Documents\Dev\Java\Frameworks\SpringBoot\franchise_api\infra\terraform"
Copy-Item terraform.tfvars.example terraform.tfvars
terraform init
terraform plan
terraform apply
```

Despues del `apply`, revisa los outputs para usar el FQDN y URL sugerida R2DBC.

### Despliegue del contenedor en Azure Web App

1. Construye y publica tu imagen (Docker Hub o ACR).
2. Define `app_container_image` en `terraform.tfvars` con el tag publicado.
3. Aplica Terraform y verifica `web_app_url` en outputs.

Ejemplo de build/push a Docker Hub:

```powershell
Set-Location "C:\Users\juanj\Documents\Dev\Java\Frameworks\SpringBoot\franchise_api"
docker build -t <docker-user>/franchise_api:latest .
docker push <docker-user>/franchise_api:latest
```

## Credenciales y seguridad

Actualmente `application.properties` define conexion directa. Para entrega publica se recomienda mover usuario/password a variables de entorno.

Ejemplo (cuando decidas migrarlo):

```powershell
setx DB_HOST "academysystemdb.mysql.database.azure.com"
setx DB_PORT "3306"
setx DB_NAME "franchisedb"
setx DB_USERNAME "useradmin"
setx DB_PASSWORD "<tu_password>"
```

## Endpoints principales

- `POST /api/franchises`
- `POST /api/franchises/{franchiseId}/branches`
- `POST /api/branches/{branchId}/products`
- `DELETE /api/branches/{branchId}/products/{productId}`
- `PATCH /api/products/{productId}/stock`
- `GET /api/franchises/{franchiseId}/top-stock-products`
- `PATCH /api/franchises/{franchiseId}/name`
- `PATCH /api/branches/{branchId}/name`
- `PATCH /api/products/{productId}/name`

## Documentacion de API (Swagger)

Al desplegar en nube, reemplaza `<tu-app>` por el host publicado:

- `https://<tu-app>/swagger-ui/index.html`
- `https://<tu-app>/v3/api-docs`

