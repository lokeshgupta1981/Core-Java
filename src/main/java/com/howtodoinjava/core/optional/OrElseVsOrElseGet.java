package com.howtodoinjava.core.optional;

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

/**
 * Examples for the tutorial "orElse vs orElseGet in Java Optional (with Examples)".
 * https://howtodoinjava.com/java8/optional-orelse-and-oreleseget/
 */
public class OrElseVsOrElseGet {
    static class ThemeStore {

        private int lookups;

        String loadDefault() {
            lookups++;                  // stands in for a file read or a remote call
            return "light";
        }

        int lookups() {
            return lookups;
        }
    }
    static class CustomerRepository {

        private final List<String> rows = new ArrayList<>();

        Optional<String> findByEmail(String email) {
            return rows.stream().filter(email::equals).findFirst();
        }

        String insert(String email) {
            rows.add(email);
            return email;
        }

        int rowCount() {
            return rows.size();
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Optional<String> saved = Optional.of("dark");
            ThemeStore store = new ThemeStore();

            String eager = saved.orElse(store.loadDefault());       // "dark"
            show("eager", eager);
            String lazy = saved.orElseGet(store::loadDefault);      // "dark"
            show("lazy", lazy);
            int lookups = store.lookups();                          // 1, from the orElse() line
            show("lookups", lookups);
        }
        {
            Optional<String> saved = Optional.of("dark");
            ThemeStore store = new ThemeStore();

            String theme = saved.orElse(store.loadDefault());       // "dark"
            show("theme", theme);
            int lookups = store.lookups();                          // 1, loadDefault() ran anyway
            show("lookups", lookups);
        }
        {
            Optional<String> saved = Optional.of("dark");
            Optional<String> notSaved = Optional.empty();
            ThemeStore store = new ThemeStore();

            String theme = saved.orElseGet(store::loadDefault);     // "dark"
            show("theme", theme);
            int before = store.lookups();                           // 0, the supplier was not called
            show("before", before);
            String fallback = notSaved.orElseGet(store::loadDefault);  // "light"
            show("fallback", fallback);
            int after = store.lookups();                            // 1
            show("after", after);
        }
        {
            CustomerRepository repo = new CustomerRepository();
            repo.insert("ana@mail.com");

            String customer = repo.findByEmail("ana@mail.com")
                    .orElse(repo.insert("ana@mail.com"));
            int rows = repo.rowCount();                             // 2, a duplicate row
            show("rows", rows);
        }
        {
            CustomerRepository repo = new CustomerRepository();
            repo.insert("ana@mail.com");

            String known = repo.findByEmail("ana@mail.com")
                    .orElseGet(() -> repo.insert("ana@mail.com"));
            int rowsAfterKnown = repo.rowCount();                   // 1
            show("rowsAfterKnown", rowsAfterKnown);
            String fresh = repo.findByEmail("raj@mail.com")
                    .orElseGet(() -> repo.insert("raj@mail.com"));
            int rowsAfterNew = repo.rowCount();                     // 2, one row per customer
            show("rowsAfterNew", rowsAfterNew);
        }
        {
            Optional<String> userTheme = Optional.empty();
            Optional<String> teamTheme = Optional.of("system");

            String theme = userTheme.or(() -> teamTheme).orElse("light");          // "system"
            show("theme", theme);
            try { String required = userTheme.orElseThrow(); show("required", required); } catch (Throwable _t) { System.out.println("required -> " + _t); }
            try { String explained = userTheme.orElseThrow(() -> new IllegalStateException("No theme")); show("explained", explained); } catch (Throwable _t) { System.out.println("explained -> " + _t); }
        }
        {
            Optional<String> none = Optional.empty();
            Supplier<String> missing = null;

            String fromOrElse = none.orElse(null);                  // null
            show("fromOrElse", fromOrElse);
            String fromSupplier = none.orElseGet(() -> null);       // null
            show("fromSupplier", fromSupplier);
            String present = Optional.of("dark").orElseGet(missing);  // "dark", supplier not needed
            show("present", present);
            try { String broken = none.orElseGet(missing); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
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
