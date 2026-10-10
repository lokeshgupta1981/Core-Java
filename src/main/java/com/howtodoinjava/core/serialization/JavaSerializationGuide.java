package com.howtodoinjava.core.serialization;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;
import java.util.function.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.time.*;
import java.math.*;
import java.nio.ByteBuffer;

/**
 * Examples for the tutorial "Java Serialization with Serializable and serialVersionUID".
 * https://howtodoinjava.com/java/serialization/java-serialization/
 */
public class JavaSerializationGuide {
    static record MealPlan(String day, MenuItem item) implements Serializable { }
    static byte[] toBytes(Object value) throws IOException {
        var bytes = new ByteArrayOutputStream();
        try (var out = new ObjectOutputStream(bytes)) {
            out.writeObject(value);
        }
        return bytes.toByteArray();
    }
    static Object fromBytes(byte[] data) throws IOException, ClassNotFoundException {
        try (var in = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return in.readObject();
        }
    }
    static public class Pantry implements Serializable {
        String owner = "Lokesh";
        int shelves = 4;      // field added after the file was written
    }
    static public record Recipe(String name, int minutes) implements Serializable {
        public Recipe {
            if (minutes < 0) {
                throw new IllegalArgumentException("minutes must be >= 0, but was " + minutes);
            }
        }
    }
    static public class RecipeCard implements Serializable {
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

        @Override
        public String toString() {
            return "RecipeCard[name=" + name + ", minutes=" + minutes + "]";
        }
    }
    static byte[] replaceInt(byte[] data, int oldValue, int newValue) {
        byte[] old = ByteBuffer.allocate(4).putInt(oldValue).array();
        byte[] copy = data.clone();
        for (int i = copy.length - 4; i >= 0; i--) {
            if (Arrays.equals(copy, i, i + 4, old, 0, 4)) {
                ByteBuffer.wrap(copy).putInt(i, newValue);
                return copy;
            }
        }
        throw new IllegalArgumentException("value not found: " + oldValue);
    }
    static public class Cookbook implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        static String publisher = "Home Press";   // static: never written

        private final String title;
        private List<Recipe> recipes;
        private transient int totalMinutes;       // transient: not written, rebuilt in readObject()

        public Cookbook(String title, List<Recipe> recipes) {
            this.title = title;
            this.recipes = new ArrayList<>(recipes);
            this.totalMinutes = sum(this.recipes);
        }

