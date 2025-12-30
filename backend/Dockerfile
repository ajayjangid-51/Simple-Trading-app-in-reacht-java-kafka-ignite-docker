FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
# Prevent ServiceLoader from loading Ignite JDBC driver
ENTRYPOINT ["java", "-Djdk.service.module.resolver=default", "-jar", "app.jar"]
