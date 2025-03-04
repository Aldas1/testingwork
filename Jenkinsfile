pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                sh 'apt update'
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
