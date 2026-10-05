Source code for the article https://howtodoinjava.com/?p=44192

# StackOverflowError in Java

Reproductions and fixes for java.lang.StackOverflowError: recursion without a base case, deep but correct
recursion (and the same traversal with an explicit Deque), toString()/equals()/hashCode() cycles between two
objects, Lombok @Data on both sides of a bidirectional relation, Jackson serialization of a bidirectional
relation, a setter that calls itself, and the recursion depth reached with different thread stack sizes.

## Versions

- JDK 25 (tested with Temurin 25.0.4.1), Maven 3.9.16
- Jackson 3.2.3 (tools.jackson.core:jackson-databind), Lombok 1.18.48
- JUnit 6.1.3
- maven-compiler-plugin 3.16.0, maven-surefire-plugin 3.6.0, exec-maven-plugin 3.6.4

## Contents

- *Sums* sums 1..n without a base case, with a base case, with a tail call and with a loop.
- *tree.Folder* and *tree.FolderCounter* count nested folders with recursion and with an ArrayDeque.
- *cycle.broken* / *cycle.fixed* show a toString() cycle between Playlist and Song and its fix.
- *lombok.broken* / *lombok.fixed* show @Data on both sides and the @ToString.Exclude / @EqualsAndHashCode.Exclude fix.
- *jackson.fixed* uses @JsonManagedReference / @JsonBackReference, *jackson.ignore* uses @JsonIgnore.
- *setter.Artist* has a setter that calls itself.
- *DepthProbe* prints how many frames fit on the main thread and on a thread created with a 16 MB stack size.
- *CrashDemo* lets the error reach the top of the main thread so the JVM prints the stack trace.
- *Runner* prints the outputs used in the article.

## Run

```bash
# Build and run all tests
mvn clean test

# Print the article outputs
mvn compile exec:java

# Uncaught error with the default stack trace (top 1024 frames)
java -cp target/classes com.howtodoinjava.stackoverflow.CrashDemo

# Print every frame, including the caller at the bottom
java -XX:MaxJavaStackTraceDepth=0 -cp target/classes com.howtodoinjava.stackoverflow.CrashDemo

# Recursion depth with the default stack and with a 2 MB stack
java -cp target/classes com.howtodoinjava.stackoverflow.DepthProbe
java -Xss2m -cp target/classes com.howtodoinjava.stackoverflow.DepthProbe
```

The depth numbers depend on the platform, the JDK and the JIT compiler, so they change from machine to machine.
