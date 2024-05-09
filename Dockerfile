FROM openjdk:17.0.2-jdk-slim

LABEL authors="angelcruz"

WORKDIR /app/msvc-users
COPY ./msvc-users .

RUN ./mvnw clean package -DskipTests

EXPOSE 8001

ENTRYPOINT ["java", "-jar", "target/msvc-users-0.0.1-SNAPSHOT.jar"]
