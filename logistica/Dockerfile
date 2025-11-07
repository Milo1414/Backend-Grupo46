# Etapa 1: Build
FROM maven:3.9.4-eclipse-temurin-21 AS builder

WORKDIR /app

# Copiar archivos de Maven
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

# Copiar código fuente
COPY src src

# Compilar el proyecto
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copiar el JAR generado desde la etapa anterior
COPY --from=builder /app/target/*.jar app.jar

# Exponer puerto
EXPOSE 8081

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
