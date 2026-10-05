package com.howtodoinjava.core.serialization;

import java.io.Serial;
import java.io.Serializable;

/**
 * A serializable class with the same validation as {@link Recipe}. Default deserialization
 * does not call the constructor, so a tampered stream can create an invalid RecipeCard.
 */
public class RecipeCard implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private final String name;
  private final int minutes;

  public RecipeCard(String name, int minutes) {
    if (minutes < 0) {
      throw new IllegalArgumentException("minutes must be >= 0, but was " + minutes);
    }
    this.name = name;
    this.minutes = minutes;
  }

  public String getName() {
    return name;
  }

  public int getMinutes() {
    return minutes;
  }

  @Override
  public String toString() {
    return "RecipeCard[name=" + name + ", minutes=" + minutes + "]";
  }
}
