# Use official Maven with Java 17
FROM maven:3.9.6-eclipse-temurin-17

# Set working directory inside the container
WORKDIR /app

# Install required dependencies
RUN apt-get update && apt-get install -y \
    wget unzip curl xvfb gnupg \
    && rm -rf /var/lib/apt/lists/*
ENV CHROMEDRIVER_VERSION=120.0.6099.71

### install chrome
RUN apt-get update && apt-get install -y wget && apt-get install -y zip
RUN wget -q https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
RUN apt-get install -y ./google-chrome-stable_current_amd64.deb

### install chromedriver
RUN wget https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/$CHROMEDRIVER_VERSION/linux64/chromedriver-linux64.zip \
  && unzip chromedriver-linux64.zip && rm -dfr chromedriver_linux64.zip \
  && mv /chromedriver-linux64/chromedriver /usr/bin/chromedriver \
  && chmod +x /usr/bin/chromedriver
ENV DISPLAY=:99

# Copy project files
COPY . .

# Expose necessary ports (if needed)
EXPOSE 8080

# Default command to run tests
CMD ["mvn", "clean", "test"]

