package com.howtodoinjava.core.serialization;

import static com.howtodoinjava.core.serialization.SerializationUtil.fromBytes;
import static com.howtodoinjava.core.serialization.SerializationUtil.replaceInt;
import static com.howtodoinjava.core.serialization.SerializationUtil.replaceLong;
import static com.howtodoinjava.core.serialization.SerializationUtil.toBytes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Runs every example of the article "Java Serialization" and prints each result.
 * Run with: mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.core.serialization.JavaSerializationDemo
 */
public class JavaSerializationDemo {

  public static void main(String[] args) throws Exception {
    quickStart();
    sharedReferences();
    notSerializable();
    transientAndStatic();
    versionMismatch();
    records();
    customReadObject();
    externalizable();
    inheritance();
    singleton();
    filters();
  }

  static void quickStart() throws Exception {
    System.out.println("== 1. Quick start");
    Path file = Files.createTempFile("recipe", ".ser");

    // 1. Serialize: object -> bytes in a file
    try (var out = new ObjectOutputStream(new FileOutputStream(file.toFile()))) {
      out.writeObject(new Recipe("Pasta", 20));
    }
    System.out.println("file size = " + Files.size(file) + " bytes");

    // 2. Deserialize: bytes -> a new object
    try (var in = new ObjectInputStream(new FileInputStream(file.toFile()))) {
      Recipe copy = (Recipe) in.readObject();
      System.out.println("copy = " + copy);
      System.out.println("equals = " + copy.equals(new Recipe("Pasta", 20)));
    }
    Files.delete(file);
  }

  static void sharedReferences() throws Exception {
    System.out.println("== 2. Object graph with a shared reference");
    Recipe soup = new Recipe("Soup", 30);
    List<Recipe> week = new ArrayList<>(List.of(soup, soup));
    @SuppressWarnings("unchecked")
    List<Recipe> copy = (List<Recipe>) fromBytes(toBytes(week));
    System.out.println("copy = " + copy);
    System.out.println("same object in copy = " + (copy.get(0) == copy.get(1)));
    System.out.println("same as original = " + (copy.get(0) == soup));
  }

  static void notSerializable() {
    System.out.println("== 3. NotSerializableException");
    try {
      new ObjectOutputStream(OutputStream.nullOutputStream())
          .writeObject(new MealPlan("Monday", new MenuItem("Salad")));
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  static void transientAndStatic() throws Exception {
    System.out.println("== 4. transient and static fields");
    Cookbook book = new Cookbook("Weeknight Meals", List.of(new Recipe("Pasta", 20), new Recipe("Soup", 30)));
    byte[] data = toBytes(book);
    Cookbook.publisher = "City Books";      // change the static field after writing
    Cookbook copy = (Cookbook) fromBytes(data);
    System.out.println("title = " + copy.getTitle());
    System.out.println("recipes = " + copy.getRecipes());
    System.out.println("totalMinutes = " + copy.getTotalMinutes());
    System.out.println("publisher = " + Cookbook.publisher);
    Cookbook.publisher = "Home Press";
  }

  static void versionMismatch() throws Exception {
    System.out.println("== 5. serialVersionUID mismatch");
    long suid = ObjectStreamClass.lookup(RecipeCard.class).getSerialVersionUID();
    System.out.println("RecipeCard serialVersionUID = " + suid);
    System.out.println("Recipe (record) serialVersionUID = "
        + ObjectStreamClass.lookup(Recipe.class).getSerialVersionUID());
    // simulate a file written by an older RecipeCard that declared serialVersionUID = 2L
    byte[] old = replaceLong(toBytes(new RecipeCard("Pasta", 20)), 1L, 2L);
    try {
      fromBytes(old);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  static void records() throws Exception {
    System.out.println("== 6. Records use the canonical constructor");
    byte[] tamperedRecipe = replaceInt(toBytes(new Recipe("Pasta", 20)), 20, -5);
    try {
      fromBytes(tamperedRecipe);
    } catch (Exception e) {
      System.out.println(e);
      System.out.println("cause: " + e.getCause());
    }
    byte[] tamperedRecipeCard = replaceInt(toBytes(new RecipeCard("Pasta", 20)), 20, -5);
    System.out.println("class: " + fromBytes(tamperedRecipeCard));
  }

  static void customReadObject() throws Exception {
    System.out.println("== 7. readObject() validation");
    byte[] data = toBytes(new Cookbook("Weeknight Meals", List.of(new Recipe("Pasta", 20))));
    // simulate a stream where the title is blank: same length, so only the bytes change
    String text = new String(data, java.nio.charset.StandardCharsets.ISO_8859_1)
        .replace("Weeknight Meals", "               ");
    try {
      fromBytes(text.getBytes(java.nio.charset.StandardCharsets.ISO_8859_1));
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  static void externalizable() throws Exception {
    System.out.println("== 8. Externalizable");
    Ingredient copy = (Ingredient) fromBytes(toBytes(new Ingredient("flour", 500)));
    System.out.println("copy = " + copy);
    System.out.println("Ingredient bytes = " + toBytes(new Ingredient("flour", 500)).length);
  }

  static void inheritance() throws Exception {
    System.out.println("== 9. Non-serializable parent");
    Dessert copy = (Dessert) fromBytes(toBytes(new Dessert("Brownie", 30)));
    System.out.println("copy = " + copy);
    byte[] drink = toBytes(new Drink("Lemonade", 250));
    System.out.println("Drink written: " + drink.length + " bytes");
    try {
      fromBytes(drink);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  static void singleton() throws Exception {
    System.out.println("== 10. Singleton and readResolve()");
    Object noResolve = fromBytes(toBytes(KitchenSettingsNoResolve.INSTANCE));
    System.out.println("without readResolve: same = " + (noResolve == KitchenSettingsNoResolve.INSTANCE));
    Object withResolve = fromBytes(toBytes(KitchenSettings.INSTANCE));
    System.out.println("with readResolve: same = " + (withResolve == KitchenSettings.INSTANCE));
  }

  static void filters() throws Exception {
    System.out.println("== 11. Deserialization filter");
    ObjectInputFilter onlyRecipes = ObjectInputFilter.Config.createFilter(
        "com.howtodoinjava.core.serialization.Recipe;java.base/*;!*");
    System.out.println("allowed = " + fromBytes(toBytes(new Recipe("Pasta", 20)), onlyRecipes));
    try {
      fromBytes(toBytes(new Dessert("Brownie", 30)), onlyRecipes);
    } catch (Exception e) {
      System.out.println(e);
    }
    ObjectInputFilter limits = ObjectInputFilter.Config.createFilter("maxdepth=5;maxarray=1000;maxbytes=10000");
    try {
      fromBytes(toBytes(new int[5000]), limits);
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}
