package com.howtodoinjava.core.datetime;

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
import java.time.temporal.*;
import java.time.chrono.*;
import java.time.zone.*;
import java.text.*;

/**
 * Examples for the tutorial "Java Period Class: Years, Months and Days in java.time".
 * https://howtodoinjava.com/java/date-time/java8-period/
 */
public class PeriodExample {
    static Optional<Period> parsePeriod(String text) {
        if (text == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Period.parse(text.strip()));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }
    static boolean underWarranty(LocalDate purchased, String term, LocalDate today) {
        LocalDate expires = purchased.plus(Period.parse(term));
        return today.isBefore(expires);
    }
    static List<LocalDate> renewals(LocalDate start, Period plan, int count) {
        return IntStream.rangeClosed(1, count)
                .mapToObj(i -> start.plus(plan.multipliedBy(i)))
                .toList();
    }
    public static void main(String[] args) throws Exception {
        {
            LocalDate purchased = LocalDate.of(2026, 1, 31);
            Period warranty = Period.of(2, 6, 0);                    // P2Y6M
            show("warranty", warranty);
            LocalDate expires = purchased.plus(warranty);            // 2028-07-31
            show("expires", expires);
            Period parsed = Period.parse("P1Y2M3D");                 // P1Y2M3D
            show("parsed", parsed);
            Period age = Period.between(LocalDate.of(1990, 5, 14), LocalDate.of(2026, 10, 10));   // P36Y4M26D
            show("age", age);
            int years = age.getYears();                              // 36
            show("years", years);
            Period fourteenMonths = Period.ofMonths(14).normalized();   // P1Y2M
            show("fourteenMonths", fourteenMonths);
        }
        {
            Period plan = Period.of(1, 3, 15);          // P1Y3M15D
            show("plan", plan);
            Period trial = Period.ofDays(14);            // P14D
            show("trial", trial);
            Period quarter = Period.ofMonths(3);         // P3M
            show("quarter", quarter);
            Period term = Period.ofYears(2);             // P2Y
            show("term", term);
            Period sprint = Period.ofWeeks(2);           // P14D
            show("sprint", sprint);
            Period none = Period.ZERO;                   // P0D
            show("none", none);
        }
        {
            Period full = Period.parse("P1Y2M3D");          // P1Y2M3D
            show("full", full);
            Period weeks = Period.parse("P52W");            // P364D
            show("weeks", weeks);
            Period mixed = Period.parse("P1Y2M3W4D");       // P1Y2M25D
            show("mixed", mixed);
            Period lower = Period.parse("p1y2m");           // P1Y2M
            show("lower", lower);
            Period negative = Period.parse("-P1Y2M");       // P-1Y-2M
            show("negative", negative);
            try { Period hours = Period.parse("PT5H"); show("hours", hours); } catch (Throwable _t) { System.out.println("hours -> " + _t); }
        }
        {
            Optional<Period> ok = parsePeriod(" P30D ");      // Optional[P30D]
            show("ok", ok);
            Optional<Period> bad = parsePeriod("30 days");     // Optional.empty
            show("bad", bad);
        }
        {
            LocalDate start = LocalDate.of(2020, 3, 12);
            LocalDate end = LocalDate.of(2020, 7, 20);
            Period between = Period.between(start, end);    // P4M8D
            show("between", between);
            Period until = start.until(end);                // P4M8D
            show("until", until);
            Period reversed = Period.between(end, start);   // P-4M-8D
            show("reversed", reversed);
            Period shortMonth = Period.between(LocalDate.of(2026, 1, 31), LocalDate.of(2026, 3, 1));   // P1M1D
            show("shortMonth", shortMonth);
        }
        {
            Period period = Period.of(1, 14, 40);
            int years = period.getYears();                    // 1
            show("years", years);
            int months = period.getMonths();                  // 14
            show("months", months);
            int days = period.getDays();                      // 40
            show("days", days);
            long viaUnit = period.get(ChronoUnit.MONTHS);     // 14
            show("viaUnit", viaUnit);
            long totalMonths = period.toTotalMonths();        // 26
            show("totalMonths", totalMonths);
            List<TemporalUnit> units = period.getUnits();     // [Years, Months, Days]
            show("units", units);
            try { long weeks = period.get(ChronoUnit.WEEKS); show("weeks", weeks); } catch (Throwable _t) { System.out.println("weeks -> " + _t); }
        }
        {
            boolean zero = Period.ZERO.isZero();                    // true
            show("zero", zero);
            boolean negative = Period.of(0, 1, -1).isNegative();    // true
            show("negative", negative);
            boolean positive = Period.ofDays(3).isNegative();       // false
            show("positive", positive);
        }
        {
            Period base = Period.of(1, 2, 3);
            Period longer = base.plus(Period.ofDays(10));        // P1Y2M13D
            show("longer", longer);
            Period shorter = base.minus(Period.ofMonths(5));     // P1Y-3M3D
            show("shorter", shorter);
            Period noDays = base.withDays(0);                    // P1Y2M
            show("noDays", noDays);
            Period doubled = base.multipliedBy(2);               // P2Y4M6D
            show("doubled", doubled);
            Period flipped = base.negated();                     // P-1Y-2M-3D
            show("flipped", flipped);
            Period tidy = Period.of(1, 14, 40).normalized();     // P2Y2M40D
            show("tidy", tidy);
            Period borrow = Period.of(1, -3, 0).normalized();    // P9M
            show("borrow", borrow);
        }
        {
            LocalDate endOfJanuary = LocalDate.of(2024, 1, 30);
            LocalDate monthsFirst = endOfJanuary.plus(Period.of(0, 1, 1));        // 2024-03-01
            show("monthsFirst", monthsFirst);
            LocalDate daysFirst = endOfJanuary.plusDays(1).plusMonths(1);         // 2024-02-29
            show("daysFirst", daysFirst);
            LocalDate leapDay = LocalDate.of(2024, 2, 29).plus(Period.ofYears(1));   // 2025-02-28
            show("leapDay", leapDay);
            LocalDate lastMonth = LocalDate.of(2026, 10, 10).minus(Period.ofMonths(1));   // 2026-09-10
            show("lastMonth", lastMonth);
        }
        {
            LocalDate anchor = LocalDate.of(2026, 1, 31);
            LocalDate stepByStep = anchor.plusMonths(1).plusMonths(1);                // 2026-03-28
            show("stepByStep", stepByStep);
            LocalDate fromAnchor = anchor.plus(Period.ofMonths(1).multipliedBy(2));   // 2026-03-31
            show("fromAnchor", fromAnchor);
        }
        {
            LocalDateTime meeting = LocalDateTime.of(2026, 1, 31, 18, 0).plus(Period.ofMonths(1));   // 2026-02-28T18:00
            show("meeting", meeting);
            Instant nextDay = Instant.parse("2026-03-07T12:00:00Z").plus(Period.ofDays(1));         // 2026-03-08T12:00:00Z
            show("nextDay", nextDay);
            try { Instant nextMonth = Instant.parse("2026-03-07T12:00:00Z").plus(Period.ofMonths(1)); show("nextMonth", nextMonth); } catch (Throwable _t) { System.out.println("nextMonth -> " + _t); }
            try { LocalTime noon = LocalTime.NOON.plus(Period.ofDays(1)); show("noon", noon); } catch (Throwable _t) { System.out.println("noon -> " + _t); }
        }
        {
            ZonedDateTime saturdayNoon = ZonedDateTime.of(2026, 3, 7, 12, 0, 0, 0, ZoneId.of("America/New_York"));
            ZonedDateTime calendarDay = saturdayNoon.plus(Period.ofDays(1));     // 2026-03-08T12:00-04:00[America/New_York]
            show("calendarDay", calendarDay);
            ZonedDateTime exactDay = saturdayNoon.plus(Duration.ofDays(1));      // 2026-03-08T13:00-04:00[America/New_York]
            show("exactDay", exactDay);
        }
        {
            LocalDate bought = LocalDate.of(2025, 4, 2);
            boolean laptop = underWarranty(bought, "P2Y", LocalDate.of(2026, 10, 10));       // true
            show("laptop", laptop);
            boolean headphones = underWarranty(bought, "P6M", LocalDate.of(2026, 10, 10));   // false
            show("headphones", headphones);
            List<LocalDate> support = renewals(LocalDate.of(2026, 1, 31), Period.ofMonths(1), 4);   // [2026-02-28, 2026-03-31, 2026-04-30, 2026-05-31]
            show("support", support);
        }
        {
            LocalDate from = LocalDate.of(2026, 1, 1);
            long days = ChronoUnit.DAYS.between(from, from.plus(Period.of(0, 2, 10)));   // 69
            show("days", days);
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
