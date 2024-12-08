FROM maven:3.8.7-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests


FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=build /app/target/mrsaccountant-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]
