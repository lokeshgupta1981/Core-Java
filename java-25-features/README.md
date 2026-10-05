Source code for the article https://howtodoinjava.com/?p=44176

# Java 25 Features (LTS) with Examples

Runnable examples for the JEPs in JDK 25: compact source files and instance main methods, module import declarations, flexible constructor bodies, scoped values, the Key Derivation Function API, compact object headers, generational Shenandoah, AOT cache, JFR method timing and CPU-time profiling, plus the preview features (stable values, structured concurrency, primitive patterns, PEM encodings) and the Vector API incubator.

## Versions

- JDK 25 (tested with Temurin 25.0.4.1)
- Maven 3.9.16
- JUnit 6.1.3, AssertJ 3.27.7
- maven-compiler-plugin 3.16.0, maven-surefire-plugin 3.6.0, exec-maven-plugin 3.6.4

Preview classes are compiled with `--enable-preview`, and the Vector API with `--add-modules jdk.incubator.vector` (see `pom.xml`).

## Run

```bash
# all tests (they also start child JVMs with the new runtime flags)
mvn test

# print the output of every library and language example
mvn compile exec:exec

# compact source files (no flags needed)
java scripts/Library.java
echo Lokesh | java scripts/Greeting.java
java scripts/AmbiguousImports.java      # fails on purpose: reference to List is ambiguous
java scripts/FixedImports.java
java --enable-preview --source 25 scripts/PreviewSnippets.java

# runtime flags (build the jar first with: mvn package -DskipTests)
java -cp target/classes com.howtodoinjava.java25.runtime.HeaderFootprint
java -XX:+UseCompactObjectHeaders -cp target/classes com.howtodoinjava.java25.runtime.HeaderFootprint
java -XX:+UseShenandoahGC -XX:ShenandoahGCMode=generational -Xlog:gc -cp target/classes com.howtodoinjava.java25.runtime.AllocationLoad
java -XX:AOTCacheOutput=app.aot -cp target/java-25-features-1.0.0.jar com.howtodoinjava.java25.runtime.StartupApp
java -XX:AOTCache=app.aot -cp target/java-25-features-1.0.0.jar com.howtodoinjava.java25.runtime.StartupApp
java '-XX:StartFlightRecording:jdk.MethodTiming#filter=com.howtodoinjava.java25.runtime.CatalogSearch::search,filename=timing.jfr' -cp target/classes com.howtodoinjava.java25.runtime.CatalogSearch
jfr view method-timing timing.jfr
java -XX:StartFlightRecording=jdk.CPUTimeSample#enabled=true,filename=cpu.jfr -cp target/classes com.howtodoinjava.java25.runtime.CatalogSearch   # Linux only
jfr view cpu-time-hot-methods cpu.jfr
```

The services in `BookLookup` simulate slow network calls with the `Waits.pause(Duration)` helper.
