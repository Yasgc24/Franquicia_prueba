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

### Opción 2: MongoDB Local

1. Instala MongoDB en tu máquina local.
2. Inicia el servicio de MongoDB.

### Configuración de Variables de Entorno

Para MongoDB Atlas, debes modificar el archivo `application.properties`, y configurar las variables de entorno:

```properties
spring.data.mongodb.uri=mongodb+srv://${MONGODB_USER}:${MONGODB_PASSWORD}@${MONGODB_CLUSTER}.mongodb.net/franquicias_db?retryWrites=true&w=majority
```

Asegúrate de definir estas variables de entorno:
```
MONGODB_USER=tu_usuario
MONGODB_PASSWORD=tu_contraseña
MONGODB_CLUSTER=tu_cluster.mongodb.net
```

Para MongoDB local, puedes modificar el archivo `application.properties` para usar una conexión local:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/franquicias_db
```

## Ejecución del Proyecto

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

### Ejecución con Docker

1. Construye la imagen Docker:

```bash
docker build -t franquicia-api .
```

2. Ejecuta un contenedor con la imagen creada:

```bash
docker run -p 8080:8080 \
  -e MONGODB_USER=tu_usuario \
  -e MONGODB_PASSWORD=tu_contraseña \
  -e MONGODB_CLUSTER=tu_cluster.mongodb.net \
  franquicia-api
```

La aplicación estará disponible en `http://localhost:8080`.

## Verificación de la Instalación

Para verificar que la API está funcionando correctamente, puedes realizar una petición POST a uno de los endpoints de la API:

```bash
curl -X POST http://localhost:8080/api/franquicias \
  -H "Content-Type: application/json" \
  -d '{"nombre": "Mi Franquicia"}'
```

Deberías recibir una respuesta JSON con la franquicia creada.

## Funcionalidades

- Agregar franquicias, sucursales y productos.
- Modificar nombres de franquicias, sucursales y productos.
- Modificar el stock de productos.
- Eliminar productos.
- Consultar el producto con más stock por sucursal dentro de una franquicia.

## Tecnologías

| Herramienta                              | Uso                                    |
| ---------------------------------------- | -------------------------------------- |
| Java 17                                  | Lenguaje de programación               |
| Spring Boot 3 + WebFlux                  | Framework backend reactivo             |
| MongoDB                                  | Base de datos                          |
| Gradle                                   | Gestión de dependencias y construcción |
| Docker *(opcional)*                      | Empaquetado de la aplicación           |
| JUnit 5                                  | Pruebas unitarias                      |

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

## Manejo de Errores

La API maneja los siguientes escenarios de error:

| Código HTTP | Descripción                                          | Formato de Respuesta                                    |
| ----------- | ---------------------------------------------------- | ------------------------------------------------------- |
| 400         | Solicitud incorrecta (datos inválidos)               | `{"mensaje": "Descripción del error"}`                  |
| 404         | Recurso no encontrado                                | `{"mensaje": "El recurso {id} no fue encontrado"}`      |
| 500         | Error interno del servidor                           | `{"mensaje": "Error interno del servidor"}`             |

## API REST

| Método   | URL                                                                                     | Descripción                                  |
| -------- | --------------------------------------------------------------------------------------- | -------------------------------------------- |
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

## Gestión de Franquicias

### Crear una nueva franquicia

**Endpoint**: `POST /api/franquicias`

**Descripción**: Crea una nueva franquicia en el sistema.

**Ejemplo de petición**:
```http
POST /api/franquicias
Content-Type: application/json

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

**Ejemplo de petición**:
```http
PUT /api/franquicias/645a32b8c12d4f5e6a7b8c9d/nombre
Content-Type: application/json

"Franquicia principal"
```

**Ejemplo de respuesta**:
```json
{
  "id": "645a32b8c12d4f5e6a7b8c9d",
  "nombre": "Franquicia principal",
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

**Ejemplo de petición**:
```http
POST /api/franquicias/645a32b8c12d4f5e6a7b8c9d/sucursales
Content-Type: application/json

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

**Ejemplo de petición**:
```http
PUT /api/franquicias/645a32b8c12d4f5e6a7b8c9d/sucursales/645a33c9d12e5f6a7b8c9d0/nombre
Content-Type: application/json

"Sucursal principal"
```

**Ejemplo de respuesta**:
```json
{
  "id": "645a32b8c12d4f5e6a7b8c9d",
  "nombre": "Franquicia 1",
  "sucursales": [
    {
      "id": "645a33c9d12e5f6a7b8c9d0",
      "nombre": "Sucursal principal",
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

**Ejemplo de petición**:
```http
POST /api/franquicias/645a32b8c12d4f5e6a7b8c9d/sucursales/645a33c9d12e5f6a7b8c9d0/productos
Content-Type: application/json

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

**Ejemplo de petición**:
```http
PUT /api/franquicias/645a32b8c12d4f5e6a7b8c9d/sucursales/645a33c9d12e5f6a7b8c9d0/productos/645a34dae23f6g7h8i9j0k1/nombre
Content-Type: application/json

"Helados de chocolate"
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

**Ejemplo de petición**:
```http
PUT /api/franquicias/645a32b8c12d4f5e6a7b8c9d/sucursales/645a33c9d12e5f6a7b8c9d0/productos/645a34dae23f6g7h8i9j0k1/stock
Content-Type: application/json

30
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
          "stock": 30
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

**Ejemplo de petición**:
```http
DELETE /api/franquicias/645a32b8c12d4f5e6a7b8c9d/sucursales/645a33c9d12e5f6a7b8c9d0/productos/645a34dae23f6g7h8i9j0k1
```

**Respuesta**: No contiene cuerpo.

**Código de estado**: 
- 204 (No Content) si la eliminación fue exitosa
- 404 (Not Found) si la franquicia, la sucursal o el producto no existen

### Obtener productos con más stock por sucursal

**Endpoint**: `GET /api/franquicias/{franquiciaId}/productos/mas-stock`

**Descripción**: Devuelve el producto con mayor stock de cada sucursal de una franquicia.

**Ejemplo de petición**:
```http
GET /api/franquicias/645a32b8c12d4f5e6a7b8c9d/productos/mas-stock
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

Esta API actualmente no implementa mecanismos de autenticación ni autorización.

## Contacto

Yasmin Giraldo Castaño
