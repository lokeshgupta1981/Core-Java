package com.howtodoinjava.core.streams;

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
 * Examples for the tutorial "Java Method References: 4 Kinds with Lambda Equivalents".
 * https://howtodoinjava.com/java8/lambda-method-references-example/
 */
public class MethodReferenceOrLambda {
    static record Task(String title, int minutes) {

        Task(String title) {
            this(title, 15);                       // default estimate
        }

        boolean isLong() {
            return minutes > 30;
        }

        static int byMinutes(Task a, Task b) {
            return Integer.compare(a.minutes(), b.minutes());
        }
    }

    static List<Task> tasks = List.of(new Task("email", 10), new Task("report", 45), new Task("review", 30));
    static class BaseReport {
        String format(Task task) {
            return task.title();
        }
    }

    static class DetailedReport extends BaseReport {

        @Override
        String format(Task task) {
            return task.title() + " (" + task.minutes() + " min)";
        }

        List<String> detailed(List<Task> tasks) {
            return tasks.stream().map(this::format).toList();
        }

        List<String> shortForm(List<Task> tasks) {
            return tasks.stream().map(super::format).toList();
        }
    }
    public static void main(String[] args) throws Exception {
        {
            BinaryOperator<Integer> maxLambda = (a, b) -> Math.max(a, b);
            BinaryOperator<Integer> maxRef = Math::max;                     // 1. static method
            show("maxRef", maxRef);
            int bigger = maxRef.apply(3, 7);                                // bigger = 7
            show("bigger", bigger);

            String str = "hello";
            Supplier<Integer> lengthLambda = () -> str.length();
            Supplier<Integer> lengthRef = str::length;                      // 2. instance method of one object (bound)
            show("lengthRef", lengthRef);
            int len = lengthRef.get();                                      // len = 5
            show("len", len);

            Function<String, String> upperLambda = s -> s.toUpperCase();
            Function<String, String> upperRef = String::toUpperCase;        // 3. instance method of any object (unbound)
            show("upperRef", upperRef);
            String upper = upperRef.apply("hello");                         // upper = "HELLO"
            show("upper", upper);

            Supplier<List<String>> listLambda = () -> new ArrayList<>();
            Supplier<List<String>> listRef = ArrayList::new;                // 4. constructor
            show("listRef", listRef);
            List<String> empty = listRef.get();                             // empty = []
            show("empty", empty);
        }
        {
            List<String> names = List.of("lokesh", "alex");

            names.forEach(name -> System.out.println(name));   // lambda
            names.forEach(System.out::println);                // method reference, same output
        }
        {
            Function<String, String> toUpper = String::toUpperCase;   // OK, apply() calls toUpperCase()
            show("toUpper", toUpper);
            String shout = toUpper.apply("hello");                    // "HELLO"
            show("shout", shout);
        }
        {
            List<String> names = List.of("lokesh", "alex", "brian");

            List<String> initials = names.stream().map(n -> n.substring(0, 1).toUpperCase()).toList();    // [L, A, B], keep the lambda
            show("initials", initials);
            List<Boolean> startsWithA = names.stream().map(n -> n.startsWith("a")).toList();              // [false, true, false], keep the lambda
            show("startsWithA", startsWithA);
            List<String> nonEmpty = Stream.of("a", "", "b").filter(Predicate.not(String::isEmpty)).toList();  // [a, b]
            show("nonEmpty", nonEmpty);
            List<String> byLength = names.stream().sorted(Comparator.comparing(String::length)).toList();     // [alex, brian, lokesh]
            show("byLength", byLength);
        }
        {
            List<String> sorted = tasks.stream().sorted(Task::byMinutes).map(Task::title).toList();   // [email, review, report]
            show("sorted", sorted);

            int total = tasks.stream().map(Task::minutes).reduce(0, Integer::sum);   // 85
            show("total", total);

            Optional<Integer> max = Stream.of(1, 12, 433, 5).reduce(Math::max);      // Optional[433]
            show("max", max);
        }
        {
            tasks.stream().map(Task::title).forEach(System.out::println);
            // email
            // report
            // review

            String str = "hello";
            Supplier<Integer> length = str::length;   // the object is str
            show("length", length);
            int size = length.get();                  // size = 5
            show("size", size);
        }
        {
            List<String> titles = tasks.stream().map(Task::title).toList();       // [email, report, review]
            show("titles", titles);
            List<Task> longTasks = tasks.stream().filter(Task::isLong).toList();  // [Task[title=report, minutes=45]]
            show("longTasks", longTasks);

            BiFunction<String, String, Boolean> same = String::equalsIgnoreCase;   // (a, b) -> a.equalsIgnoreCase(b)
            show("same", same);
            boolean equal = same.apply("Java", "JAVA");                            // equal = true
            show("equal", equal);

            List<String> names = new ArrayList<>(List.of("brian", "Alex", "lokesh"));
            names.sort(String::compareToIgnoreCase);           // [Alex, brian, lokesh]
        }
        {
            Function<String, Task> oneArg = Task::new;                // new Task(title)
            show("oneArg", oneArg);
            BiFunction<String, Integer, Task> twoArgs = Task::new;    // new Task(title, minutes)
            show("twoArgs", twoArgs);

            Task email = oneArg.apply("email");             // Task[title=email, minutes=15]
            show("email", email);
            Task report = twoArgs.apply("report", 45);      // Task[title=report, minutes=45]
            show("report", report);

            List<Task> newTasks = List.of("email", "call").stream().map(Task::new).toList();
            // [Task[title=email, minutes=15], Task[title=call, minutes=15]]

            ArrayList<String> list = tasks.stream().map(Task::title)
                    .collect(Collectors.toCollection(ArrayList::new));    // [email, report, review]
        }
        {
            IntFunction<int[]> intArrayLambda = size -> new int[size];
            IntFunction<int[]> intArrayRef = int[]::new;
            int arrayLength = intArrayRef.apply(3).length;                           // arrayLength = 3
            show("arrayLength", arrayLength);

            String[] titles = Stream.of("email", "report").toArray(String[]::new);   // [email, report]
            show("titles", titles);
            String[] copy = List.of("a", "b").toArray(String[]::new);                // [a, b]
            show("copy", copy);
        }
        {
            DetailedReport report = new DetailedReport();
            List<String> forManagers = report.detailed(tasks);    // [email (10 min), report (45 min), review (30 min)]
            show("forManagers", forManagers);
            List<String> forEmail = report.shortForm(tasks);      // [email, report, review]
            show("forEmail", forEmail);
        }
        {
            Function<Integer, String> lambda = i -> i.toString();
            Function<Integer, String> valueOf = String::valueOf;

            String fromLambda = lambda.apply(42);     // "42"
            show("fromLambda", fromLambda);
            String fromValueOf = valueOf.apply(42);   // "42"
            show("fromValueOf", fromValueOf);
        }
        {
            String title = null;

            Supplier<Integer> lambda = () -> title.length();   // OK, no error yet
            show("lambda", lambda);
            try { int size = lambda.get(); show("size", size); } catch (Throwable _t) { System.out.println("size -> " + _t); }

            try { Supplier<Integer> ref = title::length; show("ref", ref); } catch (Throwable _t) { System.out.println("ref -> " + _t); }
        }
        {
            StringBuilder builder = new StringBuilder("draft");
            Supplier<String> ref = builder::toString;
            builder = new StringBuilder("final");   // a lambda () -> builder.toString() would not compile here

            String text = ref.get();                 // text = "draft"
            show("text", text);
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
