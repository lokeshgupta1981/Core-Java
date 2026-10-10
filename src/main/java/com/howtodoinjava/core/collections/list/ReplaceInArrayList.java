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
 * Examples for the tutorial "Replace Element in ArrayList in Java: set() and replaceAll()".
 * https://howtodoinjava.com/java/collections/arraylist/replace-element-arraylist/
 */
public class ReplaceInArrayList {
    static boolean replaceFirst(List<String> list, String oldValue, String newValue) {
        int index = list.indexOf(oldValue);
        if (index < 0) {
            return false;
        }
        list.set(index, newValue);
        return true;
    }
    static record Task(int id, String title) {}
    public static void main(String[] args) throws Exception {
        {
            List<String> tasks = new ArrayList<>(List.of("email", "gym", "lunch", "call"));
            String old = tasks.set(1, "yoga");                                    // "gym", tasks = [email, yoga, lunch, call]
            show("old", old);
            int index = tasks.indexOf("lunch");                                   // 2
            show("index", index);
            String replaced = tasks.set(index, "dinner");                         // "lunch", tasks = [email, yoga, dinner, call]
            show("replaced", replaced);
            boolean changed = Collections.replaceAll(tasks, "call", "meeting");   // true, tasks = [email, yoga, dinner, meeting]
            show("changed", changed);
            tasks.replaceAll(String::toUpperCase);                                // [EMAIL, YOGA, DINNER, MEETING]
            try { String bad = tasks.set(9, "gym"); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
        }
        {
            List<String> tasks = new ArrayList<>(List.of("email", "gym", "lunch", "call"));
            String previous = tasks.set(3, "meeting");    // "call", tasks = [email, gym, lunch, meeting]
            show("previous", previous);
            int size = tasks.size();                      // 4
            show("size", size);
        }
        {
            List<String> tasks = new ArrayList<>(List.of("email", "gym", "lunch", "call"));
            try { String tooBig = tasks.set(9, "yoga"); show("tooBig", tooBig); } catch (Throwable _t) { System.out.println("tooBig -> " + _t); }
            try { String atSize = tasks.set(tasks.size(), "yoga"); show("atSize", atSize); } catch (Throwable _t) { System.out.println("atSize -> " + _t); }
            try { String negative = tasks.set(-1, "yoga"); show("negative", negative); } catch (Throwable _t) { System.out.println("negative -> " + _t); }
        }
        {
            List<String> tasks = new ArrayList<>(List.of("email", "gym", "lunch"));
            String old = tasks.set(1, "yoga");            // "gym", tasks = [email, yoga, lunch]
            show("old", old);
            tasks.add(1, "coffee");                       // [email, coffee, yoga, lunch]
        }
        {
            List<String> tasks = new ArrayList<>(List.of("email", "gym", "lunch", "call"));
            int index = tasks.indexOf("lunch");             // 2
            show("index", index);
            String old = tasks.set(index, "dinner");        // "lunch", tasks = [email, gym, dinner, call]
            show("old", old);
        }
        {
            List<String> tasks = new ArrayList<>(List.of("email", "gym", "dinner", "call"));
            String oldCall = tasks.set(tasks.indexOf("call"), "meeting");   // "call", tasks = [email, gym, dinner, meeting]
            show("oldCall", oldCall);
            try { String fails = tasks.set(tasks.indexOf("swim"), "yoga"); show("fails", fails); } catch (Throwable _t) { System.out.println("fails -> " + _t); }
        }
        {
            List<String> tasks = new ArrayList<>(List.of("email", "gym", "dinner"));
            boolean renamed = replaceFirst(tasks, "gym", "yoga");    // true, tasks = [email, yoga, dinner]
            show("renamed", renamed);
            boolean missing = replaceFirst(tasks, "swim", "yoga");   // false, tasks = [email, yoga, dinner]
            show("missing", missing);
        }
        {
            List<String> week = new ArrayList<>(List.of("gym", "email", "gym"));
            boolean changed = Collections.replaceAll(week, "gym", "yoga");    // true, week = [yoga, email, yoga]
            show("changed", changed);
            boolean none = Collections.replaceAll(week, "swim", "run");       // false, week = [yoga, email, yoga]
            show("none", none);
        }
        {
            List<String> week = new ArrayList<>(List.of("gym", "email", "gym"));
            week.replaceAll(String::toUpperCase);                         // [GYM, EMAIL, GYM]
            week.replaceAll(t -> t.equals("GYM") ? "YOGA" : t);           // [YOGA, EMAIL, YOGA]
        }
        {
            List<String> titles = new ArrayList<>(List.of("  buy milk ", "call MOM"));
            UnaryOperator<String> clean = t -> t.strip().toLowerCase();
            titles.replaceAll(clean);                                     // [buy milk, call mom]
        }
        {
            List<Integer> prices = new ArrayList<>(List.of(100, 250, 40));
            prices.replaceAll(p -> p * 110 / 100);                        // [110, 275, 44]
        }
        {
            List<String> week = List.of("gym", "email");
            List<String> upper = week.stream().map(String::toUpperCase).toList();   // [GYM, EMAIL]
            show("upper", upper);
        }
        {
            List<Task> tasks = new ArrayList<>(List.of(new Task(1, "email"), new Task(2, "gym")));
            int index = IntStream.range(0, tasks.size()).filter(i -> tasks.get(i).id() == 2).findFirst().orElse(-1);   // 1
            show("index", index);
            Task old = index >= 0 ? tasks.set(index, new Task(2, "yoga")) : null;                                    // Task[id=2, title=gym]
            show("old", old);
            String current = tasks.toString();                                                                        // "[Task[id=1, title=email], Task[id=2, title=yoga]]"
            show("current", current);
        }
        {
            List<Task> tasks = new ArrayList<>(List.of(new Task(1, "email"), new Task(2, "gym"), new Task(3, "gym")));
            tasks.replaceAll(t -> t.title().equals("gym") ? new Task(t.id(), "yoga") : t);
            long yoga = tasks.stream().filter(t -> t.title().equals("yoga")).count();      // 2
            show("yoga", yoga);
        }
        {
            List<String> week = new ArrayList<>(List.of("yoga", "email", "yoga"));
            ListIterator<String> it = week.listIterator();
            while (it.hasNext()) {
                if (it.next().equals("email")) {
                    it.set("calls");
                }
            }
            String result = week.toString();              // "[yoga, calls, yoga]"
            show("result", result);
        }
        {
            List<String> fixed = Arrays.asList("email", "gym");
            String old = fixed.set(1, "yoga");                  // "gym", fixed = [email, yoga]
            show("old", old);

            List<String> constant = List.of("email", "gym");
            try { String failed = constant.set(1, "yoga"); show("failed", failed); } catch (Throwable _t) { System.out.println("failed -> " + _t); }

            List<String> copy = new ArrayList<>(constant);
            String inCopy = copy.set(1, "yoga");                // "gym", copy = [email, yoga]
            show("inCopy", inCopy);
        }
        {
            List<String> tasks = new ArrayList<>(List.of("email", "gym", "gym"));
            for (int i = 0; i < tasks.size(); i++) {
                if (tasks.get(i).equals("gym")) {
                    tasks.set(i, "yoga");
                }
            }
            String result = tasks.toString();             // "[email, yoga, yoga]"
            show("result", result);
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
