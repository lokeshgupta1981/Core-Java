Source code for the article https://howtodoinjava.com/?p=44193

# NoSuchMethodError in Java

A Maven multi-module build that reproduces `java.lang.NoSuchMethodError` with two versions of one library,
shows how to find the jar that caused it, and fixes it with a BOM, an exclusion and the Enforcer
`dependencyConvergence` rule.

## Versions

- JDK 25 (Temurin 25.0.4.1), Maven 3.9.16
- JUnit 6.1.3, Spring Boot 4.1.1 (boot-app module)
- maven-compiler-plugin 3.16.0, maven-surefire-plugin 3.6.0, maven-jar-plugin 3.5.1,
  maven-dependency-plugin 3.11.0, maven-enforcer-plugin 3.6.3
- Gradle 9.8.0 (gradle-check folder only), JDK 21 for the release-flag demo

## Modules

| Folder | What it contains |
|---|---|
| greeter-v1 | `com.howtodoinjava:greeter:1.0.0` with `greet(String)` and `int count()` |
| greeter-v2 | `com.howtodoinjava:greeter:2.0.0`, adds `greet(String, String)`, `count()` returns `long` |
| greeter-bom | BOM that manages greeter 2.0.0 |
| legacy-report | Depends on greeter 1.0.0 |
| welcome-service | Depends on greeter 2.0.0 and calls `greet(String, String)` |
| app | Depends on both; Maven picks greeter 1.0.0, so `WelcomeService` throws NoSuchMethodError (`VersionConflictTest`) |
| app-fixed | Same dependencies plus the BOM import and `dependencyConvergence`; both calls work (`AlignedVersionTest`) |
| boot-app | Spring Boot 4.1.1 app with the same conflict, shows the Spring Boot failure analysis report |
| two-jars | Plain `javac`/`java` reproduction with two jars, `-verbose:class` and `javap` (`run.sh`) |
| release-flag | `-source 21 -target 21` vs `--release 21` on JDK 25, run on JDK 21 (`run.sh`) |
| gradle-check | Gradle build with the same two libraries; Gradle picks the highest version |

## Run

```bash
# Build everything and run the tests
mvn install

# The conflict at run time
java -cp "app/target/app-1.0.0.jar:app/target/lib/*" com.howtodoinjava.app.Main
java -verbose:class -cp "app/target/app-1.0.0.jar:app/target/lib/*" com.howtodoinjava.app.Main | grep "greeter.Greeter "

# The fixed build
java -cp "app-fixed/target/app-fixed-1.0.0.jar:app-fixed/target/lib/*" com.howtodoinjava.app.FixedMain

# Spring Boot failure analysis
java -cp "boot-app/target/boot-app-1.0.0.jar:boot-app/target/lib/*" com.howtodoinjava.boot.WelcomeApplication

# Find the conflict (run in the app folder)
cd app
mvn dependency:tree -Dverbose -Dincludes=com.howtodoinjava:greeter
mvn validate -Penforce                          # fails: dependency convergence error
mvn dependency:tree -Pexclude -Dverbose -Dincludes=com.howtodoinjava:greeter
cd ..

# Plain javac/java reproduction
cd two-jars && ./run.sh && cd ..

# --release demo (needs a JDK 21)
cd release-flag && JDK21_HOME=/usr/lib/jvm/java-21-openjdk-amd64 ./run.sh && cd ..

# Gradle resolution (after mvn install)
cd gradle-check && gradle dependencies --configuration runtimeClasspath
gradle dependencyInsight --dependency greeter --configuration runtimeClasspath
```
