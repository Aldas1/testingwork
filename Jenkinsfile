pipeline {
    agent any
    stages {
        stage('Build') { 
            steps {
		withMaven {
		    sh 'cd test'
		    sh 'mvn clean test' 
		}
            }
        }
    }
}


