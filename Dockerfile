FROM maven:3.6.3-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM adoptopenjdk:11-jre-hotspot
COPY --from=build /home/app/target/weather-app-0.0.1-SNAPSHOT.jar /usr/local/lib/application.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/application.jar"]