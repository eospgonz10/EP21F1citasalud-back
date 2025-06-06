# EP21F1citasalud-back

Proyecto Fabrica Escuela 2025-1 Citas Salud Feature 1. Desarrollando solución web de auto agendamiento médico que permita a los usuarios de EPS gestionar sus citas de forma autónoma, eficiente y segura, mejorando los procesos administrativos de la IPS y cumpliendo con la normativa del sistema de salud colombiano.

## 🚀 Características Principales

- **API REST** completa con Spring Boot 3.3.11
- **Autenticación JWT** con roles y permisos granulares
- **Monitoreo avanzado** con Prometheus y Grafana
- **Documentación automática** con Swagger/OpenAPI
- **Despliegue flexible** para desarrollo local y Render
- **HATEOAS** para navegación hipermedia
- **Spring Actuator** para observabilidad

## 🏗️ Arquitectura

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │────│   Spring Boot   │────│   PostgreSQL    │
│   (React/Vue)   │    │   REST API      │    │   Database      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                               │
                       ┌───────┴───────┐
                       │               │
               ┌───────▼──────┐ ┌──────▼───────┐
               │  Prometheus  │ │   Grafana    │
               │  (Metrics)   │ │ (Dashboard)  │
               └──────────────┘ └──────────────┘
```

## 📋 Tabla de Contenidos

- [Configuración Rápida](#configuración-rápida)
- [Desarrollo Local](#desarrollo-local)
- [Despliegue en Render](#despliegue-en-render)
- [Monitoreo y Métricas](#monitoreo-y-métricas)
- [Variables de Entorno](#variables-de-entorno)
- [Scripts Automatizados](#scripts-automatizados)
- [Documentación API](#documentación-api)
- [Solución de Problemas](#solución-de-problemas)

## ⚡ Configuración Rápida

### Prerrequisitos
- Java 17+
- Maven 3.6+
- Docker y Docker Compose (para desarrollo local)
- Git

### 1. Clonar y Configurar

```bash
# Clonar el repositorio
git clone <tu-repositorio>
cd EP21F1citasalud-back

# Copiar variables de entorno
cp .env.example .env

# Editar .env con tus valores específicos
nano .env
```

### 2. Desarrollo Local (Codespaces/Docker)

```bash
# Opción 1: Setup completo para Codespaces (SIN Docker)
./build-deploy.sh codespaces-setup

# Opción 2: Con Docker (si está disponible)
./build-deploy.sh full-local

# Opción 3: Paso a paso
mvn clean package -DskipTests
docker-compose -f docker-compose.prod.yml up -d
```

### 3. Desarrollo en Codespaces

El proyecto incluye configuración `.devcontainer` lista para usar:

```bash
# En GitHub Codespaces se inicia automáticamente
# Solo necesitas configurar el .env y ejecutar:
mvn spring-boot:run
```

## 🛠️ Desarrollo Local

### Servicios Incluidos

| Servicio | Puerto | URL | Descripción |
|----------|--------|-----|-------------|
| **Spring Boot API** | 8080 | http://localhost:8080/api | API principal |
| **PostgreSQL** | 5432 | localhost:5432 | Base de datos |
| **Swagger UI** | 8080 | http://localhost:8080/doc/swagger-ui.html | Documentación API |
| **Grafana** | 3000 | http://localhost:3000 | Dashboard de monitoreo |
| **Prometheus** | 9090 | http://localhost:9090 | Métricas |
| **Redis** | 6379 | localhost:6379 | Cache (opcional) |

### Comandos Útiles

```bash
# Compilar proyecto
mvn clean package

# Ejecutar tests
mvn test

# Ejecutar en modo desarrollo
mvn spring-boot:run

# Ver logs de contenedores
docker-compose -f docker-compose.prod.yml logs -f

# Reiniciar servicios
docker-compose -f docker-compose.prod.yml restart

