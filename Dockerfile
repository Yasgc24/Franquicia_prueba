# Usa Temurin JDK 17 oficial (multiplataforma)
FROM eclipse-temurin:17

# Directorio de la app
WORKDIR /app

# Copiar JAR compilado
COPY build/libs/*.jar app.jar

# Exponer puerto (ajústalo si no es 8080)
EXPOSE 8080

# Comando para ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
