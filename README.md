# Spring Boot Microservices

Application project to manage courses registers with two microservices.

## Features

- Users CRUD
- Courses CRUD
- Relation between courses and users
- Password hashing
- Microservices communication
- MySQL database replication
- Postgres database replication
- Users unit & integration tests
- Courses unit tests (available in the `dev` branch)

## Technologies

- Spring boot 3
- Spring security
- MySQL 8
- Postgres 16
- Docker
- GitHub Actions
- Swagger 2

## What I learn?

- Communicate java microservices with feign client
- Use GitHub actions to dockerize spring boot apps
- Use GitHub actions to deploy docker images and run them into a vps
- Use the CI/CD pipeline to run the unit & integration tests before building the docker images
- Add workflow to use testcontainers to run the integration tests
- Do MySQL database replication
- Do Postgres database replication

## Setup

1. Clone the repository

```shell
git clone https://github.com/AngelCruzL/spring-microservices
```

2. Run `docker-compose up` to start the services (this command will start the MySQL and Postgres databases and
   download the latest images of the microservices, then it will start them with the variables defined in the `.env`
   file
   at each microservice). Alternatively, you can run the services in development mode using the microservices source
   code:

```shell
docker-compose -f docker-compose-dev.yml up
```

3. Access the services at:

- [http://localhost:8001/swagger-ui.html](http://localhost:8001/swagger-ui.html) for the users service.
- [http://localhost:8002/swagger-ui.html](http://localhost:8002/swagger-ui.html) for the courses service.

## Usage

For facilitate the usage of the services, you can use the swagger UI to interact with the services. Additionally,
you can use the files included in the `http` directory to test the services using the _http client_ from your IDE.

## Deployment

The services are deployed in a VPS using GitHub actions. The services are running in a docker container and are
accessible at the following URLs:

- [Users microservice](https://msvc-users.angelcruzl.dev/swagger-ui/index.html)
- [Courses microservice](https://msvc-courses.angelcruzl.dev/swagger-ui/index.html)

Additionally, the images for the microservices are available at:

- [Users microservice image](https://hub.docker.com/r/angelcruzl/msvc-users)
- [Courses microservice image](https://hub.docker.com/r/angelcruzl/msvc-courses)

## What's next?

- Add a gateway service to manage the microservices communication
- Add a service discovery service to manage the services registration
