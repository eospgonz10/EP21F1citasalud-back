# EP21F1citasalud-back
Proyecto Fabrica Escuela 2025-1 Citas Salud Feature 1. Desarrollando solución web de auto agendamiento médico que permita a los usuarios de EPS gestionar sus citas de forma autónoma, eficiente y segura, mejorando los procesos administrativos de la IPS y cumpliendo con la normativa del sistema de salud colombiano.

## Configuración de Variables de Entorno

Este proyecto utiliza variables de entorno para gestionar la configuración sensible. Para configurar el proyecto:

1. Crea un archivo `.env` en la raíz del proyecto siguiendo el formato del archivo `.env.example`
2. Configura las siguientes variables (todas son **obligatorias**)
3. La aplicación validará que estas variables estén configuradas al iniciar

### Variables Requeridas

| Variable          | Descripción                                  | Ejemplo                                        |
|-------------------|----------------------------------------------|------------------------------------------------|
| DB_URL            | URL de conexión a la base de datos           | jdbc:postgresql://localhost:5432/bd_citassalud |
| DB_USERNAME       | Usuario de la base de datos                  | postgres                                       |
| DB_PASSWORD       | Contraseña de la base de datos               | tu_contraseña                                  |
| JWT_SECRET        | Clave secreta para firmar tokens JWT         | clave_secreta_larga_y_segura                   |
| JWT_EXPIRATION    | Tiempo de expiración del token en ms         | 86400000 (24 horas)                            |
| SERVER_PORT       | Puerto del servidor                          | 8080                                           |
| CONTEXT_PATH      | Ruta base para los endpoints de la API       | /api                                           |
| SWAGGER_SERVER_URL| URL base para la documentación Swagger       | http://localhost:8080/api                      |

### Variables Opcionales

| Variable          | Descripción                                  | Ejemplo                                        |
|-------------------|----------------------------------------------|------------------------------------------------|
| ALLOWED_ORIGINS   | Lista de orígenes permitidos para CORS (separados por coma). Si está vacío o no se define, se permitirán todos los orígenes ("*"). | http://localhost:3000,http://localhost:4200 |

## Configuración de CORS

Este proyecto incluye una configuración CORS (Cross-Origin Resource Sharing) configurable a través de variables de entorno:

- Se puede establecer una lista de orígenes permitidos separados por comas en la variable `ALLOWED_ORIGINS`.
- Si la variable está vacía o no se define, se permitirán todos los orígenes ("*").

Ejemplo:
```
ALLOWED_ORIGINS=http://localhost:3000,http://tudominio.com,https://app.ejemplo.org
```

La configuración se gestiona desde el archivo `CorsConfig.java` que utiliza la variable de entorno para restringir qué dominios pueden consumir la API. Esta es una práctica recomendada para mejorar la seguridad de la aplicación.

## HATEOAS (Hypermedia As The Engine Of Application State)

El proyecto implementa HATEOAS para facilitar la navegación y descubrimiento de la API. HATEOAS permite que los clientes descubran dinámicamente las acciones disponibles para cada recurso mediante enlaces hipermedia incluidos en las respuestas.

### Características implementadas

- Enlaces auto-descriptivos que conectan recursos relacionados
- Relaciones nombradas que describen la semántica de cada enlace
- Respuestas enriquecidas que incluyen los datos y enlaces de navegación

### Ejemplo de respuesta HATEOAS

```json
{
    "usuarioId": 1,
    "nombre": "Juan",
    "apellido": "Pérez",
    "email": "juan.perez@example.com",
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/usuarios/1"
        },
        "todos-los-usuarios": {
            "href": "http://localhost:8080/api/usuarios"
        },
        "actualizar": {
            "href": "http://localhost:8080/api/usuarios/1"
        }
    }
}
```

### Beneficios

- **Descubrimiento de API**: Los clientes pueden descubrir las capacidades de la API en tiempo de ejecución
- **Navegabilidad**: Facilita la navegación entre recursos relacionados 
- **Evolución de la API**: Permite que la API evolucione sin romper clientes existentes
- **Auto-documentación**: Las respuestas incluyen información sobre las acciones disponibles

## Spring Actuator

El proyecto utiliza Spring Actuator para monitoreo, administración y descubrimiento de endpoints. Actuator proporciona varios endpoints HTTP para interactuar con la aplicación en tiempo de ejecución.

### Endpoints disponibles

| Endpoint | Descripción |
|----------|-------------|
| `/api/actuator/health` | Muestra información sobre el estado de la aplicación |
| `/api/actuator/info` | Muestra información general sobre la aplicación |
| `/api/actuator/mappings` | Muestra todos los endpoints disponibles en la aplicación |

