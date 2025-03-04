pipeline {
    agent any
    stages {
        stage('Install deps') {
            steps {
                sh '''
                    # Install required packages
                    sudo apt update
                    sudo apt install -y wget unzip curl

                    # Install Google Chrome if not installed
                    if ! command -v google-chrome &> /dev/null
                    then
                        wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | sudo apt-key add -
                        echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" | sudo tee /etc/apt/sources.list.d/google-chrome.list
                        sudo apt update
                        sudo apt install -y google-chrome-stable
                    fi

                    # Install ChromeDriver
                    CHROME_DRIVER_VERSION=$(curl -sS chromedriver.storage.googleapis.com/LATEST_RELEASE)
                    wget -N "https://chromedriver.storage.googleapis.com/$CHROME_DRIVER_VERSION/chromedriver_linux64.zip"
                    unzip -o chromedriver_linux64.zip
                    chmod +x chromedriver
                    sudo mv chromedriver /usr/local/bin/
                '''
            }
        }
        stage('Test') {
            steps {
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

