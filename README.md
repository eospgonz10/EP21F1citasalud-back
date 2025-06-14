# EP21F1citasalud-back
Proyecto Fabrica Escuela 2025-1 Citas Salud Feature 1. Desarrollando solución web de auto agendamiento médico que permita a los usuarios de EPS gestionar sus citas de forma autónoma, eficiente y segura, mejorando los procesos administrativos de la IPS y cumpliendo con la normativa del sistema de salud colombiano.

## 📋 Tabla de Contenidos

- [Prerrequisitos del Sistema](#-prerrequisitos-del-sistema)
- [Instalación y Configuración](#-instalación-y-configuración)
- [Configuración de Variables de Entorno](#-configuración-de-variables-de-entorno)
- [Comandos de Ejecución](#-comandos-de-ejecución)
- [Despliegue](#-despliegue)
- [Características del Proyecto](#-características-del-proyecto)
- [Endpoints Disponibles](#-endpoints-disponibles)
- [Solución de Problemas](#-solución-de-problemas)

## 🔧 Prerrequisitos del Sistema

Antes de ejecutar el proyecto, asegúrate de tener instalado:

- **Java 17** o superior
- **PostgreSQL 12+** (con una base de datos configurada)
- **Maven 3.8+** (opcional, el proyecto incluye Maven Wrapper)
- **Git** para clonar el repositorio

### Verificación de Prerrequisitos

```bash
# Verificar versión de Java
java -version

# Verificar versión de PostgreSQL
psql --version

# Verificar Maven (opcional)
mvn -version
```

## 🚀 Instalación y Configuración

### 1. Clonar el Repositorio

```bash
git clone [URL_DEL_REPOSITORIO]
cd EP21F1citasalud-back
```

### 2. Configurar la Base de Datos

```sql
-- Conectarse a PostgreSQL y crear la base de datos
CREATE DATABASE bd_citassalud;
CREATE USER citasalud_user WITH PASSWORD 'tu_contraseña_segura';
GRANT ALL PRIVILEGES ON DATABASE bd_citassalud TO citasalud_user;
```

### 3. Configurar Variables de Entorno

```bash
# Copiar el archivo de ejemplo
cp .env.example .env

# Editar el archivo .env con tus configuraciones
nano .env
```

## 🔐 Configuración de Variables de Entorno

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

## ⚡ Comandos de Ejecución

### Desarrollo Local

```bash
# Compilar el proyecto
./mvnw clean compile

# Ejecutar tests
./mvnw test

# Ejecutar la aplicación en modo desarrollo
./mvnw spring-boot:run

# Ejecutar con perfil de desarrollo específico
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Construcción y Empaquetado

```bash
# Construir el proyecto (genera el JAR)
./mvnw clean package

# Construir saltando los tests (más rápido)
./mvnw clean package -DskipTests

# Ejecutar el JAR generado
java -jar target/EP21F1citasalud-back-0.0.1-SNAPSHOT.jar
```

### Comandos Útiles para Desarrollo

```bash
# Limpiar archivos generados
./mvnw clean

# Verificar dependencias
./mvnw dependency:tree

# Generar documentación del proyecto
./mvnw site

# Modo de desarrollo con recarga automática (si tienes spring-boot-devtools)
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=dev"
```

## 🚀 Despliegue

### Despliegue en Desarrollo

```bash
# Configurar variables de entorno para desarrollo
export DB_URL=jdbc:postgresql://localhost:5432/bd_citassalud
export DB_USERNAME=citasalud_user
export DB_PASSWORD=tu_contraseña
export JWT_SECRET=tu_clave_secreta_jwt_desarrollo_muy_larga_y_segura
export JWT_EXPIRATION=86400000
export SERVER_PORT=8080
export CONTEXT_PATH=/api
export SWAGGER_SERVER_URL=http://localhost:8080
export ALLOWED_ORIGINS=http://localhost:3000,http://localhost:4200

# Ejecutar la aplicación
./mvnw spring-boot:run
```

### Despliegue en GitHub Codespaces

```bash
# Configurar variables para Codespaces (reemplaza con tu URL)
export SWAGGER_SERVER_URL=https://tu-codespace-url.app.github.dev
export ALLOWED_ORIGINS=https://tu-codespace-url.app.github.dev

# Ejecutar la aplicación
./mvnw spring-boot:run
```

### Despliegue en Producción

```bash
# Construir para producción
./mvnw clean package -Dspring.profiles.active=prod

# Ejecutar con perfil de producción
java -jar target/EP21F1citasalud-back-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### Despliegue con Docker (Opcional)

```dockerfile
# Ejemplo de Dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/EP21F1citasalud-back-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
```

```bash
# Comandos Docker
docker build -t citasalud-backend .
docker run -p 8080:8080 --env-file .env citasalud-backend
```

## 🎯 Características del Proyecto

### Tecnologías Utilizadas

- **Spring Boot 3.3.11** - Framework principal
- **Java 17** - Lenguaje de programación
- **PostgreSQL** - Base de datos
- **Spring Security** - Seguridad y autenticación
- **JWT** - Tokens de autenticación
- **Spring HATEOAS** - Hipermedia REST
- **Spring Actuator** - Monitoreo y métricas
- **Swagger/OpenAPI** - Documentación de API
- **MapStruct** - Mapeo de DTOs

## 📡 Endpoints Disponibles

### Endpoints Públicos (Sin Autenticación)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/auth/login` | Autenticación de usuario |
| GET | `/api/test` | Endpoint de prueba |
| GET | `/api/status` | Estado de la API |
| GET | `/api/actuator/health` | Estado de la aplicación |
| GET | `/api/actuator/info` | Información de la aplicación |
| GET | `/api/actuator/mappings` | Listado de todos los endpoints |

### Documentación

| Endpoint | Descripción |
|----------|-------------|
| `/doc/swagger-ui.html` | Interfaz de Swagger UI |
| `/api/v3/api-docs` | Documentación OpenAPI en JSON |
| `/` | Redirección automática a Swagger |

### Verificación de la API

```bash
# Verificar que la API está ejecutándose
curl http://localhost:8080/api/status

# Verificar salud de la aplicación
curl http://localhost:8080/api/actuator/health

# Probar endpoint de prueba
curl http://localhost:8080/api/test
```

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

## 📖 Configuración de Swagger

La documentación de la API está disponible a través de Swagger UI. Puedes acceder a ella a través de:

```
/doc/swagger-ui.html
```

La configuración de Swagger soporta múltiples servidores:

1. Un servidor de producción definido por la variable de entorno `SWAGGER_SERVER_URL`
2. Un servidor de desarrollo local (`http://localhost:8080/api`)

Esto permite que la documentación funcione correctamente tanto en entornos de desarrollo como de producción.

## 🔧 Solución de Problemas

### Problemas de Configuración Inicial

#### Error: "Could not connect to database"

```bash
# Verificar que PostgreSQL esté ejecutándose
sudo systemctl status postgresql

# Verificar conexión a la base de datos
psql -h localhost -U citasalud_user -d bd_citassalud

# Verificar variables de entorno
echo $DB_URL
echo $DB_USERNAME
```

#### Error: "Port 8080 already in use"

```bash
# Encontrar el proceso que usa el puerto
lsof -i :8080

# Cambiar el puerto en el archivo .env
SERVER_PORT=8081

# O usar un puerto diferente al ejecutar
./mvnw spring-boot:run -Dserver.port=8081
```

#### Error: "JWT Secret not configured"

```bash
# Generar una clave JWT segura
openssl rand -base64 64

# Agregar al archivo .env
JWT_SECRET=tu_clave_generada_aqui
```

### Problemas de Ejecución

#### La aplicación no inicia

1. **Verificar Java**: `java -version` (debe ser 17+)
2. **Limpiar y recompilar**: `./mvnw clean compile`
3. **Verificar variables de entorno**: Revisa que todas las variables requeridas estén configuradas
4. **Revisar logs**: Los errores aparecen en la consola al ejecutar

#### Tests fallan

```bash
# Ejecutar tests con más información
./mvnw test -X

# Ejecutar un test específico
./mvnw test -Dtest=NombreDelTest

# Saltar tests si es necesario para desarrollo
./mvnw spring-boot:run -DskipTests
```

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

## 📚 Información Adicional

### Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/udea/EP21F1citasalud_back/
│   │   ├── config/          # Configuraciones (CORS, Security, etc.)
│   │   ├── controller/      # Controladores REST
│   │   ├── DTO/            # Data Transfer Objects
│   │   ├── entity/         # Entidades JPA
│   │   ├── exceptions/     # Manejo de excepciones
│   │   ├── mapper/         # Mappers con MapStruct
│   │   ├── repository/     # Repositorios JPA
│   │   ├── security/       # Configuración de seguridad
│   │   └── service/        # Lógica de negocio
│   └── resources/
│       ├── application.properties
│       ├── application-dev.properties
│       └── data.sql        # Datos iniciales
```

### Comandos Maven Wrapper

```bash
# En Linux/Mac
./mvnw [comando]

# En Windows
mvnw.cmd [comando]

# Dar permisos de ejecución si es necesario (Linux/Mac)
chmod +x mvnw
```

### Variables de Entorno por Ambiente

#### Desarrollo Local
```bash
DB_URL=jdbc:postgresql://localhost:5432/bd_citassalud
SWAGGER_SERVER_URL=http://localhost:8080
ALLOWED_ORIGINS=http://localhost:3000,http://localhost:4200
```

#### GitHub Codespaces
```bash
DB_URL=jdbc:postgresql://localhost:5432/bd_citassalud
SWAGGER_SERVER_URL=https://tu-codespace-url.app.github.dev
ALLOWED_ORIGINS=https://tu-codespace-url.app.github.dev
```

#### Producción
```bash
DB_URL=jdbc:postgresql://tu-servidor-prod:5432/bd_citassalud
SWAGGER_SERVER_URL=https://tu-dominio.com
ALLOWED_ORIGINS=https://tu-frontend.com
```

### Consejos de Desarrollo

1. **Usa el perfil de desarrollo**: Agrega `spring.profiles.active=dev` para logs más detallados
2. **Hot Reload**: Considera agregar `spring-boot-devtools` para reinicio automático
3. **Debugging**: Usa `-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005` para debug remoto
4. **Monitoring**: Los endpoints de Actuator están disponibles en `/api/actuator/`

### Enlaces Útiles

- **Swagger UI**: `http://localhost:8080/doc/swagger-ui.html`
- **Actuator Health**: `http://localhost:8080/api/actuator/health`
- **OpenAPI Docs**: `http://localhost:8080/api/v3/api-docs`

### Soporte

Si encuentras problemas no cubiertos en esta documentación:

1. Revisa los logs de la aplicación
2. Verifica que todas las variables de entorno estén configuradas
3. Asegúrate de que PostgreSQL esté ejecutándose
4. Verifica que el puerto no esté en uso por otra aplicación

---

**Última actualización**: Junio 2025  
**Versión del proyecto**: 0.0.1-SNAPSHOT  
**Spring Boot**: 3.3.11  
**Java**: 17

