pipeline {
    agent any
    stages {
        stage('Build') { 
            steps {
		sh 'cd test'
                sh 'mvn clean test' 
            }
        }
    }
}


