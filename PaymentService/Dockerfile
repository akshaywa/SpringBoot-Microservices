# =====================================================
# STAGE 1: Build the application using Maven and JDK 17
# =====================================================
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /build

# Copy all project files (pom.xml and source code)
# Note: For better layer caching, consider copying pom.xml first and downloading dependencies separately
COPY . .

# Build the JAR file (skip tests for faster builds in development)
RUN mvn clean package -DskipTests

# =====================================================
# STAGE 2: Create a lightweight runtime image
# =====================================================
FROM eclipse-temurin:17-jre-alpine

# Set the working directory for the runtime
WORKDIR /app

# Copy the built JAR from the build stage
# Using a wildcard (*) to avoid hardcoding the version
COPY --from=build /build/target/product-service-*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8081

# Run the application (use ENTRYPOINT + CMD for better flexibility)
ENTRYPOINT ["java", "-jar", "app.jar"]
# Alternative: CMD ["java", "-jar", "app.jar"]