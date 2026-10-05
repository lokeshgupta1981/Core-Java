Source code for the article https://howtodoinjava.com/?p=44105

# Unsupported class file major version 69

A small Maven project, compiled for Java 25 (class file major version 69), that shows
how to read the class file version and which tools reject Java 25 class files.

## Versions

- JDK 25 (Temurin 25.0.4.1) to build, Maven 3.9.16
- ASM 9.10.1 (and ASM 9.7.1 in an isolated class loader to show the error)
- JaCoCo 0.8.15, Mockito 5.24.0, JUnit 6.1.3, AssertJ 3.27.7
- maven-compiler-plugin 3.16.0, maven-surefire-plugin 3.6.0, maven-enforcer-plugin 3.6.3

## Contents

- *ClassFileVersion* reads the magic number, minor and major version from a .class file or every class in a JAR.
- *VersionCheck* prints the version of the given .class/.jar files and the highest version the running JVM accepts.
- *Hello* is a minimal main class used to show *UnsupportedClassVersionError* on JDK 21.
- *OldAsmTest* shows "Unsupported class file major version 69" from ASM 9.7.1.
- *PriceServiceTest* mocks a Java 25 class with Mockito (Byte Buddy).
- *OlderRuntimeTest* starts *Hello* on JDK 21 (runs only when JDK21_HOME is set).

## Run

```bash
# Build and run all tests (JDK 25 or newer)
mvn verify

# Also run the JDK 21 test
JDK21_HOME=/usr/lib/jvm/java-21-openjdk-amd64 mvn verify

# Print class file versions
java -cp target/classes com.howtodoinjava.classversion.VersionCheck \
  target/classes/com/howtodoinjava/classversion/Hello.class \
  target/unsupported-class-file-version-1.0.0.jar

# Reproduce the errors with older tool versions
mvn verify -Djacoco.version=0.8.12      # Unsupported class file major version 69

# Compile for Java 21 instead (major version 65)
mvn package -Dmaven.compiler.release=21

# Docker: JDK 25 build stage, JRE 25 runtime
docker build -t fruit-app .
docker run --rm fruit-app
```
