# Etapa 1: build (com Maven)
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw .
COPY pom.xml .
COPY src ./src

RUN chmod +x mvnw
RUN ./mvnw clean install -DskipTests

# Etapa 2: runtime (imagem menor)
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
