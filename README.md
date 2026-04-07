# franchise_api

API reactiva (Spring WebFlux + R2DBC MySQL) para gestión de franquicias, sucursales y productos.

## Requisitos

- Java 21
- Maven Wrapper (`mvnw.cmd`)
- MySQL (Azure MySQL Flexible Server o local)

## Ejecutar local

```powershell
Set-Location "C:\Users\juanj\Documents\Dev\Java\Frameworks\SpringBoot\franchise_api"
.\mvnw.cmd clean test
.\mvnw.cmd spring-boot:run
```

## Credenciales y seguridad

Actualmente `application.properties` define conexión directa. Para entrega pública se recomienda mover usuario/password a variables de entorno.

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
