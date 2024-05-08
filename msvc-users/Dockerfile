FROM openjdk:17.0.2-jdk-slim

LABEL authors="angelcruz"

WORKDIR /app

COPY ./target/msvc-users-0.0.1-SNAPSHOT.jar /app

EXPOSE 8001

ENTRYPOINT ["java", "-jar", "msvc-users-0.0.1-SNAPSHOT.jar"]
