FROM gradle:8-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

FROM openjdk:21-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]