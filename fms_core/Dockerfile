# Use an official Java runtime as a parent image
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory in the container
WORKDIR /app
LABEL maintainer="kavinda_d@epiclanka.net"
# Copy the application JAR file to the container
COPY target/efms_core-1.0.0.jar app.jar

# Expose the port the app will run on
EXPOSE 8084

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