### Configuración

La configuración de Actuator se realiza en `application.properties`:

```properties
management.endpoints.web.exposure.include=health,info,mappings
management.endpoint.health.show-details=always
management.endpoints.web.base-path=/api/actuator
```

### Acceso y seguridad de Actuator

Para facilitar el uso de Actuator, los endpoints están disponibles públicamente sin autenticación en:

```
/api/actuator/health
/api/actuator/info
/api/actuator/mappings
```

Esta configuración está definida en `SecurityConfig.java`, donde se permite el acceso a todos los endpoints bajo `/api/actuator/**` sin requerir autenticación para facilitar el monitoreo y consulta de la API.

## Configuración de Swagger

La documentación de la API está disponible a través de Swagger UI. Puedes acceder a ella a través de:

```
/doc/swagger-ui.html
```

La configuración de Swagger soporta múltiples servidores:

1. Un servidor de producción definido por la variable de entorno `SWAGGER_SERVER_URL`
2. Un servidor de desarrollo local (`http://localhost:8080/api`)

Esto permite que la documentación funcione correctamente tanto en entornos de desarrollo como de producción.

## Solución de Problemas Comunes

### Acceso a Swagger UI

Si experimentas el error "Failed to load remote configuration" al acceder a Swagger UI:

1. **Verifica la URL correcta**: La URL para acceder a Swagger UI es:
   ```
   https://[tu-url-github-codespaces]/doc/swagger-ui.html
   ```
   También puedes acceder directamente desde la ruta raíz `/` que te redirigirá automáticamente.

2. **Configuración del CORS**: Asegúrate de que tu URL de GitHub Codespaces esté en la lista de `ALLOWED_ORIGINS` en el archivo `.env`.

3. **URL del servidor en Swagger**: Verifica que `SWAGGER_SERVER_URL` en `.env` esté configurado con la URL base correcta sin `/api` al final:
   ```
   SWAGGER_SERVER_URL=https://[tu-url-github-codespaces]
   ```

4. **Limpia la caché del navegador**: En casos persistentes, limpia la caché del navegador o usa el modo incógnito.

### Problemas con HATEOAS

Si los enlaces HATEOAS no funcionan correctamente:

1. **Verificar content-type**: Las respuestas deben tener `Content-Type: application/hal+json`

2. **Actualiza tu cliente**: Asegúrate de que tu cliente REST acepta el formato HAL:
   ```
   Accept: application/hal+json
   ```

3. **Valida la estructura de los enlaces**: Los enlaces deben seguir la estructura de HAL:
   ```json
   "_links": {
     "self": { "href": "..." },
     "next": { "href": "..." }
   }
   ```

### Problemas con Actuator

Para solucionar problemas con los endpoints de Actuator:

1. Accede directamente a través de `/api/actuator` para ver los endpoints disponibles
2. Verifica que estén configurados como públicos en la configuración de seguridad
3. Si necesitas endpoints adicionales, agrégalos a `management.endpoints.web.exposure.include`

## Solución a Problemas Específicos

### Error HttpMediaTypeNotAcceptableException

Si enfrentas el error `No acceptable representation` o `HttpMediaTypeNotAcceptableException`:

1. **Verifica el encabezado Accept**: Asegúrate de que tu cliente esté enviando un encabezado Accept compatible:
   ```
   Accept: application/json, application/hal+json
   ```

2. **Prueba los endpoints de verificación**: Usa los siguientes endpoints para verificar que la API responde correctamente:
   ```
   GET /api/test
   GET /api/status
   ```

3. **Reinicia la aplicación**: Después de modificar la configuración, reinicia completamente la aplicación.

### Acceso a Swagger UI desde Codespaces

Para acceder a Swagger UI desde GitHub Codespaces, utiliza la siguiente URL:

```
https://[tu-url-codespaces]/doc/swagger-ui.html
```

Por ejemplo, si tu URL de Codespaces es `https://super-duper-fishstick-r44gjvprrrgwfw5vj-8080.app.github.dev`, 
entonces la URL de Swagger UI sería: 
```
https://super-duper-fishstick-r44gjvprrrgwfw5vj-8080.app.github.dev/doc/swagger-ui.html
```

### Endpoints disponibles sin autenticación

Los siguientes endpoints están disponibles sin necesidad de autenticación:

- `/auth/login` - Autenticación de usuario
- `/api/test` - Endpoint de prueba para verificar la API
- `/api/status` - Verificación del estado de la API

