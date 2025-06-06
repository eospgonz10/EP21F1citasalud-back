#!/bin/bash

# =============================================================================
# SCRIPT DE BUILD Y DEPLOY PARA CITAS SALUD
# =============================================================================

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Función para imprimir mensajes
print_message() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Verificar que Maven esté instalado
check_maven() {
    if ! command -v mvn &> /dev/null; then
        print_error "Maven no está instalado. Por favor, instala Maven primero."
        exit 1
    fi
}

# Verificar que Docker esté instalado y disponible
check_docker() {
    print_message "Verificando disponibilidad de Docker..."
    
    # Verificar si docker command está disponible
    if ! command -v docker &> /dev/null; then
        print_error "Docker no está instalado o no está en el PATH."
        print_message "Si estás en Codespaces, es posible que necesites:"
        print_message "1. Rebuilder el container (Ctrl+Shift+P -> 'Rebuild Container')"
        print_message "2. Verificar que features/docker-in-docker esté configurado en devcontainer.json"
        print_message ""
        print_message "Alternativamente, puedes usar:"
        print_message "  ./build-deploy.sh codespaces-setup  # Para desarrollo sin Docker"
        exit 1
    fi
    
    # Verificar si el daemon de Docker está ejecutándose
    if ! docker info &> /dev/null; then
        print_warning "Docker está instalado pero el daemon no está ejecutándose."
        print_message "Intentando iniciar el servicio Docker..."
        
        # Intentar diferentes métodos para iniciar Docker
        if command -v systemctl &> /dev/null; then
            systemctl start docker 2>/dev/null || true
        elif [ -f /etc/init.d/docker ]; then
            /etc/init.d/docker start 2>/dev/null || true
        elif command -v service &> /dev/null; then
            service docker start 2>/dev/null || true
        elif command -v dockerd &> /dev/null; then
            print_message "Iniciando dockerd manualmente..."
            dockerd &> /tmp/dockerd.log &
            sleep 10
        fi
        
        # Verificar nuevamente
        if ! docker info &> /dev/null; then
            print_error "No se pudo conectar con el daemon de Docker."
            print_message ""
            print_message "💡 SOLUCIÓN ALTERNATIVA para Codespaces:"
            print_message "   ./build-deploy.sh codespaces-setup"
            print_message ""
            print_message "   Este comando compilará y ejecutará la aplicación"
            print_message "   directamente con Java, sin necesidad de Docker."
            print_message ""
            print_message "🔧 Para usar Docker, intenta:"
            print_message "   1. Rebuild Container: Ctrl+Shift+P -> 'Dev Containers: Rebuild Container'"
            print_message "   2. O ejecuta manualmente: dockerd &"
            exit 1
        fi
    fi
    
    print_success "Docker está disponible y funcionando"
}

# Limpiar y compilar el proyecto
build_project() {
    print_message "Compilando el proyecto..."
    mvn clean package -DskipTests
    print_success "Proyecto compilado exitosamente"
}

# Ejecutar tests
run_tests() {
    print_message "Ejecutando tests..."
    mvn test
    print_success "Tests ejecutados exitosamente"
}

# Construir imagen Docker
build_docker() {
    print_message "Construyendo imagen Docker..."
    docker build -t citasalud-app:latest .
    print_success "Imagen Docker construida exitosamente"
}

# Desplegar localmente con Docker Compose
deploy_local() {
    print_message "Desplegando aplicación localmente..."
    docker-compose -f docker-compose.prod.yml up -d
    print_success "Aplicación desplegada localmente"
    print_message "Accede a los siguientes servicios:"
    echo "  - Aplicación: http://localhost:8080/api"
    echo "  - Swagger: http://localhost:8080/doc/swagger-ui.html"
    echo "  - Grafana: http://localhost:3000 (admin/admin123)"
    echo "  - Prometheus: http://localhost:9090"
}

# Detener servicios locales
stop_local() {
    print_message "Deteniendo servicios locales..."
    docker-compose -f docker-compose.prod.yml down
    print_success "Servicios detenidos"
}

