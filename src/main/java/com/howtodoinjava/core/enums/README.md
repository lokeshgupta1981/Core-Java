Source code for the article https://howtodoinjava.com/java/enum/enum-tutorial/

- Java 25 (also compiles with Java 21, the release set in the repository pom.xml)
- Jackson 2.x for EnumJsonExample (jackson-databind from the repository pom.xml; output checked with 2.15.1 and 2.22.3)

Files

- Direction.java: enum with fields, a constructor, methods and safe lookups (fromName, fromAbbreviation, fromAngle)
- Turn.java: abstract method per constant, implements UnaryOperator<Direction>
- PathUtils.java: enum singleton
- EnumExamples.java: values(), valueOf(), ordinal(), switch, EnumSet, EnumMap, comparison, serialization
- EnumJsonExample.java: enums with Jackson (@JsonValue, unknown values)

Run from the repository root

    mvn -q compile
    java -cp target/classes com.howtodoinjava.core.enums.EnumExamples
