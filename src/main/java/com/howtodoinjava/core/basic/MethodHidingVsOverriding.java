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
 * Examples for the tutorial "Method Hiding in Java: Static Methods vs Overriding".
 * https://howtodoinjava.com/java/oops/method-hiding/
 */
public class MethodHidingVsOverriding {
    static class AppConfig {
        static String source() { return "defaults"; }
        String name() { return "app"; }
    }

    static class TestConfig extends AppConfig {
        static String source() { return "test file"; }
        @Override
        String name() { return "test"; }
    }
    static class Report {
        static String header() { return "Report"; }
        static String title() { return header() + " v1"; }
    }

    static class SalesReport extends Report {
        static String header() { return "Sales"; }
    }
    static class Plan {
        static String tier = "basic";
        int seats = 1;
    }

    static class TeamPlan extends Plan {
        static String tier = "team";
        int seats = 10;

        int parentSeats() { return super.seats; }
    }
    static interface TimeSource {
        LocalDate now();
    }

    static record FixedTime(LocalDate date) implements TimeSource {
        @Override
        public LocalDate now() { return date; }
    }
    static class InvoiceService {
        private final TimeSource time;

        InvoiceService(TimeSource time) { this.time = time; }

        String dueDate() { return time.now().plusDays(30).toString(); }
    }
    public static void main(String[] args) throws Exception {
        {
            AppConfig config = new TestConfig();
            String viaParent = AppConfig.source();     // "defaults"
            show("viaParent", viaParent);
            String viaChild = TestConfig.source();     // "test file"
            show("viaChild", viaChild);
            String overridden = config.name();         // "test"
            show("overridden", overridden);
        }
        {
            AppConfig config = new TestConfig();
            String fromVariable = config.source();     // "defaults", declared type wins
            show("fromVariable", fromVariable);
            AppConfig nothing = null;
            try { String fromNull = nothing.source(); show("fromNull", fromNull); } catch (Throwable _t) { System.out.println("fromNull -> " + _t); }
        }
        {
            String parentTitle = Report.title();       // "Report v1"
            show("parentTitle", parentTitle);
            String childTitle = SalesReport.title();   // "Report v1", not "Sales v1"
            show("childTitle", childTitle);
        }
        {
            Plan plan = new TeamPlan();
            int declaredSeats = plan.seats;            // 1, declared type Plan
            show("declaredSeats", declaredSeats);
            int realSeats = ((TeamPlan) plan).seats;   // 10
            show("realSeats", realSeats);
            int hiddenSeats = new TeamPlan().parentSeats(); // 1, via super
            show("hiddenSeats", hiddenSeats);
            String tier = TeamPlan.tier;               // "team"
            show("tier", tier);
        }
        {
            TimeSource fixed = new FixedTime(LocalDate.of(2026, 1, 1));
            String due = new InvoiceService(fixed).dueDate();  // "2026-01-31"
            show("due", due);
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
