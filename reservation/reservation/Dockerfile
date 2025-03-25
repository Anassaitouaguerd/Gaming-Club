FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/reservation-service-*.jar reservation-service.jar

RUN apt-get update && apt-get install -y postgresql-client

ENV SERVER_PORT=8092
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:2025/reservation_db
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=2025
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update
EXPOSE 8092

ENTRYPOINT ["java", "-jar", "reservation-service.jar"]