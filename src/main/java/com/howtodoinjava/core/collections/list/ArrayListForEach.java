package com.howtodoinjava.core.collections.list;

import java.util.concurrent.locks.*;
import java.lang.management.*;
import java.time.format.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.zip.*;
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

import java.time.temporal.*;

/**
 * Examples for the tutorial "ArrayList forEach() in Java: Lambdas, Limits and Examples".
 * https://howtodoinjava.com/java/collections/arraylist/arraylist-foreach/
 */
public class ArrayListForEach {
    static class Tracker {
        static final List<String> AUDIT = new ArrayList<>();
        static void audit(String city) {
            AUDIT.add("visited " + city);
        }
    }
    static record Stop(String city) {
        void announce() {
            Stop.SIGN.add("Next stop " + city);
        }
        static final List<String> SIGN = new ArrayList<>();
    }
    static void writeFile(Path dir, String city) {
        try {
            Files.writeString(dir.resolve(city + ".txt"), city);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    static interface Mailer {
        void send(String to, String text);
    }
    static List<String> notifyShipped(List<String> emails, Mailer mailer) {
        List<String> failed = new ArrayList<>();
        emails.forEach(to -> {
            try {
                mailer.send(to, "Your order has shipped");
            } catch (IllegalArgumentException e) {
                failed.add(to);
            }
        });
        return failed;
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> cities = new ArrayList<>(List.of("Oslo", "Rome", "Lima"));
            List<String> labels = new ArrayList<>();
            cities.forEach(c -> labels.add(c.toUpperCase()));
            List<String> result = labels;                   // [OSLO, ROME, LIMA]
            show("result", result);
            cities.forEach(System.out::println);            // prints Oslo, Rome, Lima on three lines
        }
        {
            List<String> names = new ArrayList<>(Arrays.asList("Ana", null, "Ben"));
            List<String> seen = new ArrayList<>();
            names.forEach(n -> seen.add(String.valueOf(n)));
            List<String> visited = seen;                        // [Ana, null, Ben]
            show("visited", visited);
            try { names.forEach(null);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            List<String> cities = List.of("Oslo", "Rome");
            List<String> copy = new ArrayList<>();
            cities.forEach(Tracker::audit);                      // static method
            cities.forEach(copy::add);                           // method of one particular object
            List<Stop> stops = List.of(new Stop("Oslo"), new Stop("Rome"));
            stops.forEach(Stop::announce);                       // method of each element
            List<String> audit = Tracker.AUDIT;                  // [visited Oslo, visited Rome]
            show("audit", audit);
            List<String> copied = copy;                          // [Oslo, Rome]
            show("copied", copied);
            List<String> board = Stop.SIGN;                      // [Next stop Oslo, Next stop Rome]
            show("board", board);
        }
        {
            List<String> cities = List.of("Oslo", "Rome", "Lima");
            StringBuilder csv = new StringBuilder();
            cities.forEach(c -> {
                if (!csv.isEmpty()) {
                    csv.append(',');
                }
                csv.append(c.toLowerCase());
            });
            String line = csv.toString();                        // "oslo,rome,lima"
            show("line", line);
        }
        {
            List<String> log = new ArrayList<>();
            List<String> outbox = new ArrayList<>();
            Consumer<String> logIt = c -> log.add("log " + c);
            Consumer<String> queueIt = c -> outbox.add("mail " + c);
            List.of("Oslo", "Rome").forEach(logIt.andThen(queueIt));
            List<String> logged = log;                           // [log Oslo, log Rome]
            show("logged", logged);
            List<String> queued = outbox;                        // [mail Oslo, mail Rome]
            show("queued", queued);
        }
        {
            List<Integer> temps = List.of(18, 21, 35, 19);
            List<Integer> printed = new ArrayList<>();
            temps.forEach(t -> {
                if (t > 30) {
                    return;                                      // skips 35 only, the loop goes on
                }
                printed.add(t);
            });
            List<Integer> skipped = printed;                     // [18, 21, 19]
            show("skipped", skipped);
            List<Integer> untilHot = temps.stream().takeWhile(t -> t <= 30).toList();   // [18, 21]
            show("untilHot", untilHot);
            boolean anyHot = temps.stream().anyMatch(t -> t > 30);                       // true
            show("anyHot", anyHot);
        }
        {
            List<String> cities = List.of("Oslo", "Rome", "Lima");
            List<String> numbered = new ArrayList<>();
            IntStream.range(0, cities.size()).forEach(i -> numbered.add((i + 1) + ". " + cities.get(i)));
            List<String> lines = numbered;                       // [1. Oslo, 2. Rome, 3. Lima]
            show("lines", lines);
        }
        {
            Path dir = Files.createTempDirectory("cities");
            List.of("Oslo", "Rome").forEach(c -> writeFile(dir, c));
            int files = dir.toFile().list().length;              // 2
            show("files", files);
        }
        {
            List<String> cities = List.of("Oslo", "Rome", "Lima");
            long longNames = cities.stream().filter(c -> c.length() > 3).count();   // 3
            show("longNames", longNames);
            AtomicInteger letters = new AtomicInteger();
            cities.forEach(c -> letters.addAndGet(c.length()));
            int totalLetters = letters.get();                                       // 12
            show("totalLetters", totalLetters);
        }
        {
            List<String> codes = new ArrayList<>(List.of("osl", "fco", "lim"));
            List<String> view = codes;
            codes.forEach(c -> view.set(view.indexOf(c), c.toUpperCase()));
            List<String> upper = codes;                          // [OSL, FCO, LIM]
            show("upper", upper);
        }
        {
            List<String> codes = new ArrayList<>(List.of("osl", "fco", "lim"));
            codes.replaceAll(String::toUpperCase);
            List<String> replaced = codes;                       // [OSL, FCO, LIM]
            show("replaced", replaced);
        }
        {
            List<String> trip = new ArrayList<>(List.of("Oslo", "Rome", "Lima"));
            try { trip.forEach(c -> { if (c.equals("Oslo")) trip.remove("Lima"); });  } catch (Throwable _t) { System.out.println("-> " + _t); }
            List<String> afterError = trip;                                       // [Oslo, Rome]
            show("afterError", afterError);
        }
        {
            Map<String, Integer> visitors = new TreeMap<>(Map.of("Rome", 120, "Oslo", 45));
            List<String> report = new ArrayList<>();
            visitors.forEach((city, count) -> report.add(city + "=" + count));
            List<String> rows = report;                          // [Oslo=45, Rome=120]
            show("rows", rows);
        }
        {
            List<Integer> nums = List.of(1, 2, 3, 4, 5, 6);
            List<Integer> ordered = new ArrayList<>();
            nums.parallelStream().map(n -> n * 10).forEachOrdered(ordered::add);
            List<Integer> tens = ordered;                        // [10, 20, 30, 40, 50, 60]
            show("tens", tens);
        }
        {
            List<String> sent = new ArrayList<>();
            Mailer fake = (to, text) -> {
                if (!to.contains("@")) {
                    throw new IllegalArgumentException("bad address " + to);
                }
                sent.add(to);
            };
            List<String> failures = notifyShipped(List.of("ana@shop.io", "ben-at-shop", "eva@shop.io"), fake);   // [ben-at-shop]
            show("failures", failures);
            int delivered = sent.size();                                                                   // 2
            show("delivered", delivered);
        }
        {
            String[] codes = {"osl", "fco"};
            List<String> seenCodes = new ArrayList<>();
            Arrays.stream(codes).forEach(seenCodes::add);
            List<String> fromArray = seenCodes;                  // [osl, fco]
            show("fromArray", fromArray);
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
