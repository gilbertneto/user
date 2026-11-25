FROM gradle:9.1.1-jdk23 as build
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

FROM openjdk:23-jdk

WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/user.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/user.jar"]