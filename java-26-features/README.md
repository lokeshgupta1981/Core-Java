Source code for the article https://howtodoinjava.com/?p=44187

# Java 26 Features with Examples

Runnable examples for the JEPs in JDK 26: final field mutation warnings, HTTP/3 in the HTTP Client, AOT object caching with ZGC, plus the preview features (lazy constants, structured concurrency, primitive types in patterns, PEM encodings) and smaller API additions (UUID version 7, Comparator min/max, Instant.plusSaturating, Process as Closeable).

## Versions

- JDK 26 (tested with Temurin 26.0.2.1)
- Maven 3.9.16
- JUnit 6.1.3, AssertJ 3.27.7
- maven-compiler-plugin 3.16.0, maven-surefire-plugin 3.6.0, exec-maven-plugin 3.6.4

Preview classes are compiled and run with `--enable-preview` (see `pom.xml`).

## Run

```bash
# all tests (some start child JVMs with the new runtime flags)
mvn test

# print the output of every example that needs no network
mvn compile exec:exec

# JEP 500: default warning, deny mode, and enabled mutation
java -cp target/classes com.howtodoinjava.java26.finalfields.FinalFieldMutation
java --illegal-final-field-mutation=deny -cp target/classes com.howtodoinjava.java26.finalfields.FinalFieldMutation
java --enable-final-field-mutation=ALL-UNNAMED -cp target/classes com.howtodoinjava.java26.finalfields.FinalFieldMutation

# JEP 517: request that prefers HTTP/3 (falls back to HTTP/2 when UDP is blocked)
java -cp target/classes com.howtodoinjava.java26.http3.Http3Client https://openjdk.org/

# JEP 516: AOT cache with ZGC (the AOT cache needs a JAR on the class path)
mvn package -DskipTests
java -XX:+UseZGC -XX:AOTCacheOutput=app.aot -cp target/java-26-features-1.0.0.jar com.howtodoinjava.java26.runtime.StartupApp
java -XX:+UseZGC -XX:AOTCache=app.aot -Xlog:aot -cp target/java-26-features-1.0.0.jar com.howtodoinjava.java26.runtime.StartupApp
```
