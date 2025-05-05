# API de Gestión de Franquicias

Este proyecto es una API desarrollada con Spring Boot (WebFlux) y MongoDB para gestionar franquicias, sucursales y productos.

## Descripción del Proyecto
Franquicia API es una aplicación Spring Boot que proporciona un servicio REST para la gestión de franquicias, sucursales y productos. Utiliza Spring WebFlux para la programación reactiva y MongoDB como base de datos.

## Requisitos Previos
- Java 17 o superior
- Gradle 8.0 o superior
- MongoDB (local o MongoDB Atlas)
- Docker (opcional, para ejecución en contenedor)

## Clonación del Repositorio

Para obtener una copia del proyecto en tu máquina local, ejecuta el siguiente comando:

```bash
git clone https://github.com/Yasgc24/Franquicia_prueba.git
cd Franquicia_prueba
```

## Configuración de la Base de Datos

La aplicación está configurada para conectarse a MongoDB Atlas o de manera local según tus necesidades.

### Opción 1: MongoDB Atlas (Nube)

1. Crea una cuenta en [MongoDB Atlas](https://www.mongodb.com/cloud/atlas).
2. Crea un nuevo cluster.
3. En la sección "Database Access", crea un nuevo usuario con permisos de lectura y escritura.
4. En la sección "Network Access", añade tu dirección IP a la lista de direcciones permitidas.
5. En la sección "Clusters", haz clic en "Connect" y selecciona "Connect your application".
6. Copia la cadena de conexión proporcionada.

Para MongoDB Atlas, debes modificar el archivo `application.properties`, y configurar las variables de entorno:

```properties
spring.data.mongodb.uri=mongodb+srv://${MONGODB_USER}:${MONGODB_PASSWORD}@${MONGODB_CLUSTER}.mongodb.net/franquicias_db?retryWrites=true&w=majority
```

Crea un archivo `.env` en la raíz del proyecto, con las siguientes variables:
```.env
MONGODB_USER=tu_usuario
MONGODB_PASSWORD=tu_contraseña
MONGODB_CLUSTER=tu_cluster.mongodb.net
JWT_SECRET=tu_clave_secreta_jwt_muy_segura
JWT_EXPIRATION=86400
```

### Opción 2: MongoDB Local

1. Instala MongoDB en tu máquina local.
2. Inicia el servicio de MongoDB.

Para MongoDB local, puedes modificar el archivo `application.properties` para usar una conexión local:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/test_db
```

## Tecnologías

| Herramienta                              | Uso                                    |
| ---------------------------------------- | -------------------------------------- |
| Java 17                                  | Lenguaje de programación               |
| Spring Boot 3 + WebFlux                  | Framework backend reactivo             |
| Spring Security                          | Seguridad y autenticación              |
| JWT (JSON Web Token)                     | Autenticación basada en tokens         |
| MongoDB                                  | Base de datos                          |
| Gradle                                   | Gestión de dependencias y construcción |
| Docker *(opcional)*                      | Empaquetado de la aplicación           |
| JUnit 5                                  | Pruebas unitarias                      |

## Ejecución del Proyecto

### Ejecución con Docker

1. Navega hasta el directorio raíz del proyecto.
2. Construye la imagen de Docker:

```bash
docker build -t franquicias_api .
```
3. Inicia la aplicación con Docker Compose:

```bash
docker-compose up
```

### Ejecución con Gradle

1. Navega hasta el directorio raíz del proyecto.
2. Ejecuta el siguiente comando para compilar y ejecutar la aplicación:

```bash
./gradlew bootRun
```

### Ejecución de Pruebas

```bash
./gradlew test
```

La aplicación estará disponible en `http://localhost:8080`.

## Sistema de Autenticación

La API utiliza autenticación basada en JWT (JSON Web Token) para proteger los endpoints. Todos los endpoints, excepto los de autenticación, requieren un token JWT válido.

### Configuración de Seguridad

El archivo `.env` debe incluir las siguientes variables para la configuración de JWT:

```
JWT_SECRET=clave_secreta_para_firmar_tokens
JWT_EXPIRATION=86400
```

- `JWT_SECRET`: Una cadena secreta utilizada para firmar los tokens JWT.
- `JWT_EXPIRATION`: Tiempo de expiración del token en segundos (86400 = 24 horas).

### Endpoints de Autenticación

#### Registro de Usuario

**Endpoint**: `POST /api/auth/register`

**Descripción**: Crea un nuevo usuario en el sistema.

**Ejemplo de petición**:
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "usuario1",
  "password": "contraseña123"
}
```

**Ejemplo de respuesta**:
```json
{
  "id": "645a32b8c12d4f5e6a7b8c9d",
  "username": "usuario1",
  "enabled": true
}
```

**Código de estado**: 201 (Created)

#### Inicio de Sesión

**Endpoint**: `POST /api/auth/login`

**Descripción**: Autentica un usuario y devuelve un token JWT.

**Ejemplo de petición**:
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "usuario1",
  "password": "contraseña123"
}
```

