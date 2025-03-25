FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
# Skip tests during build
RUN mvn clean package -DskipTests -Dmaven.test.skip=true

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/eureka-server-*.jar eureka-server.jar

# Configure Eureka server with environment variables
ENV SERVER_PORT=8765
ENV EUREKA_CLIENT_REGISTER_WITH_EUREKA=false
ENV EUREKA_CLIENT_FETCH_REGISTRY=false
ENV EUREKA_INSTANCE_HOSTNAME=eureka-server

EXPOSE 8765

ENTRYPOINT ["java", "-jar", "eureka-server.jar"]