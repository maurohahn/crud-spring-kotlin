FROM gradle:7.5-jdk17-alpine AS builder

WORKDIR /opt/app

# Only copy dependency-related files
COPY build.gradle.kts settings.gradle.kts /opt/app/

# Only download dependencies (to cache)
# Eat the expected build failure since no source code has been copied yet
# RUN gradle clean build --no-daemon > /dev/null 2>&1 || true
RUN gradle build --no-daemon > /dev/null 2>&1 || true

COPY . .

RUN gradle build --no-daemon -x test

################################################################################

FROM openjdk:17 AS runner

VOLUME /tmp

ARG JAR_FILE

COPY --from=builder /opt/app/build/libs/crud-spring-kotlin-*.jar /app.jar
COPY --from=builder /opt/app/build/resources/main/keys /keys

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Xrs","-Duser.timezone=GMT-3","-jar","/app.jar"]