# Monitoreo para EP21F1citasalud-back

Este directorio contiene la configuración necesaria para monitorear la aplicación Spring Boot usando Prometheus y Grafana.

## Estructura

```
monitoring/
├── grafana/
│   ├── dashboards/                # Dashboards preconfigurados
│   │   └── spring-boot-dashboard.json
│   └── provisioning/              # Configuración automática de Grafana
│       ├── dashboards/            # Provisionamiento de dashboards
│       │   └── springboot.yml
│       └── datasources/           # Provisionamiento de fuentes de datos
│           └── prometheus.yml
└── prometheus/
    └── prometheus.yml            # Configuración de Prometheus
```

## Prometheus

Prometheus es un sistema de monitoreo y base de datos de series temporales que recolecta métricas de sistemas monitorizados mediante un modelo de extracción (pull model).

### Configuración

El archivo `prometheus.yml` define:

- `scrape_interval`: Cada cuánto tiempo Prometheus recolecta métricas (15s por defecto)
- `evaluation_interval`: Cada cuánto tiempo Prometheus evalúa reglas de alerta
- `scrape_configs`: Configuración para recolectar métricas de la aplicación Spring Boot

## Grafana

Grafana es una plataforma de visualización y análisis que permite consultar, visualizar y alertar sobre datos de varias fuentes.

### Dashboards

El dashboard preconfigurado de Spring Boot (ID 4701) incluye:

- Métricas JVM (heap, non-heap memory)
- Estadísticas de recolección de basura
- Estadísticas HTTP (tasa de solicitudes, duración)
- Estado general de la aplicación

### Datasources

La configuración del datasource de Prometheus se carga automáticamente al iniciar Grafana.

## Uso

Para iniciar los servicios de monitoreo:

```bash
./start-monitoring.sh
```

Acceso:
- Grafana: http://localhost:3000 (usuario: admin, contraseña: admin)
- Prometheus: http://localhost:9090