        public String getTitle() { return title; }
        public List<Recipe> getRecipes() { return recipes; }
        public int getTotalMinutes() { return totalMinutes; }

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
    }
    static public class Ingredient implements Externalizable {
        private String name;
        private int grams;

        public Ingredient() { }                   // required: public no-arg constructor

        public Ingredient(String name, int grams) {
            this.name = name;
            this.grams = grams;
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeUTF(name);
            out.writeInt(grams);
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException {
            name = in.readUTF();                    // same order as writeExternal()
            grams = in.readInt();
        }

        @Override
        public String toString() {
            return "Ingredient[name=" + name + ", grams=" + grams + "]";
        }
    }
    static public class MenuItem {                                   // not Serializable
        protected String name;

        public MenuItem() { this.name = "unknown"; }
        public MenuItem(String name) { this.name = name; }
    }
    static public class Dessert extends MenuItem implements Serializable {
        private final int sugarGrams;

        public Dessert(String name, int sugarGrams) {
            super(name);
            this.sugarGrams = sugarGrams;
        }

        @Override
        public String toString() {
            return "Dessert[name=" + name + ", sugarGrams=" + sugarGrams + "]";
        }
    }
    static public class BarItem {                                    // not Serializable, no no-arg constructor
        protected final String name;

        public BarItem(String name) { this.name = name; }
    }

    static public class Drink extends BarItem implements Serializable {
        public Drink(String name) { super(name); }
    }
    static public final class KitchenSettings implements Serializable {
        public static final KitchenSettings INSTANCE = new KitchenSettings();

        private KitchenSettings() { }

        @Serial
        private Object readResolve() {
            return INSTANCE;                      // drop the copy, return the one instance
        }
    }

    static public final class PlainSettings implements Serializable {   // the same singleton without readResolve()
        public static final PlainSettings INSTANCE = new PlainSettings();

        private PlainSettings() { }
    }
    static Object readFiltered(byte[] data, ObjectInputFilter filter) throws IOException, ClassNotFoundException {
        try (var in = new ObjectInputStream(new ByteArrayInputStream(data))) {
            in.setObjectInputFilter(filter);
            return in.readObject();
        }
    }
    public static void main(String[] args) throws Exception {
        {
            // 1. Serialize: object -> bytes in a file
            try (var out = new ObjectOutputStream(new FileOutputStream("recipe.ser"))) {
                out.writeObject(new Recipe("Pasta", 20));
            }

            // 2. Deserialize: bytes -> a new object
            try (var in = new ObjectInputStream(new FileInputStream("recipe.ser"))) {
                Recipe copy = (Recipe) in.readObject();                  // Recipe[name=Pasta, minutes=20]
                boolean same = copy.equals(new Recipe("Pasta", 20));     // true
            }

            // 3. Every field must be serializable too (MenuItem is not Serializable)
            var sink = new ObjectOutputStream(OutputStream.nullOutputStream());
            try { sink.writeObject(new MealPlan("Monday", new MenuItem("Salad")));  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            Recipe copy = (Recipe) fromBytes(toBytes(new Recipe("Pasta", 20)));   // Recipe[name=Pasta, minutes=20]
            show("copy", copy);
        }
        {
            Recipe soup = new Recipe("Soup", 30);
            List<Recipe> week = new ArrayList<>(List.of(soup, soup));

            List<Recipe> copy = (List<Recipe>) fromBytes(toBytes(week));
            boolean shared = copy.get(0) == copy.get(1);     // true  (one Soup object in the copy)
            show("shared", shared);
            boolean original = copy.get(0) == soup;          // false (the copy is a new object)
            show("original", original);
        }
        {
            byte[] data = toBytes(new Cookbook("Weeknight Meals", List.of(new Recipe("Pasta", 20), new Recipe("Soup", 30))));
            Cookbook.publisher = "City Books";

            Cookbook copy = (Cookbook) fromBytes(data);
            String title = copy.getTitle();                // "Weeknight Meals"
            show("title", title);
            List<Recipe> recipes = copy.getRecipes();      // [Recipe[name=Pasta, minutes=20], Recipe[name=Soup, minutes=30]]
            show("recipes", recipes);
            int total = copy.getTotalMinutes();            // 50 (0 without the readObject() in section 6)
            show("total", total);
            String publisher = Cookbook.publisher;         // "City Books" (the current class value, not the written one)
            show("publisher", publisher);
        }
        {
            try { Object recipe = fromBytes(replaceInt(toBytes(new Recipe("Pasta", 20)), 20, -5)); show("recipe", recipe); } catch (Throwable _t) { System.out.println("recipe -> " + _t); }
            Object card = fromBytes(replaceInt(toBytes(new RecipeCard("Pasta", 20)), 20, -5));     // RecipeCard[name=Pasta, minutes=-5]
            show("card", card);
        }
        {
            try { Object blank = fromBytes(toBytes(new Cookbook(" ", List.of()))); show("blank", blank); } catch (Throwable _t) { System.out.println("blank -> " + _t); }
        }
        {
            Object flour = fromBytes(toBytes(new Ingredient("flour", 500)));   // Ingredient[name=flour, grams=500]
            show("flour", flour);
        }
        {
            Object dessert = fromBytes(toBytes(new Dessert("Brownie", 30)));   // Dessert[name=unknown, sugarGrams=30]
            show("dessert", dessert);
        }
        {
            try { Object drink = fromBytes(toBytes(new Drink("Lemonade"))); show("drink", drink); } catch (Throwable _t) { System.out.println("drink -> " + _t); }
        }
        {
            boolean plain = fromBytes(toBytes(PlainSettings.INSTANCE)) == PlainSettings.INSTANCE;         // false
            show("plain", plain);
            boolean resolved = fromBytes(toBytes(KitchenSettings.INSTANCE)) == KitchenSettings.INSTANCE;  // true
            show("resolved", resolved);
        }
        {
            ObjectInputFilter onlyRecipes = ObjectInputFilter.Config.createFilter(Recipe.class.getName() + ";java.base/*;!*");
            Object pasta = readFiltered(toBytes(new Recipe("Pasta", 20)), onlyRecipes);        // Recipe[name=Pasta, minutes=20]
            show("pasta", pasta);
            try { Object brownie = readFiltered(toBytes(new Dessert("Brownie", 30)), onlyRecipes); show("brownie", brownie); } catch (Throwable _t) { System.out.println("brownie -> " + _t); }
        }
        {
            ObjectInputFilter limits = ObjectInputFilter.Config.createFilter("maxdepth=5;maxarray=1000;maxbytes=10000");
            try { Object big = readFiltered(toBytes(new int[5000]), limits); show("big", big); } catch (Throwable _t) { System.out.println("big -> " + _t); }
        }
    }

    static void show(String name, Object value) {
        String text = value instanceof int[] a ? Arrays.toString(a)
        : value instanceof long[] a ? Arrays.toString(a)
        : value instanceof double[] a ? Arrays.toString(a)
        : value instanceof Object[] a ? Arrays.deepToString(a)
        : value instanceof String str ? "\"" + str + "\""
        : String.valueOf(value);
        System.out.println(name + " = " + text);
    }
}
