package com.howtodoinjava.core.enums;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class EnumExamples {

  public static void main(String[] args) throws Exception {
    quickReference();
    basics();
    builtInMethods();
    nameVsToString();
    stringToEnum();
    switchExamples();
    fieldsAndMethods();
    abstractMethodsAndInterfaces();
    enumSetAndEnumMap();
    comparing();
    singleton();
    serialization();
  }

  static void quickReference() {
    System.out.println("--- quick reference ---");
    Direction dir = Direction.NORTH;              // NORTH
    Direction[] all = Direction.values();         // [EAST, WEST, NORTH, SOUTH]
    Direction east = Direction.valueOf("EAST");   // EAST
    String name = dir.name();                     // "NORTH"
    int position = dir.ordinal();                 // 2
    boolean same = east == Direction.EAST;        // true
    String axis = switch (dir) {                  // "vertical"
      case EAST, WEST -> "horizontal";
      case NORTH, SOUTH -> "vertical";
    };
    System.out.println(dir + " " + Arrays.toString(all) + " " + east + " " + name + " "
        + position + " " + same + " " + axis);
    try {
      Direction bad = Direction.valueOf("up");    // IllegalArgumentException
      System.out.println(bad);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
    }
  }

  static void basics() {
    System.out.println("--- basics ---");
    Direction north = Direction.NORTH;
    String text = north.toString();                                // "NORTH"
    System.out.println(text);                                      // NORTH
    System.out.println(north.getClass().getSuperclass().getName()); // java.lang.Enum
  }

  static void builtInMethods() {
    System.out.println("--- built-in methods ---");
    Direction[] all = Direction.values();                     // [EAST, WEST, NORTH, SOUTH]
    Direction east = Direction.valueOf("EAST");               // EAST
    String name = Direction.NORTH.name();                     // "NORTH"
    int eastOrdinal = Direction.EAST.ordinal();               // 0
    int northOrdinal = Direction.NORTH.ordinal();             // 2
    int order = Direction.EAST.compareTo(Direction.NORTH);    // -2
    Direction viaClass = Enum.valueOf(Direction.class, "WEST"); // WEST
    Class<Direction> type = Direction.SOUTH.getDeclaringClass(); // class ...Direction

    System.out.println(Arrays.toString(all));
    System.out.println(east + " " + name + " " + eastOrdinal + " " + northOrdinal + " " + order
        + " " + viaClass + " " + type.getSimpleName());

    for (Direction direction : Direction.values()) {
      System.out.println(direction.ordinal() + " " + direction.name());
    }
  }

  enum Size {
    SMALL, LARGE;

    @Override
    public String toString() {
      return name().toLowerCase(Locale.ROOT);
    }
  }

  static void nameVsToString() {
    System.out.println("--- name() vs toString() ---");
    String text = Size.LARGE.toString();     // "large"
    String name = Size.LARGE.name();         // "LARGE"
    System.out.println(text + " " + name);
    try {
      Size size = Size.valueOf("large");
      System.out.println(size);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
    }
  }

  static void stringToEnum() {
    System.out.println("--- String to enum ---");
    try {
      Direction lower = Direction.valueOf("east");
      System.out.println(lower);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
    }
    try {
      Direction missing = Direction.valueOf(null);
      System.out.println(missing);
    } catch (NullPointerException e) {
      System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
    }

    Optional<Direction> found = Direction.fromName(" east ");  // Optional[EAST]
    Optional<Direction> unknown = Direction.fromName("up");     // Optional.empty
    Optional<Direction> noInput = Direction.fromName(null);     // Optional.empty
    Direction direction = Direction.fromName("up").orElse(Direction.NORTH); // NORTH
    System.out.println(found + " " + unknown + " " + noInput + " " + direction);
  }

  static void switchExamples() {
    System.out.println("--- switch ---");
    for (Direction direction : Direction.values()) {
      System.out.println(classicSwitch(direction) + " | " + label(direction) + " | " + describe(direction));
    }
    System.out.println(describe(null));
  }

  // Before Java 14: switch statement with case labels and break
  static String classicSwitch(Direction direction) {
    String axis;
    switch (direction) {
      case EAST:
      case WEST:
        axis = "horizontal";
        break;
      case NORTH:
      case SOUTH:
        axis = "vertical";
        break;
      default:
        throw new IllegalStateException("Unknown direction: " + direction);
    }
    return axis;
  }

  // Java 14+: switch expression, exhaustive without default
  static String label(Direction direction) {
    String label = switch (direction) {
      case EAST, WEST -> "horizontal";
      case NORTH, SOUTH -> "vertical";
    };
    return label;
  }

  // Java 21+: case null and guarded patterns
  static String describe(Direction direction) {
    String text = switch (direction) {
      case null -> "no direction";
      case EAST -> "sunrise side";
      case Direction d when d.getAngle() > 180 -> d.name() + " is past 180 degrees";
      case Direction d -> d.name() + " at " + d.getAngle() + " degrees";
    };
    return text;
  }

  static void fieldsAndMethods() {
    System.out.println("--- fields, constructors, methods ---");
    Direction west = Direction.WEST;
    int angle = west.getAngle();                               // 180
    String abbreviation = west.getAbbreviation();              // "W"
    String message = Direction.NORTH.message();                // "Moving in NORTH direction"
    Optional<Direction> byCode = Direction.fromAbbreviation("s"); // Optional[SOUTH]
    Optional<Direction> byAngle = Direction.fromAngle(450);     // Optional[NORTH]
    System.out.println(angle + " " + abbreviation + " " + message + " " + byCode + " " + byAngle);
  }

  static void abstractMethodsAndInterfaces() {
    System.out.println("--- abstract methods and interfaces ---");
    Direction afterLeft = Turn.LEFT.apply(Direction.EAST);     // NORTH
    Direction afterRight = Turn.RIGHT.apply(Direction.EAST);   // SOUTH
    Direction afterBack = Turn.BACK.apply(Direction.NORTH);    // SOUTH
    System.out.println(afterLeft + " " + afterRight + " " + afterBack);

    Function<Direction, Direction> twoLefts = Turn.LEFT.andThen(Turn.LEFT);
    Direction result = twoLefts.apply(Direction.EAST);         // WEST
    String constantClass = Turn.LEFT.getClass().getName();     // ...Turn$1
    Class<Turn> declaring = Turn.LEFT.getDeclaringClass();     // ...Turn
    System.out.println(result + " " + constantClass + " " + declaring.getName());
  }

  static void enumSetAndEnumMap() {
    System.out.println("--- EnumSet and EnumMap ---");
    Set<Direction> horizontal = EnumSet.of(Direction.WEST, Direction.EAST); // [EAST, WEST]
    Set<Direction> vertical = EnumSet.complementOf(EnumSet.copyOf(horizontal)); // [NORTH, SOUTH]
    Set<Direction> all = EnumSet.allOf(Direction.class);       // [EAST, WEST, NORTH, SOUTH]
    Set<Direction> none = EnumSet.noneOf(Direction.class);     // []
    Set<Direction> range = EnumSet.range(Direction.WEST, Direction.SOUTH); // [WEST, NORTH, SOUTH]
    boolean hasNorth = vertical.contains(Direction.NORTH);     // true
    System.out.println(horizontal + " " + vertical + " " + all + " " + none + " " + range + " " + hasNorth);

    try {
      horizontal.add(null);
    } catch (NullPointerException e) {
      System.out.println("EnumSet.add(null): NullPointerException");
    }

    Map<Direction, Integer> steps = new EnumMap<>(Direction.class);
    steps.put(Direction.SOUTH, 2);
    steps.put(Direction.EAST, 5);
    steps.merge(Direction.EAST, 1, Integer::sum);              // EAST=6
    int northSteps = steps.getOrDefault(Direction.NORTH, 0);   // 0
    String printed = steps.toString();                         // {EAST=6, SOUTH=2}
    System.out.println(printed + " " + northSteps);            // {EAST=6, SOUTH=2} 0

    try {
      steps.put(null, 1);
    } catch (NullPointerException e) {
      System.out.println("EnumMap.put(null, 1): NullPointerException");
    }
  }

  static void comparing() {
    System.out.println("--- comparing ---");
    Direction east = Direction.EAST;
    Direction eastNew = Direction.valueOf("EAST");
    Direction nothing = null;

    boolean same = east == eastNew;                            // true
    boolean equal = east.equals(eastNew);                      // true
    boolean nullSafe = nothing == Direction.EAST;              // false
    boolean otherType = Direction.EAST.equals(Turn.LEFT);      // false, compiles
    System.out.println(same + " " + equal + " " + nullSafe + " " + otherType);
    try {
      boolean crash = nothing.equals(Direction.EAST);
      System.out.println(crash);
    } catch (NullPointerException e) {
      System.out.println("nothing.equals(EAST): NullPointerException");
    }
  }

  static void singleton() {
    System.out.println("--- singleton ---");
    PathUtils first = PathUtils.getInstance();
    boolean sameInstance = first == PathUtils.INSTANCE;        // true
    System.out.println(sameInstance + " " + first.getRootPath().toAbsolutePath().isAbsolute());
  }

  static void serialization() throws IOException, ClassNotFoundException {
    System.out.println("--- serialization ---");
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
      out.writeObject(Direction.NORTH);
    }
    try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
      Direction restored = (Direction) in.readObject();
      boolean sameConstant = restored == Direction.NORTH;     // true
      System.out.println(restored + " " + sameConstant);
    }
  }
}
