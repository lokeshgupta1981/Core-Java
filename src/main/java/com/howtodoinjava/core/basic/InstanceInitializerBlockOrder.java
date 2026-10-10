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

/**
 * Examples for the tutorial "Java Instance Initializer Block: Order, Rules and Examples".
 * https://howtodoinjava.com/java/oops/java-instance-initializer/
 */
public class InstanceInitializerBlockOrder {
    static class Recipe {
        final List<String> steps = new ArrayList<>();
        final String title;

        {
            steps.add("wash hands");            // instance initializer block
        }

        Recipe() { this.title = "untitled"; }

        Recipe(String title) { this.title = title; }
    }
    static class Dish {
        final List<String> log = new ArrayList<>();

        {
            log.add("Dish block");
        }

        Dish() {
            log.add("Dish()");
        }
    }
    static class Dessert extends Dish {
        String topping = note("topping field");

        {
            log.add("Dessert block 1");
        }

        Dessert() {
            log.add("Dessert()");
        }

        {
            log.add("Dessert block 2");
        }

        String note(String step) {
            log.add(step);
            return step;
        }
    }
    static class Batch {
        int blockRuns;
        final int servings;

        {
            blockRuns++;
        }

        Batch() {
            this(4);
        }

        Batch(int servings) {
            this.servings = servings;
        }
    }
    static class ImportedDish {
        final String title;
        final Instant createdAt;
        final List<String> ingredients;

        ImportedDish(String title, List<String> ingredients) {
            this.title = title;
            this.createdAt = Instant.now();
            this.ingredients = new ArrayList<>(ingredients);
        }

        ImportedDish(String title) {
            this(title, List.of());
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Recipe pasta = new Recipe("Pasta");
            List<String> pastaSteps = pasta.steps;      // [wash hands]
            show("pastaSteps", pastaSteps);
            Recipe blank = new Recipe();
            String blankTitle = blank.title;            // "untitled"
            show("blankTitle", blankTitle);
            List<String> blankSteps = blank.steps;      // [wash hands]
            show("blankSteps", blankSteps);
        }
        {
            List<String> order = new Dessert().log;   // [Dish block, Dish(), topping field, Dessert block 1, Dessert block 2, Dessert()]
            show("order", order);
        }
        {
            Batch defaultBatch = new Batch();
            int runs = defaultBatch.blockRuns;          // 1
            show("runs", runs);
            int servings = defaultBatch.servings;       // 4
            show("servings", servings);
        }
        {
            Recipe toast = new Recipe("Toast") {
                {
                    steps.add("toast the bread");
                }
            };
            List<String> toastSteps = toast.steps;      // [wash hands, toast the bread]
            show("toastSteps", toastSteps);
        }
        {
            Map<String, Integer> spices = new HashMap<>() {{
                    put("salt", 1);
                    put("pepper", 2);
            }};
            boolean extraClass = spices.getClass() != HashMap.class;   // true
            show("extraClass", extraClass);
        }
        {
            Map<String, Integer> fixedSpices = Map.of("salt", 1);     // {salt=1}
            show("fixedSpices", fixedSpices);
            Map<String, Integer> editable = new HashMap<>(Map.of("salt", 1));
            editable.put("pepper", 2);
            int spiceCount = editable.size();                         // 2
            show("spiceCount", spiceCount);
        }
        {
            ImportedDish soup = new ImportedDish("Soup");
            int ingredientCount = soup.ingredients.size();    // 0
            show("ingredientCount", ingredientCount);
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
