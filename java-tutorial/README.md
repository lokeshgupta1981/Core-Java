Source code for the article https://howtodoinjava.com/?p=44234

# Java Tutorial for Beginners (Java 25)

The first programs from the tutorial: a compact source file with an instance main method (Java 25),
the classic class with `public static void main(String[] args)`, and a small Maven project with JUnit 6 tests.

## Versions

- JDK 25 (tested with Temurin 25.0.4.1)
- Maven 3.9.16
- JUnit 6.1.3
- maven-compiler-plugin 3.16.0, maven-surefire-plugin 3.6.0

## Files

| File | What it shows |
|---|---|
| `scripts/Hello.java` | Smallest Java 25 program: `void main()` and `IO.println()` |
| `scripts/Greeting.java` | Compact source file that reads input with `IO.readln()` and uses `List` without an import |
| `scripts/HelloWorld.java` | Classic class with `public static void main(String[] args)` |
| `src/main/java/.../Greeter.java` | Greeting logic that handles a missing name |
| `src/main/java/.../App.java` | Classic main class that uses `Greeter` |
| `src/test/java/.../GreeterTest.java` | JUnit 6 tests for `Greeter` |
| `src/test/java/.../FirstProgramsTest.java` | Runs the three programs in `scripts/` with `java` and `javac` and checks their output |

## Run

```bash
# compact source files (no compile step)
cd scripts
java Hello.java
echo Lokesh | java Greeting.java
java Greeting.java < /dev/null          # empty input: Hello, stranger!

# classic class
javac HelloWorld.java
java HelloWorld
cd ..

# Maven project: all tests
mvn test

# run the classic main class of the Maven project
mvn -q compile
java -cp target/classes com.howtodoinjava.tutorial.App Lokesh
```
