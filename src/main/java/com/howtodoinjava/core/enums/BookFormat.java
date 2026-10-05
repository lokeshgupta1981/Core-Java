package com.howtodoinjava.core.enums;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Enum with String values: a display label and a short code per constant.
 */
public enum BookFormat {

  HARDCOVER("Hardcover", "HC"),
  PAPERBACK("Paperback", "PB"),
  EBOOK("E-Book", "EB");

  // Built once, after all constants exist; keys are lower case for a case-insensitive lookup
  private static final Map<String, BookFormat> BY_LABEL = Arrays.stream(values())
      .collect(Collectors.toUnmodifiableMap(
          format -> format.label.toLowerCase(Locale.ROOT), Function.identity()));

  private final String label;
  private final String code;

  // Enum constructors are always private
  BookFormat(String label, String code) {
    this.label = label;
    this.code = code;
  }

  public String getLabel() {
    return label;
  }

  public String getCode() {
    return code;
  }

  // Safe lookup by constant name: ignores case and blanks, never throws
  public static Optional<BookFormat> fromName(String name) {
    if (name == null) {
      return Optional.empty();
    }
    String key = name.strip();
    return Arrays.stream(values())
        .filter(format -> format.name().equalsIgnoreCase(key))
        .findFirst();
  }

  // Lookup by the label field with a stream
  public static Optional<BookFormat> fromLabel(String label) {
    if (label == null) {
      return Optional.empty();
    }
    String key = label.strip();
    return Arrays.stream(values())
        .filter(format -> format.label.equalsIgnoreCase(key))
        .findFirst();
  }

  // Lookup by the label field with the static map
  public static Optional<BookFormat> fromLabelMap(String label) {
    if (label == null) {
      return Optional.empty();
    }
    String key = label.strip().toLowerCase(Locale.ROOT);
    return Optional.ofNullable(BY_LABEL.get(key));
  }

  // Lookup by the second value
  public static Optional<BookFormat> fromCode(String code) {
    if (code == null) {
      return Optional.empty();
    }
    String key = code.strip();
    return Arrays.stream(values())
        .filter(format -> format.code.equalsIgnoreCase(key))
        .findFirst();
  }
}
