package com.howtodoinjava.java26.finalfields;

public class Recipe {

  private final int servings;

  public Recipe(int servings) {
    this.servings = servings;
  }

  public int servings() {
    return servings;
  }
}
