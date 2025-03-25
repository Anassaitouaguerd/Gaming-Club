FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
# Download dependencies separately (for better caching)
RUN mvn dependency:go-offline -B
COPY src ./src
# Package skipping tests to avoid database connection errors
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/user-service-*.jar user-service.jar
# Add PostgreSQL JDBC driver
RUN apt-get update && apt-get install -y postgresql-client
# Environment variables for database connection (to be overridden)
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:2025/user_service_db
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=2025
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "user-service.jar"]