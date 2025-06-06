#!/bin/bash

# Script para desplegar en Render

echo "🚀 Preparando despliegue en Render..."

# Verificar archivos necesarios
echo "📋 Verificando archivos necesarios..."
FILES_TO_CHECK=(
  "Dockerfile"
  "Dockerfile.prometheus"
  "Dockerfile.grafana"
  "render.yaml"
  "monitoring/prometheus/prometheus.yml"
  "monitoring/grafana/provisioning/datasources/prometheus.yml"
  "monitoring/grafana/provisioning/dashboards/springboot.yml"
)

for file in "${FILES_TO_CHECK[@]}"; do
  if [ ! -f "$file" ]; then
    echo "❌ Error: No se encontró el archivo $file"
    exit 1
  fi
done

echo "✅ Todos los archivos necesarios están presentes."

echo ""
echo "📊 Resumen para despliegue en Render:"
echo "1. Crea una cuenta en Render si aún no la tienes: https://render.com"
echo "2. Conecta tu repositorio Git con el código actual"
echo "3. Crea un nuevo servicio Blueprint y selecciona el repositorio"
echo "4. Render usará automáticamente el archivo render.yaml para configurar los servicios"
echo ""
echo "💡 Tip: También puedes usar el botón 'Deploy to Render' en tu repositorio"
echo "📘 Para más información, consulta: /workspace/RENDER_DEPLOYMENT.md"

# Generar la URL del botón Deploy to Render
REPO_URL=$(git config --get remote.origin.url 2>/dev/null)
if [ -n "$REPO_URL" ]; then
  # Convertir SSH URL a HTTPS URL si es necesario
  if [[ $REPO_URL == git@* ]]; then
    # Convertir de formato git@github.com:usuario/repo.git a https://github.com/usuario/repo
    REPO_URL=$(echo $REPO_URL | sed 's|git@\([^:]*\):\([^.]*\).git|https://\1/\2|')
  fi
  
  # Eliminar .git al final si existe
  REPO_URL=${REPO_URL%.git}
  
  RENDER_DEPLOY_URL="https://render.com/deploy?repo=${REPO_URL}"
  echo ""
  echo "🔗 URL para Deploy to Render:"
  echo "$RENDER_DEPLOY_URL"
fi
