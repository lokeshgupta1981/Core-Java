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
 * Examples for the tutorial "Java Iterator: Methods, Fail-Fast Behavior and Examples".
 * https://howtodoinjava.com/java/collections/java-iterator/
 */
public class IteratorExamples {
    static record Task(String name, boolean done) {}
    static record Countdown(int from) implements Iterable<Integer> {
        @Override
        public Iterator<Integer> iterator() {
            return new Iterator<>() {
                private int current = from;

                @Override
                public boolean hasNext() {
                    return current > 0;
                }

                @Override
                public Integer next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    return current--;
                }
            };
        }
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> tasks = new ArrayList<>(List.of("email", "report", "call"));
            Iterator<String> it = tasks.iterator();
            boolean more = it.hasNext();                  // true
            show("more", more);
            String first = it.next();                     // "email"
            show("first", first);
            it.remove();                                  // removes "email" from tasks
            String second = it.next();                    // "report"
            show("second", second);
            List<String> rest = new ArrayList<>();
            it.forEachRemaining(rest::add);               // rest = [call]
            int left = tasks.size();                      // 2
            show("left", left);
        }
        {
            Iterator<String> cursor = List.of("email").iterator();
            String only = cursor.next();                  // "email"
            show("only", only);
            boolean hasMore = cursor.hasNext();           // false
            show("hasMore", hasMore);
            try { String extra = cursor.next(); show("extra", extra); } catch (Throwable _t) { System.out.println("extra -> " + _t); }
        }
        {
            List<String> todo = new ArrayList<>(List.of("email", "report"));
            Iterator<String> walker = todo.iterator();
            try { walker.remove();  } catch (Throwable _t) { System.out.println("-> " + _t); }
            String done = walker.next();                  // "email"
            show("done", done);
            walker.remove();                              // removes "email"
            try { walker.remove();  } catch (Throwable _t) { System.out.println("-> " + _t); }
            Iterator<String> fixed = List.of("email").iterator();
            String read = fixed.next();                   // "email"
            show("read", read);
            try { fixed.remove();  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            List<Task> board = new ArrayList<>(List.of(new Task("email", true), new Task("report", false), new Task("call", true)));
            List<String> auditLog = new ArrayList<>();
            Iterator<Task> tasksIt = board.iterator();
            while (tasksIt.hasNext()) {
                Task task = tasksIt.next();
                if (task.done()) {
                    auditLog.add("removed " + task.name());
                    tasksIt.remove();
                }
            }
            int open = board.size();                      // 1
            show("open", open);
            String log = String.join(", ", auditLog);     // "removed email, removed call"
            show("log", log);
        }
        {
            Iterator<String> lines = List.of("name,qty", "apple,5", "kiwi,2").iterator();
            String header = lines.next();                 // "name,qty"
            show("header", header);
            List<String> rows = new ArrayList<>();
            lines.forEachRemaining(rows::add);
            int rowCount = rows.size();                   // 2
            show("rowCount", rowCount);
        }
        {
            Set<String> labels = new TreeSet<>(Set.of("urgent", "home", "work"));
            Iterator<String> labelIt = labels.iterator();
            String firstLabel = labelIt.next();           // "home"
            show("firstLabel", firstLabel);
        }
        {
            Map<String, Integer> minutes = new LinkedHashMap<>();
            minutes.put("email", 10);
            minutes.put("report", 90);
            minutes.put("call", 5);
            Iterator<Map.Entry<String, Integer>> entryIt = minutes.entrySet().iterator();
            while (entryIt.hasNext()) {
                Map.Entry<String, Integer> entry = entryIt.next();
                if (entry.getValue() < 15) {
                    entryIt.remove();
                }
            }
            Set<String> longTasks = minutes.keySet();     // [report]
            show("longTasks", longTasks);
        }
        {
            List<String> work = new ArrayList<>(List.of("email", "report", "call", "lunch"));
            Iterator<String> workIt = work.iterator();
            String step1 = workIt.next();                 // "email"
            show("step1", step1);
            boolean added = work.add("review");           // true, modCount changes
            show("added", added);
            try { String step2 = workIt.next(); show("step2", step2); } catch (Throwable _t) { System.out.println("step2 -> " + _t); }
        }
        {
            List<String> three = new ArrayList<>(List.of("email", "report", "call"));
            for (String t : three) if (t.equals("report")) three.remove(t);
            String quirk = String.join(",", three);       // "email,call", no exception and "call" never visited
            show("quirk", quirk);
        }
        {
            List<String> listeners = new CopyOnWriteArrayList<>(List.of("audit", "mail"));
            for (String name : listeners) {
                listeners.add(name + "-backup");          // the loop keeps reading the old snapshot
            }
            int listenerCount = listeners.size();         // 4
            show("listenerCount", listenerCount);
        }
        {
            List<Integer> seen = new ArrayList<>();
            for (int n : new Countdown(3)) {
                seen.add(n);
            }
            List<Integer> order = List.copyOf(seen);      // [3, 2, 1]
            show("order", order);
            Iterator<Integer> once = new Countdown(1).iterator();
            Integer last = once.next();                   // 1
            show("last", last);
            try { Integer none = once.next(); show("none", none); } catch (Throwable _t) { System.out.println("none -> " + _t); }
        }
        {
            Vector<String> legacy = new Vector<>(List.of("a", "b", "c"));
            Enumeration<String> en = legacy.elements();
            String e1 = en.nextElement();                 // "a"
            show("e1", e1);
            boolean removedA = legacy.remove("a");        // true
            show("removedA", removedA);
            String e2 = en.nextElement();                 // "c", and "b" is skipped without an exception
            show("e2", e2);
        }
        {
            Vector<String> old = new Vector<>(List.of("b", "c"));
            Iterator<String> modern = old.elements().asIterator();
            String m1 = modern.next();                    // "b"
            show("m1", m1);
            List<String> copy = Collections.list(old.elements());   // [b, c]
            show("copy", copy);
            Enumeration<String> forOldApi = Collections.enumeration(List.of("x"));
            boolean hasX = forOldApi.hasMoreElements();   // true
            show("hasX", hasX);
        }
        {
            Iterator<String> source = List.of("email", "report").iterator();
            Stream<String> stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(source, Spliterator.ORDERED), false);
            List<String> upper = stream.map(String::toUpperCase).toList();   // [EMAIL, REPORT]
            show("upper", upper);
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
