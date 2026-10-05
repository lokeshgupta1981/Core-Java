package com.howtodoinjava.java25.language;

import java.util.Objects;

/** JEP 513: statements before super(...). */
public class EBook extends Book {

  private final String format;

  public EBook(String title, int pages, String format) {
    Objects.requireNonNull(format, "format");                        // 1. validate arguments
    if (pages <= 0) {
      throw new IllegalArgumentException("pages must be positive: " + pages);
    }
    this.format = format.strip().toUpperCase();                      // 2. initialize own field
    super(title, pages);                                             // 3. call the superclass
  }

  @Override
  protected String describe() {
    return title() + " (" + format + ")";
  }

  public String format() {
    return format;
  }
}
