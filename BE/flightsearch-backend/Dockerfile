# Use the official Gradle image with JDK 17 to build the application
FROM gradle:7.6.0-jdk17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the entire project into the container
COPY . .

# Build the application
RUN gradle build --no-daemon

# Use a slim OpenJDK image to run the application
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the .env file to the app directory
COPY .env /app/.env

# Copy the built JAR file from the build stage
COPY --from=build /app/build/libs/flightsearch-backend-0.0.1-SNAPSHOT.jar /app/app.jar

# Run the application
CMD ["java", "-jar", "/app/app.jar"]