**Ejemplo de respuesta**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "usuario1"
}
```

**Código de estado**: 200 (OK)

### Uso del Token de Autenticación

Para usar el token en las peticiones a la API, debes incluirlo en la cabecera `Authorization` con el prefijo `Bearer `:

```http
GET /api/franquicias
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### Usuario Administrador por Defecto

La aplicación crea automáticamente un usuario administrador al iniciarse:

- **Username**: admin
- **Password**: admin123

Este usuario puede ser utilizado para pruebas iniciales.

## Verificación de la Instalación

Para verificar que la API está funcionando correctamente, primero debes obtener un token de autenticación:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin123"}'
```

Luego, usa el token para realizar una petición a un endpoint protegido:

```bash
curl -X POST http://localhost:8080/api/franquicias \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TU_TOKEN_JWT" \
  -d '{"nombre": "Mi Franquicia"}'
```

Deberías recibir una respuesta JSON con la franquicia creada.

## Funcionalidades

- Agregar franquicias, sucursales y productos.
- Modificar nombres de franquicias, sucursales y productos.
- Modificar el stock de productos.
- Eliminar productos.
- Consultar el producto con más stock por sucursal dentro de una franquicia.

## Modelo de Datos

### Franquicia
- **id**: String (ObjectId de MongoDB)
- **nombre**: String
- **sucursales**: Lista de objetos Sucursal

### Sucursal
- **id**: String (ObjectId de MongoDB)
- **nombre**: String
- **productos**: Lista de objetos Producto

### Producto
- **id**: String (ObjectId de MongoDB)
- **nombre**: String
- **stock**: Integer (cantidad disponible)

### Usuario
- **id**: String (ObjectId de MongoDB)
- **username**: String
- **password**: String (encriptado)
- **enabled**: Boolean

## DTOs (Data Transfer Objects)

### NombreDTO

- **nombre**: String (utilizado para actualizar nombres)

### StockDTO

- **stock**: Integer (utilizado para actualizar el stock de productos)

### ProductoConSucursalDTO

- **producto**: Objeto Producto
- **sucursalId**: String (ID de la sucursal)
- **sucursalNombre**: String (Nombre de la sucursal)

### AuthRequest

- **username**: String
- **password**: String

### AuthResponse

- **token**: String (token JWT)
- **username**: String

### RegisterRequest

- **username**: String
- **password**: String

## Manejo de Errores

La API maneja los siguientes errores:

| Código HTTP | Descripción                                          |
| ----------- | ---------------------------------------------------- |
| 400         | Solicitud incorrecta (datos inválidos)               |
| 401         | No autorizado (token inválido o expirado)            |
| 403         | Prohibido (sin permisos suficientes)                 |
| 404         | Recurso no encontrado                                |
| 500         | Error interno del servidor                           |

## API REST

| Método   | URL                                                                                     | Descripción                                  |
| -------- | --------------------------------------------------------------------------------------- | -------------------------------------------- |
| `POST`   | `/api/auth/register`                                                                    | Registrar nuevo usuario                      |
| `POST`   | `/api/auth/login`                                                                       | Iniciar sesión y obtener token               |
| `POST`   | `/api/franquicias`                                                                      | Crear una nueva franquicia                   |
| `PUT`    | `/api/franquicias/{franquiciaId}/nombre`                                                | Actualizar nombre franquicia                 |
| `POST`   | `/api/franquicias/{franquiciaId}/sucursales`                                            | Agregar sucursal                             |
| `PUT`    | `/api/franquicias/{franquiciaId}/sucursales/{sucursalId}/nombre`                        | Actualizar nombre sucursal                   |
| `POST`   | `/api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos`                     | Agregar producto                             |
| `PUT`    | `/api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}/nombre` | Actualizar nombre producto                   |
| `DELETE` | `/api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}`        | Eliminar producto                            |
| `PUT`    | `/api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}/stock`  | Modificar stock producto                     |
| `GET`    | `/api/franquicias/{franquiciaId}/productos/mas-stock`                                   | Obtener productos con más stock por sucursal |

# Guía de Endpoints y Ejemplos de Uso

Esta guía detalla todos los endpoints disponibles en la API del Sistema de Gestión de Franquicias, junto con ejemplos de peticiones y respuestas.

## Autenticación

### Registrar un nuevo usuario

**Endpoint**: `POST /api/auth/register`

**Descripción**: Crea un nuevo usuario en el sistema.

**Ejemplo de petición**:
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "usuario1",
  "password": "contraseña123"
}
```

