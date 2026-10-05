Source code for the article https://howtodoinjava.com/?p=44186

# Java 27 Features with Examples

Runnable examples for the JEPs in JDK 27: G1 as the default collector in all environments, compact object headers by default, post-quantum hybrid key exchange for TLS 1.3, JFR in-process data redaction, plus the preview features (lazy constants with *Set.ofLazy()*, primitive types in patterns, structured concurrency, PEM encodings).

## Versions

- JDK 27 (tested with Temurin 27+35)
- Maven 3.9.16
- JUnit 6.1.3, AssertJ 3.27.7
- maven-compiler-plugin 3.16.0, maven-surefire-plugin 3.6.0, exec-maven-plugin 3.6.4

Preview classes are compiled and run with `--enable-preview` (see `pom.xml`).

## Run

```bash
# all tests (some start a child JVM with extra options)
mvn test

# print the output of every example
mvn compile exec:exec

# G1 is selected even with a single CPU (JDK 26 picks Serial here)
java -XX:ActiveProcessorCount=1 -Xlog:gc -version

# compact object headers are on by default; turn them off with -XX:-UseCompactObjectHeaders
java -XX:+PrintFlagsFinal -version | grep UseCompactObjectHeaders

# JFR redaction: secrets on the command line and in the environment show as [REDACTED]
export DB_TOKEN=abc123
java -XX:StartFlightRecording:filename=shop.jfr -Ddb.password=secret123 -Dapp.name=shop \
  -cp target/classes com.howtodoinjava.java27.ShopApp --password secret456 --verbose
jfr print --events JVMInformation,InitialSystemProperty,InitialEnvironmentVariable shop.jfr
```

## Files

| Class | JEP |
|---|---|
| `RuntimeDefaults` | 523 (G1 default), 534 (compact object headers), 527 (hybrid TLS key exchange) |
| `LazyGreetings` | 531 Lazy Constants (Third Preview) |
| `StatusText` | 532 Primitive Types in Patterns, instanceof, and switch (Fifth Preview) |
| `ProfileLoader` | 533 Structured Concurrency (Seventh Preview) |
| `PemKeys` | 538 PEM Encodings of Cryptographic Objects (Third Preview) |
| `ShopApp` | 536 JFR In-Process Data Redaction (used by `JfrRedactionTest`) |
