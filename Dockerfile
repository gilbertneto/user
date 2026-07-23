FROM openjdk:23-jdk

WORKDIR /app

COPY build/libs/user-0.0.1-SNAPSHOT.jar /app/user.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/user.jar"]