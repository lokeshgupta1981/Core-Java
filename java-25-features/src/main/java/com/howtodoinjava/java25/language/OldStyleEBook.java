package com.howtodoinjava.java25.language;

/** The Java 21 way: the field is assigned after super(...) returns. */
public class OldStyleEBook extends Book {

  private final String format;

  public OldStyleEBook(String title, int pages, String format) {
    super(title, pages);
    this.format = format.strip().toUpperCase();
  }

  @Override
  protected String describe() {
    return title() + " (" + format + ")";
  }
}