**Ejemplo de respuesta**:
```json
{
  "id": "645a32b8c12d4f5e6a7b8c9d",
  "username": "usuario1",
  "enabled": true
}
```

**Código de estado**: 201 (Created)

### Iniciar sesión

**Endpoint**: `POST /api/auth/login`

**Descripción**: Autentica un usuario y devuelve un token JWT.

**Ejemplo de petición**:
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "usuario1",
  "password": "contraseña123"
}
```

**Ejemplo de respuesta**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "usuario1"
}
```

**Código de estado**: 200 (OK)

## Gestión de Franquicias

### Crear una nueva franquicia

**Endpoint**: `POST /api/franquicias`

**Descripción**: Crea una nueva franquicia en el sistema.

**Headers necesarios**:
```
Authorization: Bearer {tu_token_jwt}
```

**Ejemplo de petición**:
```http
POST /api/franquicias
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

{
  "nombre": "Franquicia 1"
}
```

**Ejemplo de respuesta**:
```json
{
  "id": "645a32b8c12d4f5e6a7b8c9d",
  "nombre": "Franquicia 1",
  "sucursales": []
}
```

**Código de estado**: 201 (Created)

### Actualizar el nombre de una franquicia

**Endpoint**: `PUT /api/franquicias/{franquiciaId}/nombre`

**Descripción**: Actualiza el nombre de una franquicia existente.

**Headers necesarios**:
```
Authorization: Bearer {tu_token_jwt}
```

**Ejemplo de petición**:
```http
PUT /api/franquicias/645a32b8c12d4f5e6a7b8c9d/nombre
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

{
  "nombre": "Franquicia Actualizada"
}
```

**Ejemplo de respuesta**:
```json
{
  "id": "645a32b8c12d4f5e6a7b8c9d",
  "nombre": "Franquicia Actualizada",
  "sucursales": []
}
```

**Código de estado**: 
- 200 (OK) si la actualización fue exitosa
- 404 (Not Found) si la franquicia no existe

## Gestión de Sucursales

### Agregar una sucursal a una franquicia

**Endpoint**: `POST /api/franquicias/{franquiciaId}/sucursales`

**Descripción**: Agrega una nueva sucursal a una franquicia existente.

**Headers necesarios**:
```
Authorization: Bearer {tu_token_jwt}
```

**Ejemplo de petición**:
```http
POST /api/franquicias/645a32b8c12d4f5e6a7b8c9d/sucursales
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

{
  "nombre": "Sucursal 1"
}
```

**Ejemplo de respuesta**:
```json
{
  "id": "645a32b8c12d4f5e6a7b8c9d",
  "nombre": "Franquicia 1",
  "sucursales": [
    {
      "id": "645a33c9d12e5f6a7b8c9d0",
      "nombre": "Sucursal 1",
      "productos": []
    }
  ]
}
```

**Código de estado**: 
- 201 (Created) si la sucursal se creó correctamente
- 404 (Not Found) si la franquicia no existe

### Actualizar el nombre de una sucursal

**Endpoint**: `PUT /api/franquicias/{franquiciaId}/sucursales/{sucursalId}/nombre`

**Descripción**: Actualiza el nombre de una sucursal existente.

**Headers necesarios**:
```
Authorization: Bearer {tu_token_jwt}
```

**Ejemplo de petición**:
```http
PUT /api/franquicias/645a32b8c12d4f5e6a7b8c9d/sucursales/645a33c9d12e5f6a7b8c9d0/nombre
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

{
  "nombre": "Sucursal Actualizada"
}
```

**Ejemplo de respuesta**:
```json
{
  "id": "645a32b8c12d4f5e6a7b8c9d",
  "nombre": "Franquicia 1",
  "sucursales": [
    {
      "id": "645a33c9d12e5f6a7b8c9d0",
      "nombre": "Sucursal Actualizada",
      "productos": []
    }
  ]
}
```

**Código de estado**: 
- 200 (OK) si la actualización fue exitosa
- 404 (Not Found) si la franquicia o la sucursal no existen

## Gestión de Productos

### Agregar un producto a una sucursal

**Endpoint**: `POST /api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos`

**Descripción**: Agrega un nuevo producto a una sucursal existente.

**Headers necesarios**:
```
Authorization: Bearer {tu_token_jwt}
```

**Ejemplo de petición**:
```http
POST /api/franquicias/645a32b8c12d4f5e6a7b8c9d/sucursales/645a33c9d12e5f6a7b8c9d0/productos
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

{
  "nombre": "Helados",
  "stock": 50
}
```

**Ejemplo de respuesta**:
```json
{
  "id": "645a32b8c12d4f5e6a7b8c9d",
  "nombre": "Franquicia 1",
  "sucursales": [
    {
      "id": "645a33c9d12e5f6a7b8c9d0",
      "nombre": "Sucursal 1",
      "productos": [
        {
          "id": "645a34dae23f6g7h8i9j0k1",
          "nombre": "Helados",
          "stock": 50
        }
      ]
    }
  ]
}
```

