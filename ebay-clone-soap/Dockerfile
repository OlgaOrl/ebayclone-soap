
FROM maven:3.9-eclipse-temurin-11 AS build
WORKDIR /src
COPY . .
RUN mvn -DskipTests clean package

FROM eclipse-temurin:11-jre
WORKDIR /app
COPY --from=build /src/target/ebay-clone-soap-1.0-SNAPSHOT.jar /app/app.jar
CMD ["java", "-jar", "app.jar"]

