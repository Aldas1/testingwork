pipeline {
    agent any
    stages {
        stage('Build') { 
            steps {
		maven {
		    sh 'cd test'
		    sh 'mvn clean test' 
		}
            }
        }
    }
}


