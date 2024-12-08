# 第一階段：構建應用
FROM maven:3.8.7-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 第二階段：運行應用
FROM openjdk:17-jdk-slim
WORKDIR /app
# 顯式指定 JAR 文件
COPY --from=build /app/target/mrsaccountant-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]
