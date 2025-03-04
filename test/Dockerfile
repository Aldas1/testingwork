# Use official Maven with Java 17
FROM maven:3.9.6-eclipse-temurin-17

# Set working directory inside the container
WORKDIR /app

# Install required dependencies
RUN apt-get update && apt-get install -y \
    wget unzip curl xvfb gnupg \
    && rm -rf /var/lib/apt/lists/*

# Install Google Chrome
RUN wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | gpg --dearmor > /usr/share/keyrings/google-chrome.gpg && \
    echo "deb [signed-by=/usr/share/keyrings/google-chrome.gpg] http://dl.google.com/linux/chrome/deb/ stable main" | tee /etc/apt/sources.list.d/google-chrome.list && \
    apt-get update && apt-get install -y google-chrome-stable

# Install ChromeDriver (Fixed)
RUN wget -q "https://storage.googleapis.com/chrome-for-testing-public/134.0.6998.35/linux64/chromedriver-linux64.zip" -O chromedriver.zip && \
    unzip chromedriver.zip -d /usr/local/bin/ && \
    rm chromedriver.zip && chmod +x /usr/local/bin/chromedriver-linux64
# Set environment variable for headless display
ENV DISPLAY=:99

# Copy project files
COPY . .

# Expose necessary ports (if needed)
EXPOSE 8080

# Default command to run tests
CMD ["mvn", "clean", "test"]

