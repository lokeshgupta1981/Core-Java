Source code for the article https://howtodoinjava.com/?p=44191

# IllegalArgumentException in Java

A small Maven project with JUnit tests that trigger each common cause of `java.lang.IllegalArgumentException`
in the JDK, assert the exact message, and show the safe version of each call. It also compares
`IllegalArgumentException` with `IllegalStateException`, `NullPointerException` and `IndexOutOfBoundsException`
in our own guard clauses, Spring's `Assert` and Guava's `Preconditions`.

## Versions

- JDK 25 (tested with Temurin 25.0.4.1), Maven 3.9.16
- Spring Framework 7.0.9 (spring-core, for `org.springframework.util.Assert`)
- Guava 33.7.2-jre (for `com.google.common.base.Preconditions`)
- JUnit 6.1.3, AssertJ 3.27.7
- maven-compiler-plugin 3.16.0, maven-surefire-plugin 3.6.0

## Contents

- `Size` is an enum with a safe `parse(String)` lookup that returns `Optional<Size>`.
- `SizeDemo` prints the stack trace of `Size.valueOf("XL")`, or the safe lookup when given an argument.
- `SafeInputs` has safe versions of `Integer.parseInt()`, `UUID.fromString()` and Base64 decoding.
- `FruitBasket` validates its arguments with guard clauses (IAE, ISE, NPE, IOOBE).
- `JdkCausesTest` asserts each JDK call that throws `IllegalArgumentException` and its message.
- `HierarchyTest` shows the subclasses (`NumberFormatException`, `PatternSyntaxException`, ...) and the look-alikes that are not subclasses (`DateTimeException`).
- `FixesTest`, `FruitBasketTest` and `ValidationLibrariesTest` cover the fixes and the validation helpers.

## Run

```bash
# Build and run all tests
mvn verify

# Stack trace of an unknown enum name
mvn -q compile
java -cp target/classes com.howtodoinjava.iae.SizeDemo

# Safe lookup
java -cp target/classes com.howtodoinjava.iae.SizeDemo " small "    # Size: SMALL
java -cp target/classes com.howtodoinjava.iae.SizeDemo XL           # Size: MEDIUM
```
