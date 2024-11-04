pipeline {
    agent any 
    stages {
        stage('Git') {
            steps {
                git branch: 'instrcuteur-hedi', 
                url: 'https://github.com/yasminemar/devops-ski.git'
            }
        }
        stage('Mvn Clean') {
            steps {
                // Nettoyage du projet avec Maven
                sh "mvn clean"
            }
        }
        stage('Mvn Compile') {
            steps {
                // Compilation du projet avec Maven
                sh "mvn compile"
            }
        }
        stage('MVN SONARQUBE') {
            steps {
                // Analyse SonarQube
                sh "mvn sonar:sonar \
                    -Dsonar.projectKey=station-ski-project \
                    -Dsonar.host.url=http://192.168.162.222:9000/ \
                    -Dsonar.login=sqp_23209ad4478d37b4101feb3032d63d134f89c14c"
            }
        }
        stage('Mvn Deploy') {
            steps {
            // Déploiement dans Nexus en sautant les tests
                sh "mvn clean deploy -DskipTests"
            }
        }
        stage('Mvn Package') {
            steps {
                // Création du package JAR avec Maven
                sh "mvn package -DskipTests" // Assurez-vous que le JAR est construit
            }
        }
        stage('building images') {
            steps {
                // Construire l'image Docker
                sh "mvn clean package -DskipTests"
     
                sh "docker build -t hedithameur/gestion-station-ski:latest ."
            }
        }
        stage('Verify Image') {
            steps {
                sh 'docker images'
            }   
        }
        stage('Push to DockerHub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                sh '''
                docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD
                docker push hedithameur/gestion-station-ski:latest
                '''
                }
            }
        }
        stage('Docker Compose Up') {
            steps {
            // Arrêter et supprimer les conteneurs existants
            sh 'docker compose down'
        
            // Lancer le fichier docker-compose.yml
            sh 'docker compose up -d'
            }
        }
    }
}