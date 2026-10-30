Source code for the article https://howtodoinjava.com/?p=46850

# UnsupportedClassVersionError in Java: Causes and Fix

Runnable examples that reproduce `java.lang.UnsupportedClassVersionError` and test each fix: running on a newer JDK, compiling with `--release`, finding a dependency built for a newer Java, and classes compiled with preview features. The tests compile small fixture classes with the running JDK and start them on older JDKs in child processes.

## Versions

- JDK 25 to build and run the tests (tested with OpenJDK 25.0.4.1)
- JDK 8, 11, 17 and 21 for the older-runtime tests (tested with OpenJDK 8u504, 11.0.32, 17.0.20 and 21.0.12)
- Maven 3.9.11, Gradle 8.14.3
- JUnit 6.1.3, AssertJ 3.27.7
- maven-compiler-plugin 3.16.0, maven-surefire-plugin 3.6.0
- maven-enforcer-plugin 3.6.3 with extra-enforcer-rules 1.12.1 (examples/enforcer)

## Run

```bash
# all tests; a test is skipped when the JDK it needs is missing
mvn test

# point the tests at your JDKs (needed on Windows and macOS)
JDK8_HOME=/path/to/jdk8 JDK11_HOME=/path/to/jdk11 JDK17_HOME=/path/to/jdk17 JDK21_HOME=/path/to/jdk21 mvn test

# print the class file version of any .class file
mvn compile
java -cp target/classes com.howtodoinjava.classversion.ClassFileVersion path/to/Some.class
```

## Files

| File | What it shows |
|---|---|
| `ClassFileVersion` | Reads the magic number, minor and major version from a .class file |
| `OlderRuntimeTest` | A class built with JDK 25 fails on JDK 21, 17, 11 and 8; `--release 21` fixes it |
| `ClassFileVersionTest` | Major version = Java version + 44, `javap -v` output, preview class files (minor 65535) |
| `ReleaseVsTargetTest` | `-source 8 -target 8` compiles but fails on Java 8 with `NoSuchMethodError`; `--release 8` stops at compile time |
| `DependencyTest` | A library built for Java 25 fails at first use inside a Java 21 app, and javac 21 reports "bad class file" |
| `PreviewFeaturesTest` | A preview class needs `--enable-preview` on the same JDK |
| `examples/enforcer` | `enforceBytecodeVersion` stops the build when a dependency targets a newer Java |
| `examples/gradle` | A Gradle toolchain on JDK 25 that compiles for Java 21 |

## Build examples

```bash
# Enforcer: install a library built for Java 25 as com.acme:acme-pricing:1.0.0 first, then
cd examples/enforcer && mvn validate
# Restricted to JDK 21 yet com.acme:acme-pricing:jar:1.0.0:compile contains com/acme/pricing/TaxCalculator.class targeted to JDK 25

# Gradle: compiles with the JDK 25 toolchain, writes Java 21 class files (major version 65)
cd examples/gradle && gradle compileJava
```
