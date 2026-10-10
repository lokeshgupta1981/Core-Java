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
 * Examples for the tutorial "Block Statement in Java: Braces, Scope and Initializers".
 * https://howtodoinjava.com/java/basics/what-is-block-statement-in-java/
 */
public class BlockScopeNesting {
    static class Catalog {
        static final List<String> LOG = new ArrayList<>();
        static int books;

        static {
            books = 100;
            LOG.add("static block");
        }

        {
            LOG.add("instance block");
        }

        Catalog() {
            LOG.add("constructor");
        }
    }
    public static void main(String[] args) throws Exception {
        {
            int copies = 2;
            String status = "Waitlisted";
            if (copies > 0) {                       // block starts
                copies--;
                status = "Checked out";
            }                                       // block ends
            int left = copies;                      // 1
            show("left", left);
            String shown = status;                  // "Checked out"
            show("shown", shown);
        }
        {
            int daysLate = 0;
            int fine = 0;
            boolean notified = false;
            if (daysLate > 0)
            fine = daysLate * 2;
            notified = true;                    // runs always, despite the indentation
            boolean sent = notified;                // true
            show("sent", sent);
        }
        {
            int daysLate = 0;
            int fine = 0;
            boolean notified = false;
            if (daysLate > 0) {
                fine = daysLate * 2;
                notified = true;
            }
            boolean sent = notified;                // false
            show("sent", sent);
        }
        {
            int daysOverdue = 0;
            boolean blocked = false;
            if (daysOverdue > 30);                  // the semicolon ends the if
            {
                blocked = true;                     // standalone block, always runs
            }
            boolean result = blocked;               // true
            show("result", result);
        }
        {
            int total = 0;
            {
                int fee = 2;
                total += fee;
            }
            {
                int fee = 3;                        // allowed, the first fee is out of scope
                total += fee;
            }
            int charged = total;                    // 5
            show("charged", charged);
        }
        {
            new Catalog();
            new Catalog();
            List<String> order = Catalog.LOG;       // [static block, instance block, constructor, instance block, constructor]
            show("order", order);
            int stock = Catalog.books;              // 100
            show("stock", stock);
        }
        {
            List<String> shelf = List.of("Dune", "Emma", "Ulysses");
            String wanted = "Emma";
            String message;
            search: {
                for (String title : shelf) {
                    if (title.equals(wanted)) {
                        message = "found " + title;
                        break search;
                    }
                }
                message = "ask the librarian";      // skipped when break search runs
            }
            String result = message;                // "found Emma"
            show("result", result);
        }
        {
            List<String> titles = List.of("Dune", "Emma");
            record Loan(String title, int days) {}
            Loan loan = new Loan(titles.get(0), 14);
            String borrowed = loan.title();         // "Dune"
            show("borrowed", borrowed);
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
