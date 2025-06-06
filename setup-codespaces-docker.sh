#!/bin/bash

# Script para configurar Docker en GitHub Codespaces

echo "📋 Esta guía te ayudará a configurar Docker en GitHub Codespaces"
echo ""
echo "Para usar Docker en GitHub Codespaces, debes:"
echo ""
echo "1. Modificar tu archivo .devcontainer/devcontainer.json para incluir"
echo "   características de Docker. Hemos creado un ejemplo de configuración en:"
echo "   .devcontainer-docker-example/devcontainer.json"
echo ""
echo "2. Reconstruir tu contenedor utilizando la paleta de comandos (Ctrl+Shift+P)"
echo "   y seleccionando 'Codespaces: Rebuild Container'"
echo ""
echo "3. Una vez reconstruido, podrás usar Docker y ejecutar ./start-monitoring.sh"
echo ""
echo "📄 Para obtener instrucciones detalladas, consulta:"
echo "   /workspace/CODESPACES_SETUP.md"

# Verificar si estamos en Codespaces
if [ -n "$CODESPACES" ]; then
    echo ""
    echo "✅ Confirmado: Estás ejecutando este script en GitHub Codespaces"
    
    # Verificar si Docker está disponible
    if docker ps &>/dev/null; then
        echo "✅ Docker está configurado correctamente"
        docker --version
        
        # Verificar Docker Compose
        if command -v docker-compose &>/dev/null; then
            echo "✅ Docker Compose (versión clásica) está disponible"
            docker-compose --version
        elif docker compose version &>/dev/null; then
            echo "✅ Docker Compose (versión nueva) está disponible"
            docker compose version
        fi
        
        echo ""
        echo "🚀 Todo está listo. Puedes ejecutar:"
        echo "./start-monitoring.sh"
    else
        echo "❌ Docker no está disponible. Necesitas reconstruir tu contenedor"
        echo "   siguiendo las instrucciones en CODESPACES_SETUP.md"
    fi
else
    echo ""
    echo "❓ No parece que estés ejecutando este script en GitHub Codespaces."
    echo "   Este script está diseñado específicamente para entornos de Codespaces."
fi
