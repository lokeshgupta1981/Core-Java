package com.howtodoinjava.core.basic;

import java.util.regex.*;
import java.net.*;
import java.text.*;
import java.nio.file.attribute.*;
import java.security.*;
import java.nio.charset.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.time.format.*;

/**
 * Examples for the tutorial "Lambda Expressions in Java: Syntax, Rules and Examples".
 * https://howtodoinjava.com/java8/lambda-expressions/
 */
public class LambdaExpressions {
    @FunctionalInterface
    static interface Operator<T> {
        T process(T a, T b);
    }
    static class Greeter {
        String name = "greeter";

        String fromLambda() {
            Supplier<String> supplier = () -> this.name;
            return supplier.get();
        }

        String fromAnonymousClass() {
            Supplier<String> supplier = new Supplier<>() {
                String name = "anonymous";

                @Override
                public String get() {
                    return this.name;
                }
            };
            return supplier.get();
        }
    }
    static record Task(String title, int priority, boolean done) {}
    public static void main(String[] args) throws Exception {
        {
            Supplier<String> greeting = () -> "Hello";
            Function<String, Integer> length = s -> s.length();
            BinaryOperator<Integer> add = (a, b) -> a + b;
            Comparator<String> byLength = (a, b) -> Integer.compare(a.length(), b.length());
            String hello = greeting.get();                                             // "Hello"
            show("hello", hello);
            int size = length.apply("lambda");                                         // 6
            show("size", size);
            int sum = add.apply(2, 3);                                                 // 5
            show("sum", sum);
            List<String> sorted = Stream.of("kiwi", "fig", "banana").sorted(byLength).toList();   // [fig, kiwi, banana]
            show("sorted", sorted);
        }
        {
            Function<Integer, Integer> square = a -> a * a;
            Function<Integer, Integer> squareBlock = a -> { return a * a; };
            int nine = square.apply(3);                                // 9
            show("nine", nine);
            int alsoNine = squareBlock.apply(3);                       // 9
            show("alsoNine", alsoNine);
        }
        {
            Operator<Integer> addNumbers = (a, b) -> a + b;
            Operator<String> appendText = (a, b) -> a + b;
            Operator<Integer> multiplyNumbers = (a, b) -> a * b;
            int six = addNumbers.process(3, 3);                        // 6
            show("six", six);
            String thirtyThree = appendText.process("3", "3");         // "33"
            show("thirtyThree", thirtyThree);
            int nineAgain = multiplyNumbers.process(3, 3);             // 9
            show("nineAgain", nineAgain);
        }
        {
            Greeter greeter = new Greeter();
            String lambdaThis = greeter.fromLambda();                  // "greeter"
            show("lambdaThis", lambdaThis);
            String anonymousThis = greeter.fromAnonymousClass();       // "anonymous"
            show("anonymousThis", anonymousThis);
        }
        {
            String prefix = "Task: ";
            Function<String, String> label = title -> prefix + title;
            String text = label.apply("pay rent");                     // "Task: pay rent"
            show("text", text);
        }
        {
            List<String> titles = List.of("buy milk", "call mom", "fix bike");
            long count = titles.stream().filter(t -> t.contains("i")).count();      // 2
            show("count", count);
            int letters = titles.stream().mapToInt(String::length).sum();         // 24
            show("letters", letters);
        }
        {
            Path notes = Files.writeString(Files.createTempFile("notes", ".txt"), "buy milk");
            Callable<String> readNotes = () -> Files.readString(notes);
            String content = readNotes.call();                         // "buy milk"
            show("content", content);
        }
        {
            Function<String, Integer> lengthLambda = s -> s.length();
            Function<String, Integer> lengthReference = String::length;
            int five = lengthLambda.apply("apple");                    // 5
            show("five", five);
            int alsoFive = lengthReference.apply("apple");             // 5
            show("alsoFive", alsoFive);
        }
        {
            List<Task> tasks = new ArrayList<>(List.of(new Task("pay rent", 1, false), new Task("water plants", 3, true), new Task("book dentist", 2, false)));
            tasks.sort((a, b) -> Integer.compare(a.priority(), b.priority()));
            boolean removed = tasks.removeIf(task -> task.done());                          // true
            show("removed", removed);
            List<String> order = tasks.stream().map(task -> task.title()).toList();        // [pay rent, book dentist]
            show("order", order);
            List<String> sent = new CopyOnWriteArrayList<>();
            List<Consumer<Task>> listeners = List.of(task -> sent.add("email: " + task.title()), task -> sent.add("push: " + task.title()));
            listeners.forEach(listener -> listener.accept(tasks.getFirst()));
            List<String> notifications = sent;                                            // [email: pay rent, push: pay rent]
            show("notifications", notifications);
            Thread reminder = Thread.ofVirtual().start(() -> sent.add("reminder: " + tasks.getLast().title()));
            reminder.join();
            String last = sent.getLast();                                                 // "reminder: book dentist"
            show("last", last);
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
