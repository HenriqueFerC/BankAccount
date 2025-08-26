FROM ubuntu:latest AS build

RUN apt-get update && apt-get install -y openjdk-21-jdk maven

WORKDIR /app

COPY src /app/src
COPY pom.xml /app

RUN mvn clean install

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/target/BankAccount-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]