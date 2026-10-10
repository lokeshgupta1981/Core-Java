package com.howtodoinjava.core.collections.cursors;

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
 * Examples for the tutorial "Java ListIterator: Traverse and Modify Lists (with Examples)".
 * https://howtodoinjava.com/java/collections/java-listiterator/
 */
public class ListIteratorExamples {

    public static void main(String[] args) throws Exception {
        {
            List<String> steps = new ArrayList<>(List.of("chop", "fry", "serve"));
            ListIterator<String> it = steps.listIterator();
            String s1 = it.next();                        // "chop"
            show("s1", s1);
            int nextIdx = it.nextIndex();                 // 1
            show("nextIdx", nextIdx);
            it.add("wash");                               // inserts "wash" after "chop"
            String s2 = it.next();                        // "fry"
            show("s2", s2);
            it.set("bake");                               // replaces "fry" with "bake"
            String back = it.previous();                  // "bake"
            show("back", back);
            boolean hasPrev = it.hasPrevious();           // true
            show("hasPrev", hasPrev);
            String recipe = String.join(" > ", steps);    // "chop > wash > bake > serve"
            show("recipe", recipe);
        }
        {
            List<String> steps = new ArrayList<>(List.of("chop", "fry", "plate", "serve"));
            ListIterator<String> it = steps.listIterator();
            StringJoiner forward = new StringJoiner(" ");
            while (it.hasNext()) {
                forward.add(it.next());
            }
            StringJoiner backward = new StringJoiner(" ");
            while (it.hasPrevious()) {
                backward.add(it.previous());
            }
            String f = forward.toString();                // "chop fry plate serve"
            show("f", f);
            String b = backward.toString();               // "serve plate fry chop"
            show("b", b);
        }
        {
            List<String> steps = new ArrayList<>(List.of("chop", "fry", "plate", "serve"));
            ListIterator<String> fromSecond = steps.listIterator(1);
            String nextStep = fromSecond.next();          // "fry"
            show("nextStep", nextStep);
            ListIterator<String> fromEnd = steps.listIterator(steps.size());
            String lastStep = fromEnd.previous();         // "serve"
            show("lastStep", lastStep);
            try { ListIterator<String> bad = steps.listIterator(5); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
        }
        {
            ListIterator<String> pos = List.of("chop", "fry").listIterator();
            int beforeAny = pos.previousIndex();          // -1
            show("beforeAny", beforeAny);
            int upcoming = pos.nextIndex();               // 0
            show("upcoming", upcoming);
            String step = pos.next();                     // "chop"
            show("step", step);
            int afterStep = pos.nextIndex();              // 1
            show("afterStep", afterStep);
            String again = pos.previous();                // "chop"
            show("again", again);
        }
        {
            ListIterator<String> edit = new ArrayList<>(List.of("chop")).listIterator();
            try { edit.set("dice");  } catch (Throwable _t) { System.out.println("-> " + _t); }
            String current = edit.next();                 // "chop"
            show("current", current);
            edit.add("rinse");
            try { edit.set("dice");  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            List<String> shopping = new ArrayList<>(List.of("bread", "salad kit", "milk"));
            ListIterator<String> items = shopping.listIterator();
            while (items.hasNext()) {
                if (items.next().equals("salad kit")) {
                    items.set("lettuce");
                    items.add("tomato");
                    items.add("dressing");
                }
            }
            String sent = String.join(", ", shopping);    // "bread, lettuce, tomato, dressing, milk"
            show("sent", sent);
        }
        {
            List<String> fixedSize = Arrays.asList("chop", "fry");
            ListIterator<String> fs = fixedSize.listIterator();
            String firstFixed = fs.next();                // "chop"
            show("firstFixed", firstFixed);
            fs.set("dice");                               // allowed, the size does not change
            try { fs.add("rinse");  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            List<String> menu = new ArrayList<>(List.of("soup", "salad", "cake"));
            ListIterator<String> menuIt = menu.listIterator();
            String course1 = menuIt.next();               // "soup"
            show("course1", course1);
            boolean added = menu.add("tea");              // true
            show("added", added);
            try { String course2 = menuIt.next(); show("course2", course2); } catch (Throwable _t) { System.out.println("course2 -> " + _t); }
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
