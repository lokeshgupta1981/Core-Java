Source code for the article https://howtodoinjava.com/?p=44413

# Flexible Constructor Bodies in Java 25

Runnable examples for JEP 513: validating arguments, preparing values and assigning fields before `super(...)` or `this(...)`, the prologue rules with the exact compiler errors, prologue and epilogue order in a hierarchy, and prologues in record constructors. Every snippet in the article has a JUnit test.

## Versions

- JDK 25 (tested with OpenJDK 25.0.4.1)
- Maven 3.9.11
- JUnit 6.1.3, AssertJ 3.27.7
- maven-compiler-plugin 3.16.0, maven-surefire-plugin 3.6.0

## Run

```bash
mvn test
```

## Files

| File | What it shows |
|---|---|
| `Account` | Superclass that counts opened accounts and calls the overridable `describe()` from its constructor |
| `LegacySavingsAccount` | The pre-Java 25 style: validation after `super()`, and `describe()` seeing a rate of 0.0 |
| `HelperSavingsAccount` | The old workaround: a static helper inside the `super(...)` arguments |
| `SavingsAccount` | Validation and field assignment before `super()`, and parsing before `this(...)` |
| `Money` | A record whose non-canonical constructor parses and validates before `this(...)` |
| `ConstructionOrder` | Three-level hierarchy that logs prologue and epilogue order |
| `PrologueRulesTest` | Compiles code in memory and checks each prologue compiler error, including `--release 21` |
