######################## 1️⃣  Build stage ########################
FROM eclipse-temurin:17-jdk-jammy AS build

# Install Maven (needed only for build stage)
RUN apt-get update -qq && \
    apt-get install -y --no-install-recommends maven && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy POM first to leverage Docker layer caching of deps
COPY pom.xml .
# Pre‑download dependencies (optional but speeds up rebuilds)
RUN mvn -B dependency:go-offline

# Copy source & build
COPY src ./src
RUN mvn -B clean package

######################## 2️⃣  Runtime stage ######################
FROM eclipse-temurin:17-jdk-jammy

# JVM & Spring settings (override at runtime if needed)
ENV JAVA_OPTS="-Xms256m -Xmx512m"

WORKDIR /app
COPY --from=build /app/target/account-service-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
