FROM openjdk:17-jdk-slim AS builder

LABEL authors="angelcruz"

WORKDIR /app/msvc-courses

COPY ./pom.xml /app
COPY ./msvc-courses/.mvn ./.mvn
COPY ./msvc-courses/mvnw .
COPY ./msvc-courses/pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip=true -Dmaven.main.skip=true -Dspring-boot.repackage.skip=true && rm -rf ./target/

COPY ./msvc-courses/src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app
RUN mkdir /app/logs

COPY --from=builder /app/msvc-courses/target/msvc-courses-0.0.1-SNAPSHOT.jar .

EXPOSE 8002

CMD ["java", "-jar", "msvc-courses-0.0.1-SNAPSHOT.jar"]
