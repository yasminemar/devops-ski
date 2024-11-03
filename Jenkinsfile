pipeline {
    agent any 
    stages {
        stage('Git') {
            steps {
                git branch: 'subsription-slimmm', 
                url: 'https://github.com/yasminemar/devops-ski.git'
            }
        }
        stage('Mvn Clean and Compile') {
            steps {
                // Nettoyage et compilation du projet avec Maven
                sh "mvn clean compile"
            }
        }
        stage('MVN SONARQUBE') {
            steps {
                // Analyse SonarQube
                sh "mvn sonar:sonar \
                    -Dsonar.projectKey=projet_devops \
                    -Dsonar.host.url=http://192.168.33.10:9000 \
                    -Dsonar.login=sqa_82e0afe9206e1be2e6c860f9aa613e6481607280"
            }
        }
        // stage('Mvn Deploy') {
        //     steps {
        //         // Déploiement dans Nexus en sautant les tests
        //         sh "mvn clean deploy -DskipTests"
        //     }
        // }
        stage('Mvn Package') {
            steps {
                // Création du package JAR avec Maven
                sh "mvn package -DskipTests" // Assurez-vous que le JAR est construit
            }
        }
        stage('Building Images') {
            steps {
                // Construire l'image Docker
                sh "docker build -t slimzouari560/gestion-station-ski ."
              

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
                    echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin
                    docker push slimzouari560/gestion-station-ski
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
