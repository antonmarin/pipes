FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /src

# install gradle
COPY ./gradle ./gradle
COPY ./gradlew ./
RUN ./gradlew --no-daemon --no-watch-fs help

# build base module
COPY ./buildSrc ./buildSrc
COPY ./settings.gradle.kts ./
RUN ./gradlew --no-daemon --no-watch-fs buildSrc:build

# cache dependencies
COPY ./build.gradle.kts ./gradle.properties ./
RUN ./gradlew --no-daemon --no-watch-fs dependencies

# setup dockerd for testcontainers and build application
ARG DOCKER_HOST=""
ENV DOCKER_HOST=$DOCKER_HOST
COPY ./src ./src
RUN ./gradlew --no-daemon --no-watch-fs --warning-mode all build

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# install application
COPY --from=builder /src/build/libs/application.jar ./

# default start
ENV LOGGING_DOMAIN_ROOT_LEVEL="INFO"
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/application.jar"]
