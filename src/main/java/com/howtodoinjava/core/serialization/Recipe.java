package com.howtodoinjava.core.serialization;

import java.io.Serializable;

/**
 * A serializable record. On deserialization, Java calls the canonical constructor,
 * so the validation in the compact constructor also runs for objects read from a stream.
 */
public record Recipe(String name, int minutes) implements Serializable {

  public Recipe {
    if (minutes < 0) {
      throw new IllegalArgumentException("minutes must be >= 0, but was " + minutes);
    }
  }
}
