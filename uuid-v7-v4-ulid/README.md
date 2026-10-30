Source code for the article https://howtodoinjava.com/?p=48038

# UUIDv7 vs UUIDv4 vs ULID: Which ID to Use in Java

Runnable comparisons of UUIDv4, UUIDv7 and ULID: version bits, text form, sort order across and within milliseconds, reading the embedded timestamp, ULID and UUID conversion, Hibernate's UUIDv7 generator, and an H2 insert benchmark. Every snippet in the article has a JUnit test.

## Versions

- JDK 25 (tested with OpenJDK 25.0.4.1)
- Maven 3.9.11
- uuid-creator 6.1.1, ulid-creator 5.2.4, jakarta.persistence-api 3.2.0, H2 2.5.252, Hibernate ORM 7.4.14.Final (tests only)
- JUnit 6.1.3, AssertJ 3.27.7
- maven-compiler-plugin 3.16.0, maven-surefire-plugin 3.6.0

## Run

```bash
# all tests
mvn test

# insert benchmark: rows, runs (takes about a minute for the defaults)
mvn compile dependency:build-classpath -Dmdep.outputFile=cp.txt
java -cp "target/classes:$(cat cp.txt)" com.howtodoinjava.ids.InsertBenchmark 1000000 5
```

## Files

| File | What it shows |
|---|---|
| `SampleIds` | Prints example UUIDv4, UUIDv7 and ULID values, as quoted in the article (the values differ on every run) |
| `UuidV7` | A plain Java 25 UUIDv7 generator with random bits only (no monotonic counter) |
| `PurchaseOrder` | JPA entity that assigns a UUIDv7 in its constructor |
| `PurchaseOrderTest` | The entity gets version 7 IDs in creation order |
| `InsertBenchmark` | Inserts rows with a UUID primary key into H2 and prints time and file size |
| `AnatomyTest` | Version and variant bits, 36 vs 26 characters, case-insensitive ULID parsing |
| `OrderingTest` | v7 and ULID sort by creation time, v4 does not; `UUID.compareTo()` on v7 |
| `SameMillisecondTest` | Random v7 and plain ULID lose order inside a millisecond; monotonic generators keep it |
| `TimestampTest` | Reading the creation time from v7 and ULID; `UUID.timestamp()` fails for v4 and v7 |
| `ConversionTest` | ULID to UUID and back; a ULID in a UUID column has no valid version |
| `GenerationTest` | One-line generation of each ID: `UUID.randomUUID()`, `getTimeOrderedEpoch()`, `getMonotonicUlid()` |
| `Shipment`, `HibernateUuidV7Test` | Hibernate `@UuidGenerator(style = VERSION_7)` with H2: version 7 IDs, ascending, also across 1,000 inserts in a row |
