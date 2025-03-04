pipeline {
    agent any
    stages {
        stage('Test') { 
            steps {
		sh 'cd test'
                sh 'mvn clean test' 
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml' 
                }
            }
        }
    }
}
