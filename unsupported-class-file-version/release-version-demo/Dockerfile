# Build stage: the JDK in this image runs javac, so it must be 25 or newer
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /src
COPY pom.xml .
COPY src ./src
RUN mvn -B -q package

# Runtime stage: a Java 25 JRE loads class file major version 69
FROM eclipse-temurin:25-jre
COPY --from=build /src/target/release-version-demo-1.0.0.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
