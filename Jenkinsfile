pipeline {
    agent any 
	environment {
        RECIPIENTS = 'hedi.thameur@esprit.tn' 
    }
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
	 post {
        failure {
            script {
                // Récupérer la sortie de la console
                def consoleOutput = sh(script: "curl -s -u 'admin:119c985aeb2bcc3cb8409b0828b6d9c594' http://192.168.33.10:8080/job/${env.JOB_NAME}/${env.BUILD_NUMBER}/consoleText", returnStdout: true).trim()
                mail to: 'slim.zouari@esprit.tn, oumayma.sahmim@esprit.tn',
                     subject: "Échec du Build: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                     body: """
                     Bonjour,
                     Le build du votre  projet '${env.JOB_NAME}' s'est terminé avec le statut : FAILURE.

                     Détails :
                     - Numéro du Build : ${env.BUILD_NUMBER}
                     - Statut du Build : FAILURE
                     - Durée du Build : ${currentBuild.durationString}

                     - Sortie de la console :
                     ${consoleOutput}
                     """
            }
        }
    }
	
}