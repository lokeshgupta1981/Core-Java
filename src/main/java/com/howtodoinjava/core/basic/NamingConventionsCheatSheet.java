package com.howtodoinjava.core.basic;

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
 * Examples for the tutorial "Java Naming Conventions for Classes, Methods and Variables".
 * https://howtodoinjava.com/java/basics/java-naming-conventions/
 */
public class NamingConventionsCheatSheet {
    static public class LibraryMember {                        // class: UpperCamelCase noun
        public static final int MAX_LOANS = 5;          // constant: UPPER_SNAKE_CASE
        private String fullName;                        // field: lowerCamelCase noun
        public boolean canBorrow(int activeLoans) {     // method: lowerCamelCase verb, parameter: lowerCamelCase
            return activeLoans < MAX_LOANS;
        }
    }
    static interface Borrowable {
        boolean isAvailable();
    }
    static record Book(String title, int year) {}
    static enum LoanStatus { ACTIVE, OVERDUE, RETURNED }
    @interface Audited {}
    static class LoanRules {
        static final int MAX_LOANS = 5;                                     // constant
        static final List<String> GENRES = List.of("fiction", "poetry");    // constant, unmodifiable list
        static final List<String> waitingList = new ArrayList<>();          // not a constant, its contents change
        private final int loanDays = 14;                                    // final, but one copy per object
    }
    static <T> T firstOrDefault(List<T> items, T fallback) {
        return items.isEmpty() ? fallback : items.get(0);
    }
    static class Calc {
        static double calc(List<Integer> d, boolean f) {
            double r = 0;
            for (int x : d) {
                r += f ? x * 0.125 : x * 0.25;
            }
            return r;
        }
    }
    static class LateFeeCalculator {
        static final double LATE_FEE_PER_DAY = 0.25;

        static double totalLateFees(List<Integer> daysOverdue, boolean isPremiumMember) {
            double feePerDay = isPremiumMember ? LATE_FEE_PER_DAY / 2 : LATE_FEE_PER_DAY;
            double total = 0;
            for (int days : daysOverdue) {
                total += days * feePerDay;
            }
            return total;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            int $count = 1;                 // 1, legal, but $ is meant for generated code
            int _count = 2;                 // 2, legal, but against the conventions
            show("_count", _count);
            int var = 3;                    // 3, var is a contextual keyword and still a legal variable name
            show("var", var);
            int record = 4;                 // 4, the same for record
            show("record", record);
        }
        {
            Book book = new Book("Dune", 1965);
            String title = book.title();            // "Dune"
            show("title", title);
            int year = book.year();                 // 1965
            show("year", year);
        }
        {
            int maxLoans = LoanRules.MAX_LOANS;               // 5
            show("maxLoans", maxLoans);
            int genreCount = LoanRules.GENRES.size();          // 2
            show("genreCount", genreCount);
        }
        {
            String firstTitle = firstOrDefault(List.of("Dune", "Emma"), "none");    // "Dune"
            show("firstTitle", firstTitle);
            String noTitle = firstOrDefault(List.of(), "none");                       // "none"
            show("noTitle", noTitle);
        }
        {
            List<Integer> overdue = List.of(4, 2);
            double before = Calc.calc(overdue, true);                              // 0.75
            show("before", before);
            double after = LateFeeCalculator.totalLateFees(overdue, true);         // 0.75
            show("after", after);
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
