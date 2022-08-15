# syntax=docker/dockerfile:1

FROM openjdk:17

WORKDIR /app

EXPOSE 8080

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve

COPY src ./src

CMD ["./mvnw", "spring-boot:run"]