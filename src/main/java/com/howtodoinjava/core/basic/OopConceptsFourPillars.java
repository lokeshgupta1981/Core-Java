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

/**
 * Examples for the tutorial "Java OOP Concepts: The 4 Pillars with Examples".
 * https://howtodoinjava.com/java/oops/object-oriented-programming/
 */
public class OopConceptsFourPillars {
    static interface Borrowable {                           // abstraction
        int loanDays();
    }

    static abstract class LibraryItem implements Borrowable {
        private final String title;                   // encapsulation

        LibraryItem(String title) {
            this.title = Objects.requireNonNull(title, "title");
        }

        String title() {
            return title;
        }
    }

    static class Book extends LibraryItem {                  // inheritance
        Book(String title) {
            super(title);
        }

        public int loanDays() {
            return 21;
        }
    }

    static class Magazine extends LibraryItem {
        Magazine(String title) {
            super(title);
        }

        public int loanDays() {
            return 7;
        }
    }
    static record Member(String name, String email) {
    }
    static class Loan {
        private LocalDate dueDate;
        private int renewals;

        Loan(Borrowable item, LocalDate start) {
            this.dueDate = start.plusDays(item.loanDays());
        }

        void renew() {
            if (renewals == 2) {
                throw new IllegalStateException("Renewal limit reached");
            }
            renewals++;
            dueDate = dueDate.plusDays(14);
        }

        LocalDate dueDate() {
            return dueDate;
        }
    }
    static int lateFee(int daysLate) {
        return lateFee(daysLate, 25);
    }

    static int lateFee(int daysLate, int centsPerDay) {
        return Math.max(0, daysLate) * centsPerDay;
    }
    static interface Notifier {
        String send(String to, String text);
    }

    static class OverdueReminder {
        private final Notifier notifier;

        OverdueReminder(Notifier notifier) {
            this.notifier = notifier;
        }

        String remind(Member member, String title) {
            return notifier.send(member.email(), "Please return " + title);
        }
    }
    static sealed interface Fee permits LateFee, LostItemFee {
    }

    static record LateFee(int days) implements Fee {
    }

    static record LostItemFee(int priceCents) implements Fee {
    }
    static int amount(Fee fee) {
        return switch (fee) {
            case LateFee(int days) -> days * 25;
            case LostItemFee(int priceCents) -> priceCents + 500;
        };
    }
    public static void main(String[] args) throws Exception {
        {
            List<Borrowable> items = List.of(new Book("Dune"), new Magazine("Wired"));
            int bookDays = items.get(0).loanDays();           // 21, polymorphism
            show("bookDays", bookDays);
            int magazineDays = items.get(1).loanDays();       // 7
            show("magazineDays", magazineDays);
        }
        {
            Book first = new Book("Dune");
            Book second = new Book("Dune");
            boolean sameObject = first == second;             // false
            show("sameObject", sameObject);
            boolean sameTitle = first.title().equals(second.title());  // true
            show("sameTitle", sameTitle);
            Member ana = new Member("Ana", "ana@example.com");
            boolean equalMembers = ana.equals(new Member("Ana", "ana@example.com"));  // true
            show("equalMembers", equalMembers);
        }
        {
            Loan loan = new Loan(new Book("Dune"), LocalDate.of(2026, 1, 5));
            LocalDate due = loan.dueDate();                   // 2026-01-26
            show("due", due);
            loan.renew();
            loan.renew();
            LocalDate renewed = loan.dueDate();               // 2026-02-23
            show("renewed", renewed);
            try { loan.renew();  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            int standardFee = lateFee(4);                     // 100
            show("standardFee", standardFee);
            int reducedFee = lateFee(4, 10);                  // 40
            show("reducedFee", reducedFee);
        }
        {
            Notifier fakeMail = (to, text) -> to + " <- " + text;
            OverdueReminder reminder = new OverdueReminder(fakeMail);
            String sent = reminder.remind(new Member("Ana", "ana@example.com"), "Dune");  // "ana@example.com <- Please return Dune"
            show("sent", sent);
        }
        {
            int late = amount(new LateFee(6));                // 150
            show("late", late);
            int lost = amount(new LostItemFee(1999));         // 2499
            show("lost", lost);
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
