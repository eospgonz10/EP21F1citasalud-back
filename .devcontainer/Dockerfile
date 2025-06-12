FROM openjdk:17-jdk-slim

RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install --no-install-recommends postgresql-client git maven \
    && apt-get clean -y && rm -rf /var/lib/apt/lists/*

# Crear el usuario vscode
RUN useradd -ms /bin/bash vscode

# Cambiar el usuario
USER vscode

# Establecer el directorio de trabajo
WORKDIR /workspace

# copiar el código fuente al contenedor
COPY --chown=vscode:vscode . .

# Compilar el proyecto
RUN chmod +x mvnw
RUN ./mvnw clean package

# Exponer el puerto que usa Spring Boot
EXPOSE 8080

# Ejecutar la aplicación
CMD ["java", "-jar", "target/EP21F1citasalud-back-0.0.1-SNAPSHOT.jar"]
