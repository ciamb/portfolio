FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml ./
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests -Dquarkus.profile=prod

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=build /app/target/quarkus-app /app/

RUN mkdir -p /app/data
VOLUME ["/app/data"]

EXPOSE 8080

ENV QUARKUS_PROFILE=prod

ENTRYPOINT ["java", "-jar", "quarkus-run.jar"]
