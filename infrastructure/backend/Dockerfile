# Use Ubuntu as the base image
FROM ubuntu:latest

# Update package lists and install dependencies (Java 17, Gradle, Git, Unzip)
RUN apt-get update && \
    apt-get install -y openjdk-21-jdk unzip curl git openssl

# Install Gradle
RUN curl -L https://services.gradle.org/distributions/gradle-8.6-bin.zip -o gradle.zip \
    && unzip gradle.zip -d /opt \
    && rm gradle.zip

# Set Gradle environment variables
ENV GRADLE_HOME /opt/gradle-8.6
ENV PATH $PATH:/opt/gradle-8.6/bin

# Set the working directory
WORKDIR /app

# Copy the Spring Boot application source code into the container
COPY . /app

# Install build dependencies
RUN apt-get update
RUN apt-get install -y build-essential

# Build the application
RUN ./gradlew build

# Expose the port that the Spring Boot application will run on
EXPOSE 8080
CMD ["java", "-jar", "build/libs/ClopenVota-0.0.1.jar"]