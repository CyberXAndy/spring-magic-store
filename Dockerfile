# --- Build Stage ---
# Use an official Maven image with GraalVM that includes JDK 17 and native-image.
FROM ghcr.io/graalvm/graalvm-ce:ol9-java17-22.3.2 as builder

# Install Maven
RUN microdnf install -y maven

# Set the working directory
WORKDIR /app

# Copy the project files
COPY . .

# Build the native image.
RUN mvn -Pnative native:compile -DskipTests


# --- Final Stage ---
# Use Ubuntu base image that includes necessary system libraries
FROM ubuntu:22.04

# Install required runtime libraries
RUN apt-get update && apt-get install -y \
    zlib1g \
    ca-certificates \
    && rm -rf /var/lib/apt/lists/*

# Create a non-root user
RUN useradd -r -s /bin/false appuser

# Set the working directory
WORKDIR /app

# Copy only the built native executable from the 'builder' stage
COPY --from=builder /app/target/demo app

# Change ownership to the non-root user
RUN chown appuser:appuser /app/app && chmod +x /app/app

# Switch to non-root user
USER appuser

# Expose port 8080
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["./app"]