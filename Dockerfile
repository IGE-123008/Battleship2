FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copy project files and build
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/BattleshipGamePlayer-2.0.jar app.jar

CMD ["java", "-jar", "app.jar"]