ARG MSVC_NAME=msvc-courses

FROM openjdk:17-jdk-slim AS builder
ARG MSVC_NAME
LABEL authors="Ángel Cruz"

WORKDIR /app/${MSVC_NAME}

COPY ./pom.xml /app
COPY ./${MSVC_NAME}/.mvn ./.mvn
COPY ./${MSVC_NAME}/mvnw .
COPY ./${MSVC_NAME}/pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip=true -Dmaven.main.skip=true -Dspring-boot.repackage.skip=true && rm -rf ./target/

COPY ./${MSVC_NAME}/src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-slim
ARG MSVC_NAME

WORKDIR /app
RUN mkdir /app/logs

ARG TARGET_FOLDER=/app/${MSVC_NAME}/target
COPY --from=builder ${TARGET_FOLDER}/msvc-courses-0.0.1-SNAPSHOT.jar .

ARG APP_PORT=8002
ENV PORT=${APP_PORT}
EXPOSE ${PORT}

CMD ["java", "-jar", "msvc-courses-0.0.1-SNAPSHOT.jar"]
