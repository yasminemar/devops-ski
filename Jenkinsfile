pipeline {
    agent any 
    stages {
        stage('Git') {
            steps {
                git branch: 'subsription-slim', 
                url: 'https://github.com/yasminemar/devops-ski.git'
            }
        }
        stage('Mvn Clean and Compile') {
            steps {
                
                sh "mvn clean compile"
            }
        }

                                     stage('MVN SONARQUBE') {
                                    steps {

                                        sh """
                                        mvn sonar:sonar \
                                            -Dsonar.projectKey=projet_station \
                                            -Dsonar.host.url=http://192.168.33.10:9000 \
                                            -Dsonar.login=sqa_deff4d127b4859ee7e96433d61cdd59b41d9c474 \
                                            -Dsonar.coverage.excludes=**/target/**,**/.idea/**
                                        """
                                    }
                                }






        // Uncomment this stage if you need to deploy
         stage('Mvn Deploy') {
             steps {
                // Déploiement dans Nexus en sautant les tests
                sh "mvn clean deploy -DskipTests"
            }
         }
        stage('Mvn Package') {
            steps {

                sh "mvn package -DskipTests" // Assurez-vous que le JAR est construit
            }
        }
        stage('Building Images') {
            steps {

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

                sh 'docker compose down'
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