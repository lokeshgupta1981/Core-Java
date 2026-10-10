package com.howtodoinjava.core.basic;

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

/**
 * Examples for the tutorial "Immutable Class in Java: Rules, Examples and Records".
 * https://howtodoinjava.com/java/basics/how-to-make-a-java-class-immutable/
 */
public class ImmutableClassDefensiveCopy {
    static final class LeakyRecipe {
        private final String name;
        private final List<String> ingredients;

        LeakyRecipe(String name, List<String> ingredients) {
            this.name = name;
            this.ingredients = ingredients;
        }

        List<String> ingredients() {
            return ingredients;
        }
    }
    static final class Recipe {
        private final String name;
        private final List<String> ingredients;

        Recipe(String name, List<String> ingredients) {
            this.name = Objects.requireNonNull(name);
            this.ingredients = List.copyOf(ingredients);
        }

        String name() {
            return name;
        }

        List<String> ingredients() {
            return ingredients;
        }

        Recipe withIngredient(String ingredient) {
            List<String> more = new ArrayList<>(ingredients);
            more.add(ingredient);
            return new Recipe(name, more);
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Recipe other
                    && name.equals(other.name)
                    && ingredients.equals(other.ingredients);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, ingredients);
        }
    }
    static record Menu(String restaurant, List<String> dishes) {
        Menu {
            Objects.requireNonNull(restaurant);
            dishes = List.copyOf(dishes);
        }

        Menu withDish(String dish) {
            List<String> more = new ArrayList<>(dishes);
            more.add(dish);
            return new Menu(restaurant, more);
        }
    }
    static record PriceSettings(BigDecimal taxRate, int maxGuests) {}
    public static void main(String[] args) throws Exception {
        {
            List<String> items = new ArrayList<>(List.of("flour", "eggs"));
            Recipe pancakes = new Recipe("Pancakes", items);
            items.add("sugar");                                         // changes only the caller's list
            int count = pancakes.ingredients().size();                  // 2
            show("count", count);
            try { boolean added = pancakes.ingredients().add("salt"); show("added", added); } catch (Throwable _t) { System.out.println("added -> " + _t); }
            Recipe sweet = pancakes.withIngredient("sugar");
            int sweetCount = sweet.ingredients().size();                // 3
            show("sweetCount", sweetCount);
            int stillTwo = pancakes.ingredients().size();               // 2
            show("stillTwo", stillTwo);
        }
        {
            String city = "pune";
            String upper = city.toUpperCase();          // "PUNE"
            show("upper", upper);
            String original = city;                     // "pune"
            show("original", original);
        }
        {
            final List<String> tags = new ArrayList<>();
            tags.add("vegan");
            int tagCount = tags.size();                 // 1, final did not stop add()
            show("tagCount", tagCount);
        }
        {
            List<String> mine = new ArrayList<>(List.of("rice"));
            LeakyRecipe pulao = new LeakyRecipe("Pulao", mine);
            mine.add("peas");
            int leakIn = pulao.ingredients().size();          // 2, changed through the argument
            show("leakIn", leakIn);
            pulao.ingredients().clear();
            int leakOut = pulao.ingredients().size();         // 0, changed through the getter
            show("leakOut", leakOut);
        }
        {
            List<String> mine2 = new ArrayList<>(List.of("rice"));
            Recipe biryani = new Recipe("Biryani", mine2);
            mine2.add("peas");
            int safeIn = biryani.ingredients().size();                  // 1
            show("safeIn", safeIn);
            try { boolean cleared = biryani.ingredients().remove("rice"); show("cleared", cleared); } catch (Throwable _t) { System.out.println("cleared -> " + _t); }
            try { Recipe nullItem = new Recipe("Soup", Arrays.asList("salt", null)); show("nullItem", nullItem); } catch (Throwable _t) { System.out.println("nullItem -> " + _t); }
        }
        {
            List<String> source = new ArrayList<>(List.of("tea"));
            List<String> view = Collections.unmodifiableList(source);
            List<String> copy = List.copyOf(source);
            source.add("coffee");
            String viewNow = view.toString();                 // "[tea, coffee]"
            show("viewNow", viewNow);
            String copyNow = copy.toString();                 // "[tea]"
            show("copyNow", copyNow);
        }
        {
            List<StringBuilder> notes = List.of(new StringBuilder("boil"));
            notes.get(0).append(" water");
            String changedNote = notes.get(0).toString();     // "boil water"
            show("changedNote", changedNote);
        }
        {
            List<String> today = new ArrayList<>(List.of("dosa"));
            Menu menu = new Menu("Udupi", today);
            today.add("idli");
            String printed = menu.toString();                          // "Menu[restaurant=Udupi, dishes=[dosa]]"
            show("printed", printed);
            int dishCount = menu.withDish("vada").dishes().size();     // 2
            show("dishCount", dishCount);
        }
        {
            LocalDate due = LocalDate.of(2026, 1, 31);
            LocalDate next = due.plusMonths(1);                        // 2026-02-28
            show("next", next);
            BigDecimal price = new BigDecimal("9.99");
            BigDecimal withTax = price.add(new BigDecimal("0.50"));    // 10.49
            show("withTax", withTax);
            BigDecimal unchanged = price;                              // 9.99
            show("unchanged", unchanged);
        }
        {
            AtomicReference<PriceSettings> current = new AtomicReference<>(new PriceSettings(new BigDecimal("0.18"), 4));
            PriceSettings before = current.get();
            current.set(new PriceSettings(new BigDecimal("0.12"), 4));
            BigDecimal oldRate = before.taxRate();               // 0.18
            show("oldRate", oldRate);
            BigDecimal newRate = current.get().taxRate();        // 0.12
            show("newRate", newRate);
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
