pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                sh 'sudo apt update'
                sh 'mvn -f test/pom.xml clean test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
    }
}
