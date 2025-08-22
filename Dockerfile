# syntax=docker/dockerfile:1

# --- Build stage ---
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Pre-fetch dependencies
COPY pom.xml ./
RUN mvn -B -q -DskipTests dependency:go-offline

# Build application
COPY src ./src
RUN mvn -B -DskipTests package

# --- Runtime stage ---
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Set defaults; Railway injects PORT dynamically
ENV PORT=8080
ENV JAVA_OPTS=""

# Copy fat jar
COPY --from=builder /app/target/*.jar /app/app.jar

# Informational; Railway maps ports automatically
EXPOSE 8080

# Start the app; Spring Boot reads server.port from -Dserver.port
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT} -jar /app/app.jar"]
