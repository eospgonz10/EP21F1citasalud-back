# =============================================================================
# DOCKERFILE PARA PRODUCCIÓN (RENDER)
# =============================================================================
FROM openjdk:17-jdk-slim

WORKDIR /app

# Instalar herramientas necesarias
RUN apt-get update && apt-get install -y \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Copiar el JAR de la aplicación
COPY target/EP21F1citasalud-back-*.jar app.jar

# Crear usuario no-root para seguridad
RUN addgroup --system appgroup && adduser --system appuser --ingroup appgroup
RUN chown -R appuser:appgroup /app
USER appuser

# Exponer el puerto
EXPOSE 8080

# Variables de entorno por defecto
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/api/actuator/health || exit 1

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
