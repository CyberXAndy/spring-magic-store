# --- Build Stage ---
# Use an official GraalVM image that includes JDK 17 and native-image.
FROM ghcr.io/graalvm/graalvm-ce:ol9-java17-22.3.2 as builder

# Set the working directory
WORKDIR /app

# Copy the project files
COPY . .

# Build the native image.
RUN chmod +x ./mvnw && ./mvnw -Pnative native:compile -DskipTests


# --- Final Stage ---
# Use a minimal, secure base image for the final container
FROM gcr.io/distroless/cc-debian12

# Set the working directory
WORKDIR /app

# Copy only the built native executable from the 'builder' stage
COPY --from=builder /app/target/demo app

# Expose port 8080
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["./app"]