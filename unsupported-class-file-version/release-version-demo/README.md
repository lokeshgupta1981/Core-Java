Source code for the article https://howtodoinjava.com/maven/release-version-not-supported/

# Fix "release version 25 not supported"

A one-class Maven project with `maven.compiler.release=25`. It builds on JDK 25 or newer and fails on JDK 21 with
`Fatal error compiling: error: release version 25 not supported`. The same folder shows the fixes:
JAVA_HOME, Maven toolchains, Gradle toolchains, GitHub Actions and Docker.

## Versions

- JDK 25 (Temurin 25.0.4.1) to build; JDK 21 (21.0.12.1) to reproduce the error
- Maven 3.9.16, maven-compiler-plugin 3.16.0, maven-jar-plugin 3.5.1, maven-toolchains-plugin 3.3.0
- Gradle 9.8.0 with the foojay-resolver-convention plugin 1.0.0
- actions/checkout v7, actions/setup-java v6
- Docker images maven:3.9-eclipse-temurin-25 and eclipse-temurin:25-jre

## Run

```bash
# 1. Build and run on JDK 25
mvn -v                                   # Java version: 25...
mvn package
java -jar target/release-version-demo-1.0.0.jar
# Running on Java 25
# Class file major version 69

# 2. Reproduce the error with JDK 21
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
mvn -v                                   # Java version: 21...
mvn clean compile                        # Fatal error compiling: error: release version 25 not supported

# 3. Fix with toolchains: Maven runs on JDK 21, javac and tests use JDK 25
#    (copy toolchains-example.xml to ~/.m2/toolchains.xml and change jdkHome, or pass it with -t)
mvn clean package -Pjdk-toolchain -t toolchains-example.xml

# 4. Gradle: the toolchain compiles with JDK 25 even when Gradle runs on JDK 21
gradle run

# 5. Docker: the JDK in the build image runs javac
docker build -t release-demo .
docker run --rm release-demo
```

`github-actions-build.yml` is a workflow to copy into `.github/workflows/` of a repository.
