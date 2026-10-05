package com.howtodoinjava.java26.finalfields;

import java.lang.reflect.Field;

/** JEP 500: mutating a final field with deep reflection prints a warning (default) or fails (deny). */
public class FinalFieldMutation {

  public static void main(String[] args) {
    Recipe soup = new Recipe(2);
    try {
      Field servings = Recipe.class.getDeclaredField("servings");
      servings.setAccessible(true);
      servings.set(soup, 4);
      System.out.println("servings = " + soup.servings());
    } catch (ReflectiveOperationException e) {
      System.out.println("blocked: " + e);
    }
  }
}
