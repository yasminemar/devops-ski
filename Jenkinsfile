pipeline {
    agent any
    environment {
        SPRING_DATASOURCE_URL = 'jdbc:mysql://localhost:3306/stationSki?createDatabaseIfNotExist=true'
        SPRING_DATASOURCE_USERNAME = 'root'
        SPRING_DATASOURCE_PASSWORD = ''
        // Add any other environment specific variables here
    }
    stages {
        stage('Checkout GIT') {
            steps {
                echo 'Pulling...';
                git branch: 'registration-brahim', 
                    url: 'https://github.com/yasminemar/devops-ski.git'
            }
        }
        stage('Testing maven') {
            steps {
                sh 'mvn -version'
            }
        }
        stage('MVN CLEAN') {
            steps {
                script {
                    try {
                        sh 'mvn clean'
                    } catch (Exception e) {
                        echo "Error during Maven clean: ${e.getMessage()}"
                        throw e // Re-throw the exception to fail the stage
                    }
                }
            }
        }
        stage('MVN COMPILE') {
            steps {
                sh 'mvn compile'
            }
        }
        stage('MVN TEST') {
            steps {
                sh 'mvn test'
            }
        }
        stage('MVN SONARQUBE') {
            steps {
                sh """
                mvn sonar:sonar \\
                    -Dsonar.projectKey=station-ski-project \\
                    -Dsonar.host.url=http://192.168.162.222:9000 \\
                    -Dsonar.login=sqa_0c212e307a78eef1a35320d37c1d97d797cad155
                """
            }
        }
        stage('MVN DEPLOY') {
            steps {
                script {
                    try {
                        sh 'mvn deploy -DskipTests' // Skipping tests to speed up deployment
                    } catch (Exception e) {
                        echo "Error during Maven deploy: ${e.getMessage()}"
                        throw e // Re-throw the exception to fail the stage if deployment fails
                    }
                }
            }
        }
         stage('Build Docker Image') {
            steps {
                script {
                    sh 'docker build -t brahim170/brahim-abdelbeki-5ds6-g6:latest .'
                }
            }
        }
        stage('Deploy Image to Docker Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'brahim170', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
                        sh 'docker login -u $USERNAME -p $PASSWORD'
                        sh 'docker push bbrahim170/brahim-abdelbeki-5ds6-g6:latest'
                    }
                }
            }
        }
    }
    post {
        always {
            echo 'Pipeline execution is complete.'
        }
    }
}
