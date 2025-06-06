# Configuración de Docker en GitHub Codespaces

Para poder utilizar Docker en GitHub Codespaces y ejecutar servicios como Prometheus y Grafana, es necesario seguir estos pasos adicionales:

## 1. Configurar tu `devcontainer.json`

GitHub Codespaces requiere que habilites las funciones de Docker explícitamente en tu configuración de devcontainer. Abre o crea el archivo `.devcontainer/devcontainer.json` y asegúrate de que tenga la siguiente configuración:

```json
{
  "name": "spring-boot-citasalud-devcontainer",
  "dockerComposeFile": "docker-compose.yml",
  "service": "app",
  "workspaceFolder": "/workspace",
  "features": {
    "ghcr.io/devcontainers/features/docker-in-docker:2": {},
    "ghcr.io/devcontainers/features/docker-outside-of-docker:1": {}
  },
  "customizations": {
    "vscode": {
      "extensions": [
        "vscjava.vscode-java-pack",
        "vscjava.vscode-spring-initializr",
        "vscjava.vscode-spring-boot-dashboard",
        "vscjava.vscode-spring-boot",
        "mtxr.sqltools",
        "mtxr.sqltools-driver-pg"
      ],
      "settings": {
        "sqltools.connections": [
          {
            "name": "Container database",
            "driver": "PostgreSQL",
            "previewLimit": 50,
            "server": "localhost",
            "port": 5432,
            "database": "bd_citassalud",
            "username": "postgres",
            "password": "root1"
          }
        ],
        "java.jdt.ls.java.home": "/docker-java-home",
        "java.configuration.updateBuildConfiguration": "automatic",
        "java.format.settings.url": "https://raw.githubusercontent.com/google/styleguide/gh-pages/eclipse-java-google-style.xml",
        "java.format.settings.profile": "GoogleStyle",
        "java.compile.nullAnalysis.mode": "automatic"
      }
    }
  },
  "forwardPorts": [8080, 5432, 9090, 3000],
  "remoteUser": "vscode"
}
```

## 2. Reconstruir el contenedor de Codespaces

Después de actualizar la configuración del devcontainer:

1. Usa la paleta de comandos (Ctrl+Shift+P o Cmd+Shift+P)
2. Escribe "Rebuild Container" y selecciona "Codespaces: Rebuild Container"

Esto reconstruirá tu Codespace con soporte para Docker.

## 3. Iniciar servicios de monitoreo

Una vez reconstruido el Codespace, podrás ejecutar:

```bash
./start-monitoring.sh
```

## 4. Acceder a Grafana y Prometheus

Después de iniciar los servicios, podrás acceder a:

- Grafana: Puerto 3000
- Prometheus: Puerto 9090

En GitHub Codespaces, puedes hacer clic en la pestaña "PORTS" en la parte inferior de la interfaz, donde aparecerán los puertos reenviados y podrás hacer clic en el enlace para abrirlos.
