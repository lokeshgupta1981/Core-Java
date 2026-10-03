package com.howtodoinjava.core.serialization;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Shows transient and static fields, and custom writeObject()/readObject() methods that
 * validate the data and rebuild a transient field after deserialization.
 */
public class Cookbook implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  static String publisher = "Home Press";   // static: belongs to the class, never written

  private final String title;
  private List<Recipe> recipes;
  private transient int totalMinutes;       // transient: not written, recomputed in readObject()

  public Cookbook(String title, List<Recipe> recipes) {
    this.title = title;
    this.recipes = new ArrayList<>(recipes);
    this.totalMinutes = sum(this.recipes);
  }

  private static int sum(List<Recipe> recipes) {
    return recipes.stream().mapToInt(Recipe::minutes).sum();
  }

  @Serial
  private void writeObject(ObjectOutputStream out) throws IOException {
    out.defaultWriteObject();               // writes title and recipes
  }

  @Serial
  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();                 // reads title and recipes
    if (title == null || title.isBlank()) {
      throw new InvalidObjectException("Cookbook title is missing");
    }
    recipes = new ArrayList<>(recipes);     // defensive copy
    totalMinutes = sum(recipes);            // rebuild the transient field
  }

  public String getTitle() {
    return title;
  }

  public List<Recipe> getRecipes() {
    return recipes;
  }

  public int getTotalMinutes() {
    return totalMinutes;
  }
}
