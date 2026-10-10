package com.howtodoinjava.core.streams;

import java.util.regex.*;
import java.net.*;
import java.text.*;
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
 * Examples for the tutorial "Remove or Update List Elements Using Streams in Java".
 * https://howtodoinjava.com/java/stream/remove-update-stream-elements/
 */
public class RemoveUpdateListElements {
    static record Task(String title, boolean done) {
        Task markDone() {
            return new Task(title, true);
        }
    }
    static class Reminder {
        private final String text;
        private int snoozes;

        Reminder(String text) { this.text = text; }

        void snooze() { snoozes++; }

        int snoozes() { return snoozes; }

        Reminder snoozedCopy() {
            Reminder copy = new Reminder(text);
            copy.snoozes = snoozes + 1;
            return copy;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> todos = List.of("pay rent", "call mom", "fix bike");
            List<String> withoutRent = todos.stream().filter(t -> !t.startsWith("pay")).toList();   // [call mom, fix bike]
            show("withoutRent", withoutRent);
            List<String> upper = todos.stream().map(String::toUpperCase).toList();                   // [PAY RENT, CALL MOM, FIX BIKE]
            show("upper", upper);

            List<String> editable = new ArrayList<>(todos);
            boolean removed = editable.removeIf(t -> t.startsWith("pay"));                          // true, editable = [call mom, fix bike]
            show("removed", removed);
            editable.replaceAll(String::toUpperCase);
            List<String> afterEdit = editable;                                                      // [CALL MOM, FIX BIKE]
            show("afterEdit", afterEdit);
        }
        {
            List<String> todos = List.of("pay rent", "call mom", "fix bike");
            List<String> keep = todos.stream().filter(t -> t.contains("o")).toList();                   // [call mom]
            show("keep", keep);
            List<String> dropped = todos.stream().filter(Predicate.not(t -> t.contains("o"))).toList();   // [pay rent, fix bike]
            show("dropped", dropped);
            List<String> original = todos;                                                             // [pay rent, call mom, fix bike]
            show("original", original);
        }
        {
            List<String> todos = List.of("pay rent", "call mom", "fix bike");
            List<String> fixed = todos.stream().filter(t -> t.length() > 7).toList();
            try { boolean added = fixed.add("water plants"); show("added", added); } catch (Throwable _t) { System.out.println("added -> " + _t); }
            ArrayList<String> growable = todos.stream().filter(t -> t.length() > 7).collect(Collectors.toCollection(ArrayList::new));
            boolean grown = growable.add("water plants");                                                    // true
            show("grown", grown);
        }
        {
            List<String> todos = List.of("pay rent", "call mom", "fix bike");
            List<String> chores = new ArrayList<>(todos);
            boolean any = chores.removeIf(t -> t.startsWith("call"));     // true, chores = [pay rent, fix bike]
            show("any", any);
            boolean none = chores.removeIf(t -> t.startsWith("walk"));    // false
            show("none", none);
            try { boolean immutable = todos.removeIf(t -> t.startsWith("pay")); show("immutable", immutable); } catch (Throwable _t) { System.out.println("immutable -> " + _t); }
        }
        {
            List<String> busy = new ArrayList<>(List.of("pay rent", "call mom", "fix bike"));
            try { busy.stream().forEach(t -> { if (t.startsWith("fix")) busy.remove(t); });  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            List<String> todos = List.of("pay rent", "call mom", "fix bike");
            List<String> labels = todos.stream().map(t -> "[ ] " + t).toList();                      // [[ ] pay rent, [ ] call mom, [ ] fix bike]
            show("labels", labels);
            List<String> oneChanged = todos.stream().map(t -> t.equals("call mom") ? "call dad" : t).toList();   // [pay rent, call dad, fix bike]
            show("oneChanged", oneChanged);
        }
        {
            List<Task> tasks = List.of(new Task("pay rent", true), new Task("call mom", false), new Task("fix bike", false));
            List<Task> updated = tasks.stream().map(t -> t.title().equals("call mom") ? t.markDone() : t).toList();   // [Task[title=pay rent, done=true], Task[title=call mom, done=true], Task[title=fix bike, done=false]]
            show("updated", updated);
            Task stillOpen = tasks.get(1);                                                                              // Task[title=call mom, done=false]
            show("stillOpen", stillOpen);
        }
        {
            List<Task> tasks = List.of(new Task("pay rent", true), new Task("call mom", false), new Task("fix bike", false));
            List<Task> board = new ArrayList<>(tasks);
            board.replaceAll(t -> t.title().startsWith("fix") ? t.markDone() : t);
            Task bike = board.get(2);                                                       // Task[title=fix bike, done=true]
            show("bike", bike);
        }
        {
            List<Reminder> reminders = List.of(new Reminder("stretch"), new Reminder("drink water"));
            List<Reminder> snoozed = reminders.stream().map(r -> { r.snooze(); return r; }).toList();
            int originalSnoozes = reminders.get(0).snoozes();                                         // 1, the original changed too
            show("originalSnoozes", originalSnoozes);
        }
        {
            List<Reminder> reminders = List.of(new Reminder("stretch"), new Reminder("drink water"));
            List<Reminder> copies = reminders.stream().map(Reminder::snoozedCopy).toList();
            int copySnoozes = copies.get(0).snoozes();                                            // 1
            show("copySnoozes", copySnoozes);
            int untouched = reminders.get(0).snoozes();                                           // 0
            show("untouched", untouched);
            reminders.forEach(Reminder::snooze);
            int afterForEach = reminders.get(0).snoozes();                                        // 1, changed on purpose
            show("afterForEach", afterForEach);
        }
        {
            List<Task> session = List.of(new Task(" pay rent", true), new Task("call mom ", false), new Task("fix bike", false));
            List<Task> remaining = session.stream().filter(t -> !t.done()).map(t -> new Task(t.title().strip(), t.done())).toList();   // [Task[title=call mom, done=false], Task[title=fix bike, done=false]]
            show("remaining", remaining);
            Map<Boolean, List<Task>> byDone = session.stream().collect(Collectors.partitioningBy(Task::done));
            int archived = byDone.get(true).size();                                                                                      // 1
            show("archived", archived);
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