# Limpiar volúmenes
docker-compose -f docker-compose.prod.yml down -v
```

## 🌐 Despliegue en Render

### Configuración Automática

1. **Conectar Repositorio**: Conecta tu repositorio de GitHub a Render

2. **Crear Web Service**:
   - Build Command: `mvn clean package -DskipTests`
   - Start Command: `java -jar target/EP21F1citasalud-back-0.0.1-SNAPSHOT.jar`
   - Environment: `Docker`

3. **Configurar Variables de Entorno**:

```bash
# Variables obligatorias en Render
SPRING_PROFILES_ACTIVE=prod
JWT_SECRET=tu_clave_secreta_super_larga_y_segura
JWT_EXPIRATION=86400000
DATABASE_URL=postgresql://usuario:password@host:puerto/database
SERVER_PORT=8080
CONTEXT_PATH=/api
SWAGGER_SERVER_URL=https://tu-app.onrender.com/api
```

### Base de Datos en Render

1. **Crear PostgreSQL Database** en Render
2. **Copiar DATABASE_URL** desde el dashboard
3. **Configurar en variables de entorno** del Web Service

### Script de Preparación

```bash
# Preparar para Render
./build-deploy.sh prepare-render
```

## 📊 Monitoreo y Métricas

### Prometheus

Métricas automáticas disponibles:
- **JVM**: Memoria, CPU, threads, garbage collection
- **HTTP**: Requests, response times, status codes
- **Database**: Pool de conexiones, queries
- **Custom**: Métricas de negocio específicas

```yaml
# Configuración en prometheus.yml
scrape_configs:
  - job_name: 'spring-boot-app'
    metrics_path: '/api/actuator/prometheus'
    static_configs:
      - targets: ['app:8080']
```

### Grafana Dashboards

#### Dashboard Incluido: "Citas Salud - Spring Boot Metrics"

- **HTTP Request Rate**: Requests por segundo por endpoint
- **CPU Usage**: Uso de CPU del sistema
- **JVM Memory**: Uso de memoria heap y non-heap
- **Response Times**: Latencia de endpoints
- **Database Connections**: Pool de conexiones activas

#### Acceso a Grafana

```
URL: http://localhost:3000
Usuario: admin
Contraseña: admin123 (configurable via GRAFANA_ADMIN_PASSWORD)
```

### Endpoints de Monitoreo

| Endpoint | Descripción |
|----------|-------------|
| `/api/actuator/health` | Estado de salud de la aplicación |
| `/api/actuator/metrics` | Métricas en formato JSON |
| `/api/actuator/prometheus` | Métricas en formato Prometheus |
| `/api/actuator/info` | Información de la aplicación |

## 📝 Variables de Entorno

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

## 🚀 Scripts Automatizados

El proyecto incluye el script `build-deploy.sh` para automatizar tareas comunes:

### Comandos Disponibles

```bash
# Mostrar ayuda
./build-deploy.sh help

# Construir el proyecto
./build-deploy.sh build

# Ejecutar tests
./build-deploy.sh test

# Setup completo para Codespaces (SIN Docker) - RECOMENDADO
./build-deploy.sh codespaces-setup

# Despliegue local completo (CON Docker)
./build-deploy.sh full-local

# Ejecutar solo la aplicación Spring Boot
./build-deploy.sh deploy-app-only

# Preparar para Render
./build-deploy.sh prepare-render

# Limpiar contenedores y volúmenes (solo con Docker)
./build-deploy.sh cleanup
```

### Ejemplos de Uso

```bash
# Desarrollo rápido
./build-deploy.sh build test

# Setup completo para desarrollo
./build-deploy.sh full-local

