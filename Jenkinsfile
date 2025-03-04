pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                sh 'mkdir /tmp/chromedriver'
                sh 'cp chromedriver /tmp/chromedriver'
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
