#!/bin/bash

# Script para configurar el entorno para monitoreo en Codespaces

echo "🔍 Verificando el entorno de Codespaces..."

# Comprobamos si estamos en un contenedor
if [ -f /.dockerenv ] || grep -q docker /proc/1/cgroup; then
    echo "✅ Detectado entorno Docker/Codespaces"
else
    echo "❌ Este script está diseñado para ejecutarse en GitHub Codespaces"
    exit 1
fi

# En Codespaces, accedemos a Docker mediante el socket de Docker
echo "🐳 Configurando Docker en Codespaces..."

# Verificar si podemos acceder al socket de Docker
if [ -e "/var/run/docker-host.sock" ]; then
    echo "🔄 Configurando socket de Docker..."
    
    # Crear directorio si no existe
    mkdir -p /var/run/
    
    # Enlazar el socket del host
    ln -sf /var/run/docker-host.sock /var/run/docker.sock
    
    echo "✅ Socket de Docker configurado correctamente"
else
    echo "❌ No se encontró el socket de Docker del host"
    echo "Este Codespace podría no tener habilitadas las funciones de Docker"
    exit 1
fi

# Verificar que podemos ejecutar comandos de Docker
echo "🧪 Probando Docker..."
if docker ps &>/dev/null; then
    echo "✅ Docker está funcionando correctamente"
    docker --version
else 
    echo "❌ No se puede acceder a Docker. Verifica que tu Codespace tenga habilitadas las funciones de Docker"
    exit 1
fi

# Verificar Docker Compose
echo "🧪 Probando Docker Compose..."
if command -v docker-compose &>/dev/null; then
    echo "✅ Docker Compose (versión clásica) está disponible"
    docker-compose --version
elif docker compose version &>/dev/null; then
    echo "✅ Docker Compose (nueva versión) está disponible"
    docker compose version
else
    echo "❌ No se encontró Docker Compose"
fi

echo ""
echo "🚀 Instalación completada. Necesitas reiniciar la terminal para que los cambios surtan efecto."
echo "Después de reiniciar, ejecuta: ./start-monitoring.sh"
echo ""
echo "Para reiniciar ahora, ejecuta: exit"
