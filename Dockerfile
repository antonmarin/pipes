FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /src

# install gradle
COPY ./gradle ./gradle
COPY ./gradlew ./
RUN ./gradlew --no-daemon help

# build base module
COPY ./buildSrc ./buildSrc
COPY ./settings.gradle.kts ./
RUN /src/gradlew --no-daemon buildSrc:build

# setup dockerd for testcontainers
ARG DOCKER_HOST=""
ENV DOCKER_HOST=$DOCKER_HOST
# build application
COPY ./src ./src
COPY ./build.gradle.kts ./gradle.properties ./
RUN /src/gradlew --no-daemon --warning-mode all build

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# install application
COPY --from=builder /src/build/libs/application.jar ./

# default start
ENV LOGGING_DOMAIN_ROOT_LEVEL="INFO"
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/application.jar"]