**Código de estado**: 
- 201 (Created) si el producto se creó correctamente
- 404 (Not Found) si la franquicia o la sucursal no existen

### Actualizar el nombre de un producto

**Endpoint**: `PUT /api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}/nombre`

**Descripción**: Actualiza el nombre de un producto existente.

**Headers necesarios**:
```
Authorization: Bearer {tu_token_jwt}
```

**Ejemplo de petición**:
```http
PUT /api/franquicias/645a32b8c12d4f5e6a7b8c9d/sucursales/645a33c9d12e5f6a7b8c9d0/productos/645a34dae23f6g7h8i9j0k1/nombre
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

{
  "nombre": "Producto Actualizado",
}
```

**Ejemplo de respuesta**:
```json
{
  "id": "645a32b8c12d4f5e6a7b8c9d",
  "nombre": "Franquicia 1",
  "sucursales": [
    {
      "id": "645a33c9d12e5f6a7b8c9d0",
      "nombre": "Sucursal 1",
      "productos": [
        {
          "id": "645a34dae23f6g7h8i9j0k1",
          "nombre": "Producto Actualizado",
          "stock": 50
        }
      ]
    }
  ]
}
```

**Código de estado**: 
- 200 (OK) si la actualización fue exitosa
- 404 (Not Found) si la franquicia, la sucursal o el producto no existen

### Modificar el stock de un producto

**Endpoint**: `PUT /api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}/stock`

**Descripción**: Modifica la cantidad de stock disponible de un producto.

**Headers necesarios**:
```
Authorization: Bearer {tu_token_jwt}
```

**Ejemplo de petición**:
```http
PUT /api/franquicias/645a32b8c12d4f5e6a7b8c9d/sucursales/645a33c9d12e5f6a7b8c9d0/productos/645a34dae23f6g7h8i9j0k1/stock
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

{
  "stock": 60
}
```

**Ejemplo de respuesta**:
```json
{
  "id": "645a32b8c12d4f5e6a7b8c9d",
  "nombre": "Franquicia 1",
  "sucursales": [
    {
      "id": "645a33c9d12e5f6a7b8c9d0",
      "nombre": "Sucursal 1",
      "productos": [
        {
          "id": "645a34dae23f6g7h8i9j0k1",
          "nombre": "Helados de chocolate",
          "stock": 60
        }
      ]
    }
  ]
}
```

**Código de estado**: 
- 200 (OK) si la actualización fue exitosa
- 404 (Not Found) si la franquicia, la sucursal o el producto no existen

### Eliminar un producto

**Endpoint**: `DELETE /api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}`

**Descripción**: Elimina un producto de una sucursal.

**Headers necesarios**:
```
Authorization: Bearer {tu_token_jwt}
```

**Ejemplo de petición**:
```http
DELETE /api/franquicias/645a32b8c12d4f5e6a7b8c9d/sucursales/645a33c9d12e5f6a7b8c9d0/productos/645a34dae23f6g7h8i9j0k1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Respuesta**: No contiene cuerpo.

**Código de estado**: 
- 204 (No Content) si la eliminación fue exitosa
- 404 (Not Found) si la franquicia, la sucursal o el producto no existen

### Obtener productos con más stock por sucursal

**Endpoint**: `GET /api/franquicias/{franquiciaId}/productos/mas-stock`

**Descripción**: Devuelve el producto con mayor stock de cada sucursal de una franquicia.

**Headers necesarios**:
```
Authorization: Bearer {tu_token_jwt}
```

**Ejemplo de petición**:
```http
GET /api/franquicias/645a32b8c12d4f5e6a7b8c9d/productos/mas-stock
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Ejemplo de respuesta**:
```json
[
  {
    "producto": {
      "id": "645a34dae23f6g7h8i9j0k1",
      "nombre": "Helados de chocolate",
      "stock": 30
    },
    "sucursalId": "645a33c9d12e5f6a7b8c9d0",
    "sucursalNombre": "Sucursal 1"
  },
  {
    "producto": {
      "id": "645a35ebf34g7h8i9j0k1l2",
      "nombre": "Helados de fresa",
      "stock": 45
    },
    "sucursalId": "645a34d0e23f6g7h8i9j0k2",
    "sucursalNombre": "Sucursal 2"
  }
]
```

**Código de estado**: 200 (OK)

## Seguridad

La API implementa las siguientes medidas de seguridad:

- Autenticación basada en JWT
- Contraseñas cifradas con BCrypt
- Control de acceso basado en tokens
- Protección contra CSRF desactivada para facilitar el uso de la API

## Contacto

Yasmin Giraldo Castaño