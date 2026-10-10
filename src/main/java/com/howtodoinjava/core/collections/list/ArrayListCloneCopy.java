package com.howtodoinjava.core.collections.list;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

/**
 * Examples for the tutorial "Java ArrayList clone(): Shallow Copy vs Deep Copy".
 * https://howtodoinjava.com/java/collections/arraylist/arraylist-clone-deep-copy/
 */
public class ArrayListCloneCopy {
    static class Ingredient {
        private final String name;
        private int grams;

        Ingredient(String name, int grams) {
            this.name = name;
            this.grams = grams;
        }

        Ingredient(Ingredient other) {
            this(other.name, other.grams);
        }

        int getGrams() { return grams; }

        void setGrams(int grams) { this.grams = grams; }

        @Override
        public String toString() { return name + ":" + grams; }
    }
    static record Step(int number, String text) {}
    static class Meal {
        private final String title;
        private final List<Ingredient> items;

        Meal(String title, List<Ingredient> items) {
            this.title = title;
            this.items = new ArrayList<>(items);
        }

        Meal(Meal other) {
            this.title = other.title;
            this.items = other.items.stream().map(Ingredient::new).collect(Collectors.toCollection(ArrayList::new));
        }

        List<Ingredient> items() { return items; }

        @Override
        public String toString() { return title + items; }
    }
    static class Tray implements Cloneable {
        private int[] slots;

        Tray(int... slots) { this.slots = slots; }

        int slot(int i) { return slots[i]; }

        void fill(int i, int value) { slots[i] = value; }

        @Override
        public Tray clone() {
            try {
                Tray copy = (Tray) super.clone();
                copy.slots = slots.clone();
                return copy;
            } catch (CloneNotSupportedException e) {
                throw new AssertionError(e);
            }
        }
    }
    public static void main(String[] args) throws Exception {
        {
            ArrayList<Ingredient> recipe = new ArrayList<>(List.of(new Ingredient("flour", 200)));
            ArrayList<Ingredient> shallow = (ArrayList<Ingredient>) recipe.clone();
            List<Ingredient> deep = recipe.stream().map(Ingredient::new).toList();
            shallow.getFirst().setGrams(400);                  // recipe = [flour:400]
            deep.getFirst().setGrams(100);                     // recipe = [flour:400]
        }
        {
            ArrayList<Ingredient> recipe = new ArrayList<>(List.of(new Ingredient("flour", 200), new Ingredient("milk", 300)));
            ArrayList<Ingredient> copy = (ArrayList<Ingredient>) recipe.clone();
            copy.add(new Ingredient("salt", 5));               // recipe = [flour:200, milk:300]
            copy.get(1).setGrams(250);                         // recipe = [flour:200, milk:250]
            boolean sameObject = copy.get(0) == recipe.get(0); // true
            show("sameObject", sameObject);
        }
        {
            List<String> spices = new ArrayList<>(List.of("salt", "pepper"));
            List<String> editable = new ArrayList<>(spices);   // [salt, pepper]
            show("editable", editable);
            List<String> readOnly = List.copyOf(spices);       // [salt, pepper]
            show("readOnly", readOnly);
            try { boolean added = readOnly.add("cumin"); show("added", added); } catch (Throwable _t) { System.out.println("added -> " + _t); }
        }
        {
            List<String> source = List.of("salt", "sugar");
            List<String> empty = new ArrayList<>();
            try { Collections.copy(empty, source);  } catch (Throwable _t) { System.out.println("-> " + _t); }
            List<String> dest = new ArrayList<>(List.of("x", "y", "z"));
            Collections.copy(dest, source);                    // dest = [salt, sugar, z]
        }
        {
            List<Step> steps = new ArrayList<>(List.of(new Step(1, "mix"), new Step(2, "bake")));
            List<Step> draft = new ArrayList<>(steps);
            draft.set(1, new Step(2, "fry"));                  // steps = [Step[number=1, text=mix], Step[number=2, text=bake]]
        }
        {
            List<Ingredient> cached = List.of(new Ingredient("flour", 200), new Ingredient("milk", 300));
            List<Ingredient> scaled = cached.stream()
                    .map(Ingredient::new)
                    .collect(Collectors.toCollection(ArrayList::new));
            scaled.forEach(i -> i.setGrams(i.getGrams() * 2));  // scaled = [flour:400, milk:600]
            String original = cached.toString();               // "[flour:200, milk:300]"
            show("original", original);
        }
        {
            List<Meal> menu = List.of(new Meal("pancakes", List.of(new Ingredient("flour", 200))));
            List<Meal> menuCopy = menu.stream().map(Meal::new).toList();
            menuCopy.getFirst().items().getFirst().setGrams(50);
            String before = menu.toString();                   // "[pancakes[flour:200]]"
            show("before", before);
            String after = menuCopy.toString();                // "[pancakes[flour:50]]"
            show("after", after);
        }
        {
            List<Tray> trays = List.of(new Tray(1, 2));
            List<Tray> trayCopies = trays.stream().map(Tray::clone).toList();
            trayCopies.getFirst().fill(0, 9);
            int untouched = trays.getFirst().slot(0);          // 1
            show("untouched", untouched);
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
