pipeline {
    agent any
    stages {
        stage('Setup & Run Tests') {
            steps {
                script {
                    // Run Selenium container using Docker Workflow Plugin
                    docker.image('selenium/standalone-chrome:latest').withRun('-p 4444:4444 -p 7900:7900 --shm-size="2g"') { selenium ->
                        // Wait for Selenium to be ready
                        sleep 5  

                        // Run the tests
                        sh 'mvn -f test/pom.xml clean test'
                    }
                }
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
    }
}

