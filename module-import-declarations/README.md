Source code for the article https://howtodoinjava.com/?p=44237

# Module Import Declarations in Java 25 (JEP 511)

Examples for `import module`: importing every package of `java.base` with one line, resolving
ambiguous names such as `List` and `Date`, transitive module imports (`java.sql`, `java.se`) and the
implicit `import module java.base` in compact source files. The project has no `module-info.java`,
so all classes run on the class path.

## Versions

- JDK 25 (tested with Temurin 25.0.4.1), no preview flags needed
- Maven 3.9.16
- JUnit 6.1.3, AssertJ 3.27.7
- maven-compiler-plugin 3.16.0, maven-surefire-plugin 3.6.0

## Files

| File | What it shows |
|---|---|
| `QuickReference.java` | `import module java.base` replaces imports from `java.util`, `java.util.stream`, `java.nio.file` and `java.time` |
| `FruitStats.java` | A normal class (not in a module) that uses many `java.base` packages |
| `FruitColors.java` | `java.base` + `java.desktop` with `import java.util.List` to resolve the `List` ambiguity |
| `PriceDates.java` | `import module java.sql` also imports `java.util.logging` (transitive dependency) |
| `examples/AmbiguousList.java` | Fails on purpose: reference to List is ambiguous |
| `examples/OnDemandFix.java` | `import java.util.*` also resolves the ambiguity |
| `examples/AmbiguousDate.java` | Fails on purpose: reference to Date is ambiguous (`java.base` + `java.sql`) |
| `examples/WholeApi.java` | `import module java.se` needs `--add-modules java.se` on the class path |
| `examples/Hello.java` | Compact source file, `java.base` is imported implicitly |
| `CompilerOutputTest` | Compiles the files in `examples/` with javac and checks the real error messages |
| `JavaBaseExportsTest` | Lists the 58 packages that `java.base` exports in JDK 25 |

## Run

```bash
# build and run all tests
mvn test

# run the examples
java -cp target/classes com.howtodoinjava.moduleimport.QuickReference
java -cp target/classes com.howtodoinjava.moduleimport.FruitColors
java -cp target/classes com.howtodoinjava.moduleimport.PriceDates

# compile every file in examples/ and print the javac output (two files fail on purpose)
bash examples/compile-examples.sh

# compact source file
java examples/Hello.java
```
