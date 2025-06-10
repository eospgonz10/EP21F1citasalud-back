FROM maven:3.9.6-eclipse-temurin-17 as builder

WORKDIR /app

# Copia el proyecto completo
COPY . .

# Construye el jar con Maven
RUN mvn clean package -DskipTests

# Etapa 2: Imagen ligera para producción
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copia solo el jar generado desde la imagen builder
COPY --from=builder /app/target/*.jar app.jar

# Expón el puerto (Render usará la variable PORT)
EXPOSE 8080

# Comando para ejecutar la app
CMD ["java", "-jar", "app.jar"]