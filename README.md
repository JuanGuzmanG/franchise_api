# franchise_api

API reactiva (Spring WebFlux + R2DBC MySQL) para gestion de franquicias, sucursales y productos.

## Documentacion de API (Swagger)

- `https://franchiseapi-production.up.railway.app/swagger-ui/index.html`
- [Ingresa aquí (desplegado en Railway provisionalmente por fallos en cuota de capacidad de Azure)](https://franchiseapi-production.up.railway.app/swagger-ui/index.html)


## Requisitos

- Java 21
- Maven Wrapper (`mvnw.cmd`)
- MySQL compatible con R2DBC 
- Docker Desktop 

## Ejecutar local

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd spring-boot:run
```

## Ejecutar con Docker

Levanta la API usando variables de entorno para conectarse a tu MySQL.

- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

Detener y limpiar contenedores:

```powershell
docker compose down
```

## Despliegue en Railway

Railway puede construir el contenedor desde este repo con el `Dockerfile` del proyecto.

### Variables que debes configurar en Railway

- `R2DBC_URL` (ejemplo: `r2dbc:mysql://<mysql-host>:3306/<db>?sslMode=REQUIRED`)
- `R2DBC_PASSWORD`
- `PORT` (Railway la inyecta automaticamente; no la definas manualmente salvo pruebas locales)

Nota: en `application.properties` el usuario actual esta fijo en `root`, por lo que el MySQL de destino debe aceptar ese usuario o debes ajustar despues esa propiedad.

```powershell
$env:R2DBC_URL="r2dbc:mysql://<host>:3306/<database>?sslMode=REQUIRED"
$env:R2DBC_PASSWORD="<tu_password>"
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
