# Utiliser une image légère OpenJDK 17 basée sur Alpine Linux
FROM openjdk:17-jdk-alpine

# Exposer le port sur lequel l'application Spring Boot sera disponible
EXPOSE 8089

# Copier votre fichier JAR généré dans le conteneur
COPY target/gestion-station-ski-1.0.jar gestion-station-ski-1.0.jar

# Définir le point d'entrée pour exécuter l'application avec Java
ENTRYPOINT ["java", "-jar", "/gestion-station-ski-1.0.jar"]