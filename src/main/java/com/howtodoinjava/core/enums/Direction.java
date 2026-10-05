package com.howtodoinjava.core.enums;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Direction {

  EAST(0, "E"), WEST(180, "W"), NORTH(90, "N"), SOUTH(270, "S");

  // Built once, after all constants exist
  private static final Map<String, Direction> BY_ABBREVIATION = Arrays.stream(values())
      .collect(Collectors.toUnmodifiableMap(Direction::getAbbreviation, Function.identity()));

  private final int angle;
  private final String abbreviation;

  // Enum constructors are always private
  Direction(int angle, String abbreviation) {
    this.angle = angle;
    this.abbreviation = abbreviation;
  }

  public int getAngle() {
    return angle;
  }

  public String getAbbreviation() {
    return abbreviation;
  }

  public String message() {
    String message = "Moving in " + this + " direction";
    return message;
  }

  // Safe lookup by name: ignores case and blanks, never throws
  public static Optional<Direction> fromName(String name) {
    if (name == null) {
      return Optional.empty();
    }
    String key = name.strip();
    return Arrays.stream(values())
        .filter(direction -> direction.name().equalsIgnoreCase(key))
        .findFirst();
  }

  // Reverse lookup by a second value
  public static Optional<Direction> fromAbbreviation(String abbreviation) {
    if (abbreviation == null) {
      return Optional.empty();
    }
    String key = abbreviation.strip().toUpperCase(Locale.ROOT);
    return Optional.ofNullable(BY_ABBREVIATION.get(key));
  }

  // Reverse lookup by angle, e.g. 450 or -270 -> NORTH
  public static Optional<Direction> fromAngle(int angle) {
    int normalized = Math.floorMod(angle, 360);
    return Arrays.stream(values())
        .filter(direction -> direction.angle == normalized)
        .findFirst();
  }
}
