# Build stage: JDK 25 compiles the classes (major version 69)
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /src
COPY pom.xml .
COPY src ./src
RUN mvn -B -q package -DskipTests

# Runtime stage: the JRE must be 25 or newer to load major version 69
FROM eclipse-temurin:25-jre
COPY --from=build /src/target/unsupported-class-file-version-1.0.0.jar /app/app.jar
ENTRYPOINT ["java", "-cp", "/app/app.jar", "com.howtodoinjava.classversion.Hello"]
