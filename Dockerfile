# Imagen base con Java 17 y Gradle preinstalado
FROM gradle:7-jdk17-alpine AS build

# Directorio de trabajo
WORKDIR /app

# Copiar el archivo build.gradle y settings.gradle
COPY build.gradle settings.gradle* ./

# Copiar el archivo .env
COPY .env ./

# Copiar el código fuente
COPY src ./src

# Construir la aplicación
RUN gradle build -x test --no-daemon

# Imagen ligera para ejecutar la aplicación
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiar el archivo JAR desde la etapa de construcción
COPY --from=build /app/build/libs/*.jar app.jar

# Copiar el archivo .env desde la etapa de construcción
COPY --from=build /app/.env ./

# Variable de entorno para el perfil de Spring
ENV SPRING_PROFILES_ACTIVE=prod

# Puerto por defecto para la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
