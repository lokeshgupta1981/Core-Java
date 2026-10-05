Source code for the article https://howtodoinjava.com/?p=44235

# Java 25 Migration: Upgrade from Java 21

A small Maven project (recipes service with Lombok, Mockito, JaCoCo and JUnit 6) that builds on Java 21
and is migrated to Java 25. `pom-java21.xml` is the project before the migration, `pom.xml` is the project after it.

## Versions

- JDK 25 (Temurin 25.0.4.1) for the migrated build, JDK 21 for the old build
- Maven 3.9.16 (or Gradle 9.8.0 with `build.gradle`)
- Lombok 1.18.48, Mockito 5.24.0, JaCoCo 0.8.15, JUnit 6.1.3
- maven-compiler-plugin 3.16.0, maven-surefire-plugin 3.6.0, maven-enforcer-plugin 3.6.3

## What changed in pom.xml

| Item | Java 21 (pom-java21.xml) | Java 25 (pom.xml) |
|---|---|---|
| maven.compiler.release | 21 | 25 |
| Lombok | 1.18.30, dependency only | 1.18.48, also in annotationProcessorPaths |
| Mockito | 5.8.0 | 5.24.0 |
| JaCoCo | 0.8.11 | 0.8.15 |
| maven-compiler-plugin | 3.11.0 | 3.16.0 |
| maven-surefire-plugin | 3.2.2 | 3.6.0 |
| maven-enforcer-plugin | - | 3.6.3 (requires JDK 25) |

## Run

```bash
# Old project on JDK 21: BUILD SUCCESS
JAVA_HOME=/path/to/jdk-21 mvn -f pom-java21.xml clean verify

# Old project on JDK 25: cannot find symbol getMinutes() (annotation processing is off by default since JDK 23)
JAVA_HOME=/path/to/jdk-25 mvn -f pom-java21.xml clean verify

# Migrated project on JDK 25: BUILD SUCCESS, 2 tests
JAVA_HOME=/path/to/jdk-25 mvn clean verify

# Same project with Gradle 9.1.0 or newer (toolchain 25)
gradle build jacocoTestReport

# Look for JDK internal API use in a JAR
jdeps --jdk-internals target/java-21-to-25-migration-1.0.0.jar
jdeps --jdk-internals ~/.m2/repository/org/projectlombok/lombok/1.18.30/lombok-1.18.30.jar
```

## Reproduce the upgrade errors

Change one version in `pom.xml` and run `mvn clean verify` on JDK 25.

| Change | Error |
|---|---|
| Lombok 1.18.30 (1.18.36 and older) | `Fatal error compiling: java.lang.ExceptionInInitializerError: com.sun.tools.javac.code.TypeTag :: UNKNOWN` |
| Mockito 5.17.0 or older | `Java 25 (69) is not supported by the current version of Byte Buddy which officially supports Java 24 (68)` |
| JaCoCo 0.8.11 | `Error while analyzing ... Recipe$RecipeBuilder.class ... Unsupported class file major version 69` |
