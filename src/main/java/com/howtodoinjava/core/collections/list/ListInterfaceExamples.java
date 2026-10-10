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
 * Examples for the tutorial "Java List Interface: Methods, Implementations and Examples".
 * https://howtodoinjava.com/java/collections/arraylist/java-list/
 */
public class ListInterfaceExamples {
    static record Task(String title, boolean done) {}
    static List<Task> openTasks(List<Task> all) {
        return all.stream().filter(t -> !t.done()).toList();
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> fixed = List.of("plan", "code", "test");                  // unmodifiable
            show("fixed", fixed);
            List<String> tasks = new ArrayList<>(fixed);                            // mutable copy
            show("tasks", tasks);
            tasks.add(1, "review");                                                 // [plan, review, code, test]
            String atTwo = tasks.get(2);                                            // "code"
            show("atTwo", atTwo);
            String replaced = tasks.set(0, "design");                               // "plan"
            show("replaced", replaced);
            int where = tasks.indexOf("test");                                      // 3
            show("where", where);
            List<String> middle = tasks.subList(1, 3);                              // [review, code]
            show("middle", middle);
            String last = tasks.getLast();                                          // "test" (Java 21)
            show("last", last);
            List<String> backwards = tasks.reversed();                              // [test, code, review, design]
            show("backwards", backwards);
        }
        {
            List<String> a = new ArrayList<>(List.of("plan", "code"));
            List<String> b = new LinkedList<>(List.of("plan", "code"));
            boolean sameOrder = a.equals(b);                                        // true, class does not matter
            show("sameOrder", sameOrder);
            boolean otherOrder = a.equals(List.of("code", "plan"));                 // false
            show("otherOrder", otherOrder);
        }
        {
            List<String> steps = List.of("plan", "code");
            List<String> copy = List.copyOf(steps);                                 // [plan, code], unmodifiable
            show("copy", copy);
            List<String> editable = new ArrayList<>(steps);                         // [plan, code], mutable
            show("editable", editable);
            try { boolean added = steps.add("ship"); show("added", added); } catch (Throwable _t) { System.out.println("added -> " + _t); }
            try { List<String> withNull = List.of("plan", null); show("withNull", withNull); } catch (Throwable _t) { System.out.println("withNull -> " + _t); }
        }
        {
            List<String> source = new ArrayList<>(List.of("plan"));
            List<String> view = Collections.unmodifiableList(source);
            List<String> snapshot = List.copyOf(source);
            source.add("code");
            int viewSize = view.size();                                             // 2, view follows the source
            show("viewSize", viewSize);
            int snapshotSize = snapshot.size();                                     // 1, copy does not
            show("snapshotSize", snapshotSize);
        }
        {
            List<String> board = new ArrayList<>(List.of("plan", "code", "test", "code"));
            String second = board.get(1);                                           // "code"
            show("second", second);
            int firstCode = board.indexOf("code");                                  // 1
            show("firstCode", firstCode);
            int lastCode = board.lastIndexOf("code");                               // 3
            show("lastCode", lastCode);
            int noShip = board.indexOf("ship");                                     // -1
            show("noShip", noShip);
            try { String outside = board.get(4); show("outside", outside); } catch (Throwable _t) { System.out.println("outside -> " + _t); }
        }
        {
            List<String> backlog = new ArrayList<>(List.of("fix login", "Docs", "add search", "Release"));
            boolean removed = backlog.removeIf(t -> t.startsWith("D"));             // true, [fix login, add search, Release]
            show("removed", removed);
            backlog.replaceAll(String::toLowerCase);                                // [fix login, add search, release]
            backlog.sort(Comparator.naturalOrder());                                // [add search, fix login, release]
            String sorted = backlog.toString();                                     // "[add search, fix login, release]"
            show("sorted", sorted);
        }
        {
            List<Integer> estimates = new ArrayList<>(List.of(8, 5, 2, 13));
            Integer byIndex = estimates.remove(2);                                  // 2, removed index 2
            show("byIndex", byIndex);
            boolean byValue = estimates.remove(Integer.valueOf(13));                // true, removed the value 13
            show("byValue", byValue);
            String rest = estimates.toString();                                     // "[8, 5]"
            show("rest", rest);
        }
        {
            List<String> sprint = new ArrayList<>(List.of("plan", "code", "test"));
            sprint.addFirst("kickoff");                                             // [kickoff, plan, code, test]
            String done = sprint.removeLast();                                      // "test"
            show("done", done);
            String next = sprint.getFirst();                                        // "kickoff"
            show("next", next);
            List<String> undo = sprint.reversed();                                  // [code, plan, kickoff], a view
            show("undo", undo);
            try { String empty = new ArrayList<String>().getLast(); show("empty", empty); } catch (Throwable _t) { System.out.println("empty -> " + _t); }
        }
        {
            List<String> chores = new ArrayList<>(List.of("dishes", "laundry", "trash"));
            ListIterator<String> cursor = chores.listIterator(chores.size());
            StringBuilder reverse = new StringBuilder();
            while (cursor.hasPrevious()) {
                reverse.append(cursor.previous()).append(' ');
            }
            String order = reverse.toString().strip();                              // "trash laundry dishes"
            show("order", order);
        }
        {
            List<Task> project = List.of(new Task("plan", true), new Task("code", false), new Task("test", false));
            List<Task> open = openTasks(project);
            int openCount = open.size();                                            // 2
            show("openCount", openCount);
            String firstOpen = open.getFirst().title();                             // "code"
            show("firstOpen", firstOpen);
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
