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
import org.apache.commons.lang3.SerializationUtils;

/**
 * Examples for the tutorial "Java Deep Copy Using In-Memory Serialization (With Examples)".
 * https://howtodoinjava.com/java/serialization/how-to-do-deep-cloning-using-in-memory-serialization-in-java/
 */
public class DeepCopyInMemorySerialization {
    @SuppressWarnings("unchecked")
    static <T extends Serializable> T deepCopy(T original) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(buffer)) {
            out.writeObject(original);
        }
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()))) {
            return (T) in.readObject();
        }
    }
    static class Ingredient implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        private final String name;
        private int grams;

        Ingredient(String name, int grams) {
            this.name = name;
            this.grams = grams;
        }

        String getName() { return name; }
        int getGrams() { return grams; }
        void setGrams(int grams) { this.grams = grams; }
    }
    static class Recipe implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        private final String title;
        private final List<Ingredient> ingredients = new ArrayList<>();

        Recipe(String title, Ingredient... items) {
            this.title = title;
            this.ingredients.addAll(List.of(items));
        }

        String title() { return title; }
        List<Ingredient> ingredients() { return ingredients; }
    }
    static class MealPlan implements Serializable {
        Recipe monday;
        Recipe friday;
    }
    static class CachedRecipe implements Serializable {
        String title = "Soup";
        transient String nutritionCache = "320 kcal";
    }

    static class TimedRecipe implements Serializable {
        String title = "Bread";
        Thread timer = new Thread(() -> { });
    }
    static class Units implements Serializable {
        static final Units INSTANCE = new Units();

        private Units() { }

        @Serial
        private Object readResolve() {
            return INSTANCE;
        }
    }
    static class Spice {
        String name;
        int grams;

        Spice(String name, int grams) {
            this.name = name;
            this.grams = grams;
        }

        Spice(Spice other) {
            this(other.name, other.grams);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Recipe pancakes = new Recipe("Pancakes", new Ingredient("flour", 200), new Ingredient("milk", 300));
            Recipe copy = deepCopy(pancakes);
            copy.ingredients().get(0).setGrams(250);

            int originalFlour = pancakes.ingredients().get(0).getGrams();   // 200
            show("originalFlour", originalFlour);
            int copiedFlour = copy.ingredients().get(0).getGrams();         // 250
            show("copiedFlour", copiedFlour);
            boolean sameList = copy.ingredients() == pancakes.ingredients(); // false
            show("sameList", sameList);
        }
        {
            Recipe working = new Recipe("Pancakes", new Ingredient("flour", 200), new Ingredient("milk", 300));
            Recipe snapshot = deepCopy(working);

            working.ingredients().get(0).setGrams(250);
            working.ingredients().add(new Ingredient("sugar", 20));

            int snapshotFlour = snapshot.ingredients().get(0).getGrams();   // 200
            show("snapshotFlour", snapshotFlour);
            int snapshotSize = snapshot.ingredients().size();               // 2
            show("snapshotSize", snapshotSize);
            int workingSize = working.ingredients().size();                 // 3
            show("workingSize", workingSize);
        }
        {
            MealPlan plan = new MealPlan();
            plan.monday = new Recipe("Pasta", new Ingredient("tomato", 400));
            plan.friday = plan.monday;

            MealPlan planCopy = deepCopy(plan);
            boolean stillShared = planCopy.monday == planCopy.friday;     // true
            show("stillShared", stillShared);
            boolean newObject = planCopy.monday != plan.monday;           // true
            show("newObject", newObject);
        }
        {
            CachedRecipe cachedCopy = deepCopy(new CachedRecipe());
            String cache = cachedCopy.nutritionCache;                     // null, transient
            show("cache", cache);

            try { TimedRecipe timedCopy = deepCopy(new TimedRecipe()); show("timedCopy", timedCopy); } catch (Throwable _t) { System.out.println("timedCopy -> " + _t); }
        }
        {
            Units unitsCopy = deepCopy(Units.INSTANCE);
            boolean sameUnits = unitsCopy == Units.INSTANCE;             // true
            show("sameUnits", sameUnits);
            DayOfWeek day = deepCopy(DayOfWeek.MONDAY);
            boolean sameDay = day == DayOfWeek.MONDAY;                    // true
            show("sameDay", sameDay);
        }
        {
            Recipe soup = new Recipe("Soup", new Ingredient("carrot", 150));
            Recipe soupCopy = SerializationUtils.clone(soup);
            soupCopy.ingredients().get(0).setGrams(180);
            int soupCarrot = soup.ingredients().get(0).getGrams();        // 150
            show("soupCarrot", soupCarrot);
        }
        {
            List<Spice> rack = new ArrayList<>(List.of(new Spice("salt", 5), new Spice("pepper", 2)));
            List<Spice> rackCopy = rack.stream().map(Spice::new).collect(Collectors.toCollection(ArrayList::new));
            rackCopy.get(0).grams = 8;
            int originalSalt = rack.get(0).grams;                           // 5
            show("originalSalt", originalSalt);
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
