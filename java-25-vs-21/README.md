Source code for the article https://howtodoinjava.com/?p=44443

# Java 25 vs Java 21: Should You Upgrade?

Runnable examples that compare Java 21 and Java 25: code that compiles only on Java 25, virtual thread pinning in `synchronized` blocks, compact object headers, the disabled Security Manager, and the `sun.misc.Unsafe` warnings. The demo programs are compiled with `--release 21`, so the same class files run on both JDKs.

## Versions

- JDK 25 (tested with OpenJDK 25.0.4.1) and JDK 21 (tested with OpenJDK 21.0.12.1)
- Maven 3.9.11
- JUnit 6.1.3, AssertJ 3.27.7
- maven-compiler-plugin 3.16.0, maven-surefire-plugin 3.6.0

## Run

```bash
# all tests; the JDK 21 side is skipped unless JAVA21_HOME is set
JAVA21_HOME=/path/to/jdk-21 mvn test

# run a demo by hand on both JDKs
javac --release 21 -d classes src/main/java/com/howtodoinjava/upgrade/demo/*.java
/path/to/jdk-21/bin/java -Djdk.virtualThreadScheduler.parallelism=4 -cp classes com.howtodoinjava.upgrade.demo.PinningDemo
/path/to/jdk-25/bin/java -Djdk.virtualThreadScheduler.parallelism=4 -cp classes com.howtodoinjava.upgrade.demo.PinningDemo
/path/to/jdk-25/bin/java -Xmx1g -XX:+UseSerialGC -XX:+UseCompactObjectHeaders -cp classes com.howtodoinjava.upgrade.demo.HeaderFootprint
```

## Files

| File | What it shows |
|---|---|
| `OrderReport` | Stream gatherers and an unnamed variable (Java 25 code) |
| `demo.PinningDemo` | 100 virtual threads sleeping inside `synchronized`: about 2.5 s on JDK 21, about 0.13 s on JDK 25 |
| `demo.HeaderFootprint` | Heap per small record: 24 bytes, or 16 bytes with compact object headers |
| `demo.UnsafeDemo` | `sun.misc.Unsafe` memory access, which prints a warning on JDK 25 |
| `demo.Started` | Used to check that `-Djava.security.manager=allow` stops JDK 25 from starting |
| `SourceLevelTest` | The `javac --release 21` errors for Java 25 code |
| `RuntimeDifferenceTest` | Runs the demos in child JVMs on JDK 21 and JDK 25 |
