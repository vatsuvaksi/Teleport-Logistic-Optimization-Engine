# =========================
# Build stage
# =========================
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /build

# Copy only pom first to leverage Docker layer caching
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN mvn -B -q clean install -DskipTests


# =========================
# Runtime stage (minimal)
# =========================
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy only the fat jar from build stage
COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

# JVM options optimized for containers
ENTRYPOINT ["java", \
  "-XX:MaxRAMPercentage=75.0", \
  "-XX:+ExitOnOutOfMemoryError", \
  "-jar", \
  "app.jar"]