# Antes de hacer commit
./build-deploy.sh test cleanup
```

## 📖 Documentación API

### Swagger/OpenAPI

- **URL Local**: http://localhost:8080/doc/swagger-ui.html
- **URL Producción**: https://tu-app.onrender.com/doc/swagger-ui.html
- **Especificación OpenAPI**: `/api/v3/api-docs`

### Endpoints Principales

| Método | Endpoint | Descripción | Autenticación |
|--------|----------|-------------|---------------|
| POST | `/auth/login` | Autenticación de usuario | No |
| GET | `/api/usuarios` | Listar usuarios | Sí |
| POST | `/api/usuarios` | Crear usuario | Sí |
| GET | `/api/citas` | Listar citas | Sí |
| POST | `/api/citas` | Crear cita | Sí |
| GET | `/api/medicos` | Listar médicos | Sí |
| GET | `/api/especialidades` | Listar especialidades | Sí |

### Autenticación JWT

```bash
# 1. Obtener token
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password"}'

# 2. Usar token en requests
curl -X GET http://localhost:8080/api/usuarios \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 🏗️ Arquitectura de Perfiles

### Perfil de Desarrollo (`dev`)

```properties
# application-dev.properties
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=create-drop
logging.level.com.proyecto.citassalud=DEBUG
spring.datasource.hikari.maximum-pool-size=5
```

**Características:**
- Logging detallado (DEBUG)
- DDL automático para desarrollo
- Pool de conexiones pequeño
- SQL visible en logs

### Perfil de Producción (`prod`)

```properties
# application-prod.properties
spring.jpa.show-sql=false
spring.jpa.hibernate.ddl-auto=validate
logging.level.com.proyecto.citassalud=INFO
spring.datasource.hikari.maximum-pool-size=20
```

**Características:**
- Logging optimizado (INFO)
- Validación estricta de DDL
- Pool de conexiones escalable
- Configuración de performance

### Activación de Perfiles

```bash
# Desarrollo
export SPRING_PROFILES_ACTIVE=dev
mvn spring-boot:run

# Producción
export SPRING_PROFILES_ACTIVE=prod
java -jar target/EP21F1citasalud-back-0.0.1-SNAPSHOT.jar
```

## 🐳 Configuración Docker

### Docker Compose para Desarrollo

```yaml
# .devcontainer/docker-compose.yml
services:
  db:
    image: postgres:15
    ports: ["5432:5432"]
  
  prometheus:
    image: prom/prometheus:latest
    ports: ["9090:9090"]
  
  grafana:
    image: grafana/grafana:latest
    ports: ["3000:3000"]
  
  redis:
    image: redis:7-alpine
    ports: ["6379:6379"]
```

### Dockerfile para Producción

```dockerfile
# Multi-stage build optimizado
FROM openjdk:17-jdk-slim as builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jre-slim
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring
COPY --from=builder /app/target/*.jar app.jar
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/api/actuator/health || exit 1
ENTRYPOINT ["java","-jar","/app.jar"]
```

## 🔧 Configuración de Base de Datos

### PostgreSQL Local

```bash
# Conectar a la base de datos
docker exec -it postgres_container psql -U postgres -d bd_citassalud

# Crear backup
docker exec postgres_container pg_dump -U postgres bd_citassalud > backup.sql

# Restaurar backup
docker exec -i postgres_container psql -U postgres bd_citassalud < backup.sql
```

### PostgreSQL en Render

1. **Crear Database**: En Render Dashboard > New > PostgreSQL
2. **Configurar Variables**:
   ```bash
   DATABASE_URL=postgresql://user:pass@host:port/db_name
   ```
3. **Verificar Conexión**:
   ```bash
   psql $DATABASE_URL -c "SELECT version();"
   ```

### Migraciones

```bash
# Generar SQL para migración
mvn liquibase:updateSQL

# Aplicar migraciones manualmente
mvn liquibase:update

# Rollback última migración
mvn liquibase:rollback -Dliquibase.rollbackCount=1
```

## 🔐 Seguridad y JWT

### Configuración JWT

```java
// Configuración en application.properties
jwt.secret=${JWT_SECRET:default_secret_key}
jwt.expiration=${JWT_EXPIRATION:86400000}
```

### Roles y Permisos

