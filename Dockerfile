FROM openjdk:17-jdk-alpine
EXPOSE 8089
COPY target/gestion-station-ski-2.3.jar gestion-station-ski-2.3.jar
ENTRYPOINT ["java", "-jar", "/gestion-station-ski-2.3.jar"]
