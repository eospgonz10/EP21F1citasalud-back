#!/bin/bash

# Script para iniciar los servicios de monitoreo con Prometheus y Grafana

echo "🔍 Verificando configuraciones..."

# Verificar que existe la aplicación Spring Boot
if [ ! -f "target/EP21F1citasalud-back-0.0.1-SNAPSHOT.jar" ]; then
  echo "⚠️  Aplicación no compilada. Compilando proyecto Spring Boot..."
  ./mvnw clean package -DskipTests
fi

# Verificar que existen las configuraciones necesarias
if [ ! -f "monitoring/prometheus/prometheus.yml" ]; then
  echo "❌ Falta archivo prometheus.yml. Verifica la instalación."
  exit 1
fi

# Crear los directorios necesarios si no existen
mkdir -p ./monitoring/grafana/dashboards

# Asegurar que el dashboard existe
if [ ! -f "./monitoring/grafana/dashboards/spring-boot-dashboard.json" ]; then
  echo "📥 Descargando dashboard de Spring Boot para Grafana..."
  curl -s -o ./monitoring/grafana/dashboards/spring-boot-dashboard.json https://raw.githubusercontent.com/grafana/grafana/main/public/app/plugins/datasource/prometheus/dashboards/spring_boot_dashboard.json
fi

echo "🚀 Iniciando servicios de monitoreo..."

# Configurar el socket de Docker en Codespaces si es necesario
if [ -f /.dockerenv ] || grep -q docker /proc/1/cgroup; then
  if [ -e "/var/run/docker-host.sock" ] && [ ! -e "/var/run/docker.sock" ]; then
    echo "🔄 Configurando Docker en Codespaces..."
    ln -sf /var/run/docker-host.sock /var/run/docker.sock
  fi
fi

# Detectar si estamos en Codespaces o en entorno local
if command -v docker-compose &> /dev/null; then
  DOCKER_COMPOSE_CMD="docker-compose"
else
  DOCKER_COMPOSE_CMD="docker compose"
fi

# Iniciar servicios usando el comando adecuado
echo "🚀 Ejecutando: $DOCKER_COMPOSE_CMD -f docker-compose.monitoring.yml up -d"
$DOCKER_COMPOSE_CMD -f docker-compose.monitoring.yml up -d

# Verificar que los servicios se iniciaron correctamente
if [ $? -eq 0 ]; then
  echo "✅ Servicios iniciados correctamente."
  echo ""
  echo "📊 Servicios disponibles:"
  
  # Detectar si estamos en GitHub Codespaces para mostrar URLs correctas
  if [ -n "$CODESPACES" ] && [ -n "$GITHUB_CODESPACES_PORT_FORWARDING_DOMAIN" ]; then
    CODESPACE_URL="https://$CODESPACE_NAME-$FORWARDED_PORT.$GITHUB_CODESPACES_PORT_FORWARDING_DOMAIN"
    echo "   - Grafana: [puerto 3000 - abre desde la pestaña 'PORTS' en Codespaces]"
    echo "   - Prometheus: [puerto 9090 - abre desde la pestaña 'PORTS' en Codespaces]"
    echo "   - Métricas Spring Boot: ${CODESPACE_URL}/actuator/prometheus"
  else
    echo "   - Grafana: http://localhost:3000 (usuario: admin, contraseña: admin)"
    echo "   - Prometheus: http://localhost:9090"
    echo "   - Métricas Spring Boot: http://localhost:8080/actuator/prometheus"
  fi
  
  echo ""
  echo "💡 Tip: Una vez en Grafana, importa el dashboard con ID 4701 para visualizar métricas detalladas de Spring Boot"
else
  echo "❌ Error al iniciar los servicios. Verifica los logs con '$DOCKER_COMPOSE_CMD -f docker-compose.monitoring.yml logs'"
  exit 1
fi