| Rol | Permisos |
|-----|----------|
| **ADMIN** | Gestión completa del sistema |
| **MEDICO** | Gestión de citas y pacientes |
| **PACIENTE** | Consulta y agendamiento de citas |

### Endpoints Protegidos

```java
// Configuración en SecurityConfig.java
.requestMatchers("/auth/**").permitAll()
.requestMatchers("/api/actuator/**").permitAll()
.requestMatchers("/doc/**").permitAll()
.anyRequest().authenticated()
```

## 🚨 Solución de Problemas Avanzados

### Problemas con Docker en Codespaces

Si experimentas problemas con Docker en GitHub Codespaces:

#### Opción 1: Usar la aplicación sin Docker (RECOMENDADO)
```bash
./build-deploy.sh codespaces-setup
```

Este comando:
- Compila el proyecto con Maven
- Configura variables de entorno automáticamente
- Ejecuta solo la aplicación Spring Boot
- **No requiere Docker**

#### Opción 2: Solucionar Docker
```bash
# 1. Intentar iniciar Docker manualmente
dockerd &

# 2. Esperar unos segundos y verificar
sleep 10 && docker info

# 3. Si funciona, usar el comando completo
./build-deploy.sh full-local
```

#### Opción 3: Rebuild Container
1. Presiona `Ctrl+Shift+P`
2. Busca "Dev Containers: Rebuild Container"
3. Espera a que se reconstruya
4. Intenta nuevamente `./build-deploy.sh full-local`

### Problemas de Memoria

```bash
# Configurar JVM en producción
JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC"

# Monitorear memoria
curl http://localhost:8080/api/actuator/metrics/jvm.memory.used
```

### Problemas de Conexión DB

```bash
# Verificar pool de conexiones
curl http://localhost:8080/api/actuator/metrics/hikaricp.connections.active

# Test de conexión manual
curl http://localhost:8080/api/actuator/health/db
```

### Debugging de Aplicación

```bash
# Ejecutar con debug remoto
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"

# Logs detallados
export LOGGING_LEVEL_ROOT=DEBUG
mvn spring-boot:run
```

## 📊 Métricas Personalizadas

### Agregar Métricas de Negocio

```java
@Component
public class CitasMetrics {
    private final Counter citasCreadas = Counter.builder("citas.creadas")
        .description("Total de citas creadas")
        .register(Metrics.globalRegistry);
    
    public void incrementarCitasCreadas() {
        citasCreadas.increment();
    }
}
```

### Dashboard Custom de Grafana

```json
{
  "dashboard": {
    "title": "Citas Salud - Métricas de Negocio",
    "panels": [
      {
        "title": "Citas por Hora",
        "type": "graph",
        "targets": [
          {
            "expr": "rate(citas_creadas_total[1h])"
          }
        ]
      }
    ]
  }
}
```

## 🤝 Contribución

### Configuración para Desarrollo

1. **Fork del repositorio**
2. **Crear rama feature**:
   ```bash
   git checkout -b feature/nueva-funcionalidad
   ```
3. **Configurar pre-commit hooks**:
   ```bash
   ./build-deploy.sh test
   ```
4. **Enviar Pull Request**

### Estándares de Código

- **Java**: Seguir convenciones de Spring Boot
- **Tests**: Cobertura mínima 80%
- **Commits**: Conventional Commits
- **Documentación**: Actualizar README y Swagger

## 📞 Soporte

### Recursos de Ayuda

- **Documentación Spring Boot**: https://spring.io/projects/spring-boot
- **Documentación Render**: https://render.com/docs
- **Issues del Proyecto**: [GitHub Issues](https://github.com/tu-repo/issues)

### Información de Contacto

- **Equipo de Desarrollo**: Fabrica Escuela 2025-1
- **Email**: soporte@proyecto-citas.com
- **Slack**: #citas-salud-dev

---

## 📄 Licencia

Este proyecto está bajo la licencia MIT. Ver archivo `LICENSE` para más detalles.

---

**Desarrollado con ❤️ por el equipo de Fabrica Escuela 2025-1**

