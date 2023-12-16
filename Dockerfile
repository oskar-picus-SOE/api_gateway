FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /buildApp

COPY ./pom.xml .
COPY ./src ./src

RUN mvn package

FROM openjdk:17.0-slim

EXPOSE 8081

USER root

RUN mkdir /app

COPY --from=build /buildApp/target/*.jar /app/api_gateway.jar

ENTRYPOINT ["java", "-jar", "app/api_gateway.jar"]