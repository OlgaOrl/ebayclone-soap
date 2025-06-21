FROM openjdk:11-jre-slim

WORKDIR /app

COPY target/ebay-clone-soap-1.0-SNAPSHOT.jar /app/app.jar
COPY src/main/resources /app/resources

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]