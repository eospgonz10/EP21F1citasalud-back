# Despliegue en Render

Este documento describe cómo desplegar la aplicación EP21F1citasalud-back junto con Prometheus y Grafana en Render.

## Prerrequisitos

1. Cuenta en [Render](https://render.com/)
2. Repositorio Git con el código del proyecto

## Pasos para el despliegue

### 1. Configurar la aplicación principal

1. En el dashboard de Render, selecciona "New" > "Web Service"
2. Conecta tu repositorio Git
3. Configura el servicio:
   - Nombre: `citasalud-api`
   - Runtime: `Docker`
   - Branch: `main` (o la que uses)
   - Root Directory: `.`
   - DockerfilePath: `Dockerfile`
   - Tipo de plan: Free (puedes cambiar según necesidades)
   
4. En "Environment Variables", añade:
   - `SERVER_PORT`: `8080`
   - `CONTEXT_PATH`: `""`
   - `SPRING_PROFILES_ACTIVE`: `prod`
   - `JWT_SECRET`: Genera un valor seguro o usa secretos de Render
   - `JWT_EXPIRATION`: `86400000`
   - Configura las variables de base de datos

5. Haz clic en "Create Web Service"

### 2. Configurar Prometheus

1. En el dashboard de Render, selecciona "New" > "Web Service"
2. Conecta el mismo repositorio
3. Configura el servicio:
   - Nombre: `citasalud-prometheus`
   - Runtime: `Docker`
   - DockerfilePath: `Dockerfile.prometheus`
   - Tipo de plan: Free
   
4. En "Environment Variables", mantén las predeterminadas
5. Haz clic en "Create Web Service"

### 3. Configurar Grafana

1. En el dashboard de Render, selecciona "New" > "Web Service"
2. Conecta el mismo repositorio
3. Configura el servicio:
   - Nombre: `citasalud-grafana`
   - Runtime: `Docker`
   - DockerfilePath: `Dockerfile.grafana`
   - Tipo de plan: Free
   
4. En "Environment Variables":
   - `GF_SECURITY_ADMIN_USER`: Nombre de usuario deseado
   - `GF_SECURITY_ADMIN_PASSWORD`: Contraseña segura
   
5. Haz clic en "Create Web Service"

## Acceso a los servicios desplegados

Una vez desplegados, podrás acceder a cada servicio a través de las URLs proporcionadas por Render:

- Aplicación: `https://citasalud-api.onrender.com`
- Prometheus: `https://citasalud-prometheus.onrender.com`
- Grafana: `https://citasalud-grafana.onrender.com`

## Conectar Grafana con Prometheus

1. Accede a Grafana con tus credenciales
2. Ve a Configuration > Data Sources
3. Añade una nueva fuente de datos de tipo Prometheus
4. En URL, introduce: `https://citasalud-prometheus.onrender.com`
5. Guarda y prueba la conexión

## Importar Dashboard

1. En Grafana, ve a Dashboards > Import
2. Ingresa el ID `4701` para importar el dashboard de Spring Boot
3. Selecciona la fuente de datos de Prometheus configurada anteriormente
4. Haz clic en Import
