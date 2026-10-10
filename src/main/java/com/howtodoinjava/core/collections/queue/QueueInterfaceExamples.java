package com.howtodoinjava.core.collections.queue;

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
 * Examples for the tutorial "Java Queue Interface: Methods, Implementations and Examples".
 * https://howtodoinjava.com/java/collections/java-queue-interface/
 */
public class QueueInterfaceExamples {
    static record Folder(String name, List<Folder> children) {}
    static List<String> levelOrder(Folder root) {
        List<String> names = new ArrayList<>();
        Queue<Folder> pending = new ArrayDeque<>();
        pending.offer(root);
        while (!pending.isEmpty()) {
            Folder folder = pending.poll();
            names.add(folder.name());
            folder.children().forEach(pending::offer);
        }
        return names;
    }
    public static void main(String[] args) throws Exception {
        {
            Queue<String> emails = new ArrayDeque<>();
            boolean added = emails.add("welcome");          // true, added at the tail
            show("added", added);
            boolean offered = emails.offer("invoice");      // true
            show("offered", offered);
            String head = emails.peek();                    // "welcome" (not removed)
            show("head", head);
            String same = emails.element();                 // "welcome" (not removed)
            show("same", same);
            String first = emails.poll();                   // "welcome" (removed)
            show("first", first);
            String second = emails.remove();                // "invoice" (removed)
            show("second", second);
            String none = emails.poll();                    // null (empty queue)
            show("none", none);
            try { String fail = emails.remove(); show("fail", fail); } catch (Throwable _t) { System.out.println("fail -> " + _t); }
        }
        {
            Queue<String> printJobs = new ArrayBlockingQueue<>(1);
            boolean ok = printJobs.offer("report.pdf");     // true
            show("ok", ok);
            boolean full = printJobs.offer("memo.pdf");     // false (capacity 1)
            show("full", full);
            try { boolean crash = printJobs.add("memo.pdf"); show("crash", crash); } catch (Throwable _t) { System.out.println("crash -> " + _t); }
        }
        {
            Queue<String> strict = new ArrayDeque<>();
            try { boolean nullAdded = strict.offer(null); show("nullAdded", nullAdded); } catch (Throwable _t) { System.out.println("nullAdded -> " + _t); }
        }
        {
            Queue<Integer> fifo = new ArrayDeque<>(List.of(3, 1, 2));
            Integer fifoHead = fifo.poll();                 // 3 (first added)
            show("fifoHead", fifoHead);
            Queue<Integer> byValue = new PriorityQueue<>(List.of(3, 1, 2));
            Integer smallest = byValue.poll();              // 1 (smallest first)
            show("smallest", smallest);
        }
        {
            Deque<String> history = new ArrayDeque<>();
            history.offerLast("home");
            history.offerLast("cart");
            String newest = history.peekLast();             // "cart"
            show("newest", newest);
            String oldest = history.getFirst();             // "home"
            show("oldest", oldest);
            List<String> backwards = List.copyOf(history.reversed());   // [cart, home]
            show("backwards", backwards);
        }
        {
            Queue<String> outbox = new ArrayDeque<>(List.of("a@x.com", "b@x.com", "c@x.com"));
            long count = outbox.stream().filter(e -> e.startsWith("b")).count();   // 1 (nothing removed)
            show("count", count);
            boolean removedB = outbox.removeIf(e -> e.startsWith("b"));             // true
            show("removedB", removedB);

            List<String> sent = new ArrayList<>();
            String next;
            while ((next = outbox.poll()) != null) {
                sent.add(next);
            }
            List<String> sentOrder = sent;                  // [a@x.com, c@x.com]
            show("sentOrder", sentOrder);
            boolean done = outbox.isEmpty();                // true
            show("done", done);
        }
        {
            Folder src = new Folder("src", List.of(new Folder("main", List.of(new Folder("java", List.of()))),
            new Folder("test", List.of())));
            Folder project = new Folder("app", List.of(src, new Folder("docs", List.of())));
            List<String> order = levelOrder(project);       // [app, src, docs, main, test, java]
            show("order", order);
        }
        {
            Queue<String> logLines = new ConcurrentLinkedQueue<>();
            Thread web = Thread.ofVirtual().start(() -> logLines.offer("web started"));
            Thread db = Thread.ofVirtual().start(() -> logLines.offer("db connected"));
            web.join();
            db.join();
            int lines = logLines.size();                    // 2 (the order depends on thread timing)
            show("lines", lines);
        }
        {
            Queue<String> tasks = new ArrayDeque<>(List.of("build", "test", "deploy"));
            String firstTask = tasks.peek();                // "build"
            show("firstTask", firstTask);
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
