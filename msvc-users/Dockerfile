FROM openjdk:17-jdk-slim AS builder

LABEL authors="angelcruz"

WORKDIR /app/msvc-users

COPY ./pom.xml /app
COPY ./msvc-users/.mvn ./.mvn
COPY ./msvc-users/mvnw .
COPY ./msvc-users/pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip=true -Dmaven.main.skip=true -Dspring-boot.repackage.skip=true && rm -rf ./target/

COPY ./msvc-users/src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/msvc-users/target/msvc-users-0.0.1-SNAPSHOT.jar .

EXPOSE 8001

ENTRYPOINT ["java", "-jar", "msvc-users-0.0.1-SNAPSHOT.jar"]