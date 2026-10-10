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
 * Examples for the tutorial "Iterate ArrayList in Java: for, forEach(), Iterator and Streams".
 * https://howtodoinjava.com/java/collections/arraylist/iterate-through-arraylist/
 */
public class IterateArrayList {
    static void dropStep(List<String> steps, String name) {
        for (String step : steps) {
            if (step.equals(name)) {
                steps.remove(step);         // changes the list inside the loop
            }
        }
    }
    static record Step(String text, boolean optional) {}
    public static void main(String[] args) throws Exception {
        {
            List<String> steps = new ArrayList<>(List.of("chop", "fry", "plate"));

            for (String step : steps) {                        // chop, fry, plate
                System.out.println(step);
            }
            for (int i = 0; i < steps.size(); i++) {           // 1. chop, 2. fry, 3. plate
                System.out.println((i + 1) + ". " + steps.get(i));
            }
            steps.forEach(System.out::println);                // chop, fry, plate
            steps.reversed().forEach(System.out::println);     // plate, fry, chop (Java 21)
            steps.removeIf(step -> step.startsWith("f"));      // removes "fry" safely
            List<String> remaining = steps;                    // [chop, plate]
            show("remaining", remaining);
        }
        {
            List<String> steps = List.of("chop", "fry", "plate");
            StringBuilder card = new StringBuilder();
            for (String step : steps) {
                card.append(step).append(" > ");
            }
            String recipe = card.toString();                   // "chop > fry > plate > "
            show("recipe", recipe);
        }
        {
            List<String> steps = new ArrayList<>(List.of("chop", "fry", "plate"));
            for (int i = 0; i < steps.size(); i++) {
                steps.set(i, (i + 1) + ". " + steps.get(i));
            }
            List<String> numbered = steps;                     // [1. chop, 2. fry, 3. plate]
            show("numbered", numbered);
        }
        {
            List<String> steps = List.of("chop", "fry", "plate");
            int index = 0;
            int letters = 0;
            while (index < steps.size()) {
                letters += steps.get(index++).length();
            }
            int totalLetters = letters;                        // 12
            show("totalLetters", totalLetters);
        }
        {
            List<String> steps = List.of("Chop", "Fry", "Plate");
            List<String> lower = new ArrayList<>();
            steps.forEach(step -> lower.add(step.toLowerCase()));
            List<String> result = lower;                       // [chop, fry, plate]
            show("result", result);
        }
        {
            List<String> steps = List.of("chop", "fry", "plate");
            List<String> log = new ArrayList<>();
            Consumer<String> logStep = step -> log.add("step: " + step);
            steps.forEach(logStep);
            steps.forEach(step -> {
                String upper = step.toUpperCase();
                log.add(upper);
            });
            int entries = log.size();                          // 6
            show("entries", entries);
        }
        {
            List<String> steps = new ArrayList<>(List.of("chop", "fry", "plate"));
            try { steps.forEach(step -> steps.add("wash"));  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            List<String> steps = new ArrayList<>(List.of("chop", "fry", "plate"));
            Iterator<String> it = steps.iterator();
            while (it.hasNext()) {
                if (it.next().equals("fry")) {
                    it.remove();
                }
            }
            List<String> left = steps;                         // [chop, plate]
            show("left", left);
        }
        {
            List<String> steps = new ArrayList<>(List.of("chop", "fry", "plate"));
            ListIterator<String> li = steps.listIterator(steps.size());
            StringBuilder backwards = new StringBuilder();
            while (li.hasPrevious()) {
                int pos = li.previousIndex();
                String step = li.previous();
                backwards.append(pos).append('=').append(step).append(' ');
                li.set(step.toUpperCase());
            }
            String visited = backwards.toString().strip();     // "2=plate 1=fry 0=chop"
            show("visited", visited);
            List<String> edited = steps;                       // [CHOP, FRY, PLATE]
            show("edited", edited);
        }
        {
            List<String> steps = new ArrayList<>(List.of("chop", "fry", "plate"));
            try { dropStep(steps, "chop");  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            List<String> steps = new ArrayList<>(List.of("chop", "fry", "plate", "fry again"));
            boolean changed = steps.removeIf(step -> step.startsWith("fry"));   // true
            show("changed", changed);
            List<String> left = steps;                         // [chop, plate]
            show("left", left);
        }
        {
            List<String> steps = new ArrayList<>(List.of("chop", "fry", "plate"));
            List<String> undo = new ArrayList<>();
            for (String step : steps.reversed()) {
                undo.add("undo " + step);
            }
            List<String> undoOrder = undo;                     // [undo plate, undo fry, undo chop]
            show("undoOrder", undoOrder);
            String last = steps.getLast();                     // "plate"
            show("last", last);
        }
        {
            List<String> steps = List.of("chop", "fry", "plate");
            List<String> longSteps = steps.stream()
                    .filter(step -> step.length() > 3)
                    .map(String::toUpperCase)
                    .toList();                                 // [CHOP, PLATE]
            long count = steps.stream().filter(s -> s.contains("p")).count();   // 2
            show("count", count);
        }
        {
            List<String> steps = List.of("chop", "fry", "plate");
            List<String> lines = IntStream.range(0, steps.size())
                    .mapToObj(i -> (i + 1) + ". " + steps.get(i))
                    .toList();                                 // [1. chop, 2. fry, 3. plate]
        }
        {
            List<Step> recipe = new ArrayList<>(List.of(
            new Step("chop onions", false),
            new Step("fry onions", false),
            new Step("add parsley", true),
            new Step("plate", false)));

            List<String> page = new ArrayList<>();
            for (int i = 0; i < recipe.size(); i++) {
                page.add((i + 1) + ". " + recipe.get(i).text());
            }
            String firstLine = page.getFirst();                // "1. chop onions"
            show("firstLine", firstLine);

            recipe.removeIf(Step::optional);
            List<String> quickMode = recipe.stream().map(Step::text).toList();   // [chop onions, fry onions, plate]
            show("quickMode", quickMode);
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
