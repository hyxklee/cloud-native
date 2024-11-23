# 1단계: Build Stage
FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY . .
RUN ./gradlew build --no-daemon -x test

# 2단계: Runtime Stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar docker-springboot.jar
ENTRYPOINT ["java", "-jar", "docker-springboot.jar"]
