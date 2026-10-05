Source code for the article https://howtodoinjava.com/?p=44232

# Gradle 9: What's New and How to Upgrade from Gradle 8

A small Java 25 project built with the Gradle 9 wrapper and a Kotlin DSL build script, with JUnit 6 tests.
The build also shows the most common Gradle 9 upgrade fix: *Project.exec()* is removed, so the
*printGitHash* task runs Git through *providers.exec()*.

## Versions

- Gradle 9.8.0 (through the wrapper in `gradle/wrapper`)
- JDK 25 (tested with Temurin 25.0.4.1); Gradle 9 itself needs JDK 17 or newer to run
- JUnit 6.1.3 (JUnit BOM, Jupiter and the JUnit Platform launcher)
- Configuration cache enabled in `gradle.properties`

## Run

```bash
# build and run the tests
./gradlew build

# run the app
./gradlew run

# print the short Git commit hash (prints "unknown" outside a Git repository)
./gradlew printGitHash

# show the Gradle, Kotlin and Groovy versions of the wrapper
./gradlew --version
```

## Upgrade steps used for this project

```bash
# 1. On Gradle 8.14.x, list every deprecation
./gradlew build --warning-mode=all

# 2. Change the wrapper version (run it twice to update the wrapper scripts too)
./gradlew wrapper --gradle-version 9.8.0
./gradlew wrapper

# 3. Build on Gradle 9
./gradlew build
```

The Gradle 8 version of `printGitHash` called `exec { ... }` inside `doLast`. On Gradle 9.8.0 the
Kotlin DSL script fails with `Unresolved reference 'exec'`. The fix is in `build.gradle.kts`.
