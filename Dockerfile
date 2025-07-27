# --- Build Stage ---
# Use an official Maven image that includes JDK 17.
FROM maven:3-eclipse-temurin-17 as builder

# Set the working directory
WORKDIR /app

# Copy the project files
COPY . .

# Build the application.
RUN mvn package -DskipTests


# --- Final Stage ---
# Use a slim, secure JRE image for the final container
FROM eclipse-temurin:17-jre-alpine

# Set the working directory
WORKDIR /app

# Copy only the built .jar file from the 'builder' stage
COPY --from=builder /app/target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]