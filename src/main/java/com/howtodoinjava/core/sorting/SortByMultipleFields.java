package com.howtodoinjava.core.sorting;

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
 * Examples for the tutorial "Sort by Multiple Fields in Java With Comparator thenComparing()".
 * https://howtodoinjava.com/java/sort/sort-on-multiple-fields/
 */
public class SortByMultipleFields {
    static record Runner(String name, String country, int minutes) {}
    static record Lap(String name, int bib, long millis, double pace) {}
    static <T> Comparator<T> chainOf(List<Comparator<T>> keys) {
        return keys.stream().reduce(Comparator::thenComparing).orElse((a, b) -> 0);
    }
    static record Result(String name, boolean finished, int minutes) {
        boolean dnf() {
            return !finished;
        }
    }
    static class RaceBoard {
        static final Comparator<Result> ORDER = Comparator.comparing(Result::dnf)
                .thenComparingInt(Result::minutes)
                .thenComparing(Result::name);
    }
    public static void main(String[] args) throws Exception {
        {
            List<Runner> runners = new ArrayList<>(List.of(
            new Runner("Ivo", "NO", 171), new Runner("Ana", "KE", 128),
            new Runner("Lea", "NO", 150), new Runner("Tom", "KE", 128), new Runner("Bo", "KE", 140)));
            runners.sort(Comparator.comparing(Runner::country).thenComparingInt(Runner::minutes));
            List<String> twoKeys = runners.stream().map(Runner::name).toList();     // [Ana, Tom, Bo, Lea, Ivo]
            show("twoKeys", twoKeys);
            runners.sort(Comparator.comparing(Runner::country).thenComparingInt(Runner::minutes).thenComparing(Runner::name, Comparator.reverseOrder()));
            List<String> threeKeys = runners.stream().map(Runner::name).toList();   // [Tom, Ana, Bo, Lea, Ivo]
            show("threeKeys", threeKeys);
        }
        {
            Comparator<Runner> byCountry = Comparator.comparing(Runner::country);
            Comparator<Runner> byTime = Comparator.comparingInt(Runner::minutes);
            Comparator<Runner> handMade = (a, b) -> {
                int res = byCountry.compare(a, b);
                return res != 0 ? res : byTime.compare(a, b);
            };
            Comparator<Runner> chained = byCountry.thenComparing(byTime);
            Runner lea = new Runner("Lea", "NO", 150);
            Runner ivo = new Runner("Ivo", "NO", 171);
            int byHand = handMade.compare(lea, ivo);           // -1
            show("byHand", byHand);
            int byChain = chained.compare(lea, ivo);           // -1
            show("byChain", byChain);
        }
        {
            List<Lap> splits = new ArrayList<>(List.of(
            new Lap("eva", 3, 900L, 4.5), new Lap("Ben", 1, 900L, 4.5),
            new Lap("ada", 2, 500L, 4.1), new Lap("ben", 4, 900L, 4.5)));
            splits.sort(Comparator.comparingDouble(Lap::pace).thenComparingLong(Lap::millis).thenComparing(Lap::name, String.CASE_INSENSITIVE_ORDER).thenComparingInt(Lap::bib));
            List<Integer> bibs = splits.stream().map(Lap::bib).toList();     // [2, 1, 4, 3]
            show("bibs", bibs);
        }
        {
            List<Runner> board = new ArrayList<>(List.of(new Runner("Ivo", "NO", 171), new Runner("Ana", "KE", 128), new Runner("Lea", "NO", 150)));
            Comparator<Runner> order = Comparator.comparing(Runner::country).thenComparingInt(Runner::minutes);
            Collections.sort(board, order);
            String first = board.get(0).name();                                  // "Ana"
            show("first", first);
            List<Runner> fixed = Arrays.asList(new Runner("Ivo", "NO", 171), new Runner("Lea", "NO", 150));
            fixed.sort(order);
            String fixedFirst = fixed.get(0).name();                             // "Lea"
            show("fixedFirst", fixedFirst);
            List<Runner> frozen = List.of(new Runner("Ivo", "NO", 171), new Runner("Lea", "NO", 150));
            try { frozen.sort(order);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            List<Runner> field = new ArrayList<>(List.of(
            new Runner("Zoe", "FR", 140), new Runner("\u00c9mile", "FR", 140), new Runner("Eva", "FR", 140)));
            field.sort(Comparator.comparingInt(Runner::minutes).thenComparing(Runner::name));
            List<String> byCharValue = field.stream().map(Runner::name).toList();     // [Eva, Zoe, \u00c9mile]
            show("byCharValue", byCharValue);
            Collator french = Collator.getInstance(Locale.FRENCH);
            field.sort(Comparator.comparingInt(Runner::minutes).thenComparing(Runner::name, french));
            List<String> byLanguage = field.stream().map(Runner::name).toList();      // [\u00c9mile, Eva, Zoe]
            show("byLanguage", byLanguage);
        }
        {
            List<Comparator<Runner>> layout = List.of(Comparator.comparing(Runner::country), Comparator.comparingInt(Runner::minutes).reversed());
            List<Runner> table = new ArrayList<>(List.of(new Runner("Ivo", "NO", 171), new Runner("Ana", "KE", 128), new Runner("Lea", "NO", 150), new Runner("Bo", "KE", 140)));
            table.sort(chainOf(layout));
            List<String> slowestFirst = table.stream().map(Runner::name).toList();   // [Bo, Ana, Ivo, Lea]
            show("slowestFirst", slowestFirst);
        }
        {
            Map<String, Integer> counts = Map.of("run", 4, "pace", 2, "lap", 4, "bib", 1);
            Comparator<Map.Entry<String, Integer>> byCountDesc = Map.Entry.<String, Integer>comparingByValue().reversed();
            List<String> ranked = counts.entrySet().stream().sorted(byCountDesc.thenComparing(Map.Entry.comparingByKey())).map(Map.Entry::getKey).toList();   // [lap, run, pace, bib]
            show("ranked", ranked);
        }
        {
            int[][] laps = {{5, 40}, {4, 55}, {5, 12}, {4, 55}};
            Arrays.sort(laps, Comparator.<int[]>comparingInt(r -> r[0]).thenComparingInt(r -> r[1]));
            int[][] sortedLaps = laps;                                   // [[4, 55], [4, 55], [5, 12], [5, 40]]
            show("sortedLaps", sortedLaps);
        }
        {
            List<Runner> twice = new ArrayList<>(List.of(new Runner("Ivo", "NO", 171), new Runner("Ana", "KE", 128), new Runner("Lea", "NO", 150), new Runner("Bo", "KE", 140)));
            twice.sort(Comparator.comparingInt(Runner::minutes));
            twice.sort(Comparator.comparing(Runner::country));
            List<String> viaTwoSorts = twice.stream().map(Runner::name).toList();    // [Ana, Bo, Lea, Ivo]
            show("viaTwoSorts", viaTwoSorts);
        }
        {
            List<Result> results = new ArrayList<>(List.of(
            new Result("Ivo", false, 0), new Result("Tom", true, 128),
            new Result("Ana", true, 128), new Result("Lea", true, 150)));
            results.sort(RaceBoard.ORDER);
            List<String> page = new ArrayList<>();
            for (int i = 0; i < results.size(); i++) {
                Result r = results.get(i);
                page.add(r.finished() ? (i + 1) + ". " + r.name() : "DNF " + r.name());
            }
            List<String> lines = page;                         // [1. Ana, 2. Tom, 3. Lea, DNF Ivo]
            show("lines", lines);
        }
        {
            Comparator<Runner> typed = Comparator.comparing((Runner r) -> r.country()).thenComparingInt(Runner::minutes);
            Comparator<Runner> witness = Comparator.<Runner, String>comparing(r -> r.country()).thenComparingInt(Runner::minutes);
            int sameResult = Integer.signum(typed.compare(new Runner("Ana", "KE", 128), new Runner("Lea", "NO", 150)));   // -1
            show("sameResult", sameResult);
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