# Desplegar solo la aplicación Spring Boot (sin Docker)
deploy_local_app_only() {
    print_message "Desplegando solo la aplicación Spring Boot..."
    
    # Verificar que el JAR existe
    if [ ! -f "target/EP21F1citasalud-back-0.0.1-SNAPSHOT.jar" ]; then
        print_error "El archivo JAR no existe. Ejecuta 'build' primero."
        exit 1
    fi
    
    # Verificar variables de entorno
    if [ ! -f ".env" ]; then
        print_warning "Archivo .env no encontrado. Usando .env.example como plantilla."
        cp .env.example .env
    fi
    
    # Cargar variables de entorno
    if [ -f ".env" ]; then
        export $(cat .env | grep -v '^#' | grep -v '^$' | xargs)
    fi
    
    # Configurar perfil de desarrollo por defecto
    export SPRING_PROFILES_ACTIVE=dev
    
    print_message "Iniciando aplicación Spring Boot..."
    print_message "Perfil activo: dev"
    print_message "Accede a los siguientes servicios:"
    echo "  - Aplicación: http://localhost:8080/api"
    echo "  - Swagger: http://localhost:8080/doc/swagger-ui.html"
    echo "  - Actuator Health: http://localhost:8080/api/actuator/health"
    echo ""
    print_message "Presiona Ctrl+C para detener la aplicación"
    
    java -jar target/EP21F1citasalud-back-0.0.1-SNAPSHOT.jar
}
prepare_render() {
    print_message "Preparando para despliegue en Render..."
    
    # Verificar que el JAR existe
    if [ ! -f "target/EP21F1citasalud-back-0.0.1-SNAPSHOT.jar" ]; then
        print_error "El archivo JAR no existe. Ejecuta 'build' primero."
        exit 1
    fi
    
    print_success "Proyecto listo para Render"
    print_message "Para desplegar en Render:"
    echo "  1. Conecta tu repositorio de GitHub a Render"
    echo "  2. Crea un nuevo Web Service"
    echo "  3. Configura las variables de entorno:"
    echo "     - SPRING_PROFILES_ACTIVE=prod"
    echo "     - JWT_SECRET=tu_clave_secreta"
    echo "     - JWT_EXPIRATION=86400000"
    echo "     - DATABASE_URL=postgresql://usuario:password@host:puerto/database"
    echo "  4. Usa Docker como entorno de build"
}

# Mostrar ayuda
show_help() {
    echo "Script de build y deploy para Citas Salud"
    echo ""
    echo "Uso: $0 [COMMAND]"
    echo ""
    echo "Comandos disponibles:"
    echo "  build              Compilar el proyecto"
    echo "  test               Ejecutar tests"
    echo "  docker             Construir imagen Docker"
    echo "  deploy-local       Desplegar localmente con Docker Compose"
    echo "  deploy-app-only    Desplegar solo la aplicación Spring Boot (sin Docker)"
    echo "  stop-local         Detener servicios locales"
    echo "  prepare-render     Preparar para despliegue en Render"
    echo "  full-local         Compilar, construir Docker y desplegar localmente"
    echo "  codespaces-setup   Setup completo para Codespaces (sin Docker)"
    echo "  help               Mostrar esta ayuda"
}

# Función principal
main() {
    case "${1:-help}" in
        build)
            check_maven
            build_project
            ;;
        test)
            check_maven
            run_tests
            ;;
        docker)
            check_docker
            build_docker
            ;;
        deploy-local)
            check_docker
            deploy_local
            ;;
        deploy-app-only)
            check_maven
            deploy_local_app_only
            ;;
        stop-local)
            check_docker
            stop_local
            ;;
        prepare-render)
            prepare_render
            ;;
        full-local)
            check_maven
            check_docker
            build_project
            build_docker
            deploy_local
            ;;
        codespaces-setup)
            check_maven
            build_project
            deploy_local_app_only
            ;;
        help|--help|-h)
            show_help
            ;;
        *)
            print_error "Comando desconocido: $1"
            show_help
            exit 1
            ;;
    esac
}

main "$@"
