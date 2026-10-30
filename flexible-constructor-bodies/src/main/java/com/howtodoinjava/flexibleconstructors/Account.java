package com.howtodoinjava.flexibleconstructors;

import java.util.concurrent.atomic.AtomicInteger;

public class Account {

  static final AtomicInteger OPENED = new AtomicInteger();

  private final String id;
  private final String summary;

  public Account(String id) {
    this.id = id;
    OPENED.incrementAndGet();
    this.summary = describe();
  }

  public String id() {
    return id;
  }

  public String describe() {
    return "Account " + id;
  }

  public String summary() {
    return summary;
  }
}
