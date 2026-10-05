package com.howtodoinjava.iae;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public enum Size {
  SMALL, MEDIUM, LARGE;

  /**
   * Safe lookup: trims and upper-cases the input, and returns an empty Optional
   * for null, blank or unknown names instead of throwing IllegalArgumentException.
   */
  public static Optional<Size> parse(String name) {
    if (name == null || name.isBlank()) {
      return Optional.empty();
    }
    String key = name.strip().toUpperCase(Locale.ROOT);
    return Arrays.stream(values())
        .filter(size -> size.name().equals(key))
        .findFirst();
  }
}
