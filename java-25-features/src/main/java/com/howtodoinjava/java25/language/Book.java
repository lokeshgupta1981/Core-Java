package com.howtodoinjava.java25.language;

import java.util.Objects;

public class Book {

  private final String title;
  private final int pages;
  private final String summary;

  public Book(String title, int pages) {
    this.title = Objects.requireNonNull(title, "title");
    this.pages = pages;
    this.summary = describe();        // calls the subclass override
  }

  protected String describe() {
    return title;
  }

  public String title() {
    return title;
  }

  public int pages() {
    return pages;
  }

  public String summary() {
    return summary;
  }
}
