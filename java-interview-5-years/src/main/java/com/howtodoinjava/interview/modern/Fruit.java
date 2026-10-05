package com.howtodoinjava.interview.modern;

import java.util.Objects;

public record Fruit(String name, int count) {

  public Fruit {                       // compact constructor validates the components
    Objects.requireNonNull(name, "name");
    if (count < 0) {
      throw new IllegalArgumentException("count must be >= 0");
    }
  }
}
