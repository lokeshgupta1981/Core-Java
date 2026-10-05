# Java Exception Handling Examples

## NullPointerException

Source code for the article [Java NullPointerException: How to Fix and Avoid It](https://howtodoinjava.com/java/exception-handling/how-to-effectively-handle-nullpointerexception-in-java/).

- `SampleNPE.java`: the smallest program that throws an NPE, with the helpful message (JEP 358).
- `NpeCauses.java`: common causes (uninitialized field, Map.get() miss with unboxing, method returning null, null array element, chained calls, synchronized, throw null, switch on null, String.valueOf(null), List.of(null)) and the message each one prints.
- `NpeFixes.java`: null checks, Objects.requireNonNull, requireNonNullElse, getOrDefault, Optional, "literal".equals(var), empty collections, record validation, null-safe operators and Java 21 `case null`.

Java 21 or later. Run from the repo root:

```bash
mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.core.exceptions.NpeCauses
mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.core.exceptions.NpeFixes
```

Maven compiles with debug information, so the messages show local variable names. With plain `javac`, add `-g`.
