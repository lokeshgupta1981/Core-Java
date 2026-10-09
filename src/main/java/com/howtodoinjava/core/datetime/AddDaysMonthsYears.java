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
 * Examples for the tutorial "Add Days to a Date in Java: plusDays, plusMonths, plusYears".
 * https://howtodoinjava.com/java/date-time/add-days-months-years/
 */
public class AddDaysMonthsYears {
    static LocalDate billingDate(LocalDate signup, int cycle) {
        return signup.plusMonths(cycle);    // always from the anchor date
    }
    public static void main(String[] args) throws Exception {
        {
            LocalDate invoiceDate = LocalDate.of(2026, 1, 31);
            LocalDate dueDate = invoiceDate.plusDays(30);             // 2026-03-02
            show("dueDate", dueDate);
            LocalDate weekBefore = invoiceDate.minusWeeks(1);         // 2026-01-24
            show("weekBefore", weekBefore);
            LocalDate nextMonth = invoiceDate.plusMonths(1);          // 2026-02-28
            show("nextMonth", nextMonth);
            LocalDate lastYear = invoiceDate.minusYears(1);           // 2025-01-31
            show("lastYear", lastYear);
            LocalDate mixed = invoiceDate.plus(Period.of(1, 2, 3));   // 2027-04-03
            show("mixed", mixed);
        }
        {
            LocalDate start = LocalDate.of(2026, 5, 10);
            start.plusDays(5);                                  // result ignored, start is still 2026-05-10
            LocalDate end = start.plusDays(5);                  // 2026-05-15
            show("end", end);
            LocalDate back = start.plusDays(-5);                // 2026-05-05
            show("back", back);
        }
        {
            try { LocalDate overflow = LocalDate.MAX.plusDays(1); show("overflow", overflow); } catch (Throwable _t) { System.out.println("overflow -> " + _t); }
        }
        {
            LocalDate signup = LocalDate.of(2026, 4, 15);
            LocalDate trialEnd = signup.plus(14, ChronoUnit.DAYS);        // 2026-04-29
            show("trialEnd", trialEnd);
            LocalDate review = signup.plus(6, ChronoUnit.MONTHS);         // 2026-10-15
            show("review", review);
            LocalDate archive = signup.minus(2, ChronoUnit.YEARS);        // 2024-04-15
            show("archive", archive);
        }
        {
            Period gracePeriod = Period.parse("P1M10D");                   // P1M10D
            show("gracePeriod", gracePeriod);
            LocalDate expiry = LocalDate.of(2026, 4, 15).plus(gracePeriod);  // 2026-05-25
            show("expiry", expiry);
            LocalDate before = LocalDate.of(2026, 4, 15).minus(gracePeriod); // 2026-03-05
            show("before", before);
        }
        {
            LocalDate jan31 = LocalDate.of(2026, 1, 31);
            LocalDate feb = jan31.plusMonths(1);                // 2026-02-28
            show("feb", feb);
            LocalDate roundTrip = feb.minusMonths(1);           // 2026-01-28, not January 31
            show("roundTrip", roundTrip);
            LocalDate leapDay = LocalDate.of(2028, 2, 29);
            LocalDate nextYear = leapDay.plusYears(1);          // 2029-02-28
            show("nextYear", nextYear);
            LocalDate fourYears = leapDay.plusYears(4);         // 2032-02-29
            show("fourYears", fourYears);
        }
        {
            LocalDate jan30 = LocalDate.of(2026, 1, 30);
            LocalDate monthFirst = jan30.plusMonths(1).plusDays(1);   // 2026-03-01
            show("monthFirst", monthFirst);
            LocalDate dayFirst = jan30.plusDays(1).plusMonths(1);     // 2026-02-28
            show("dayFirst", dayFirst);
        }
        {
            LocalDate signupDay = LocalDate.of(2026, 1, 31);
            LocalDate second = billingDate(signupDay, 1);                     // 2026-02-28
            show("second", second);
            LocalDate third = billingDate(signupDay, 2);                      // 2026-03-31
            show("third", third);
            LocalDate drifted = signupDay.plusMonths(1).plusMonths(1);        // 2026-03-28
            show("drifted", drifted);
        }
        {
            LocalDateTime meeting = LocalDateTime.of(2026, 1, 31, 14, 30);
            LocalDateTime followUp = meeting.plusDays(7);       // 2026-02-07T14:30
            show("followUp", followUp);
            LocalDateTime nextMonthSlot = meeting.plusMonths(1); // 2026-02-28T14:30
            show("nextMonthSlot", nextMonthSlot);
        }
        {
            ZonedDateTime standup = ZonedDateTime.of(2026, 3, 7, 9, 0, 0, 0, ZoneId.of("America/New_York"));
            ZonedDateTime nextStandup = standup.plusDays(1);              // 2026-03-08T09:00-04:00[America/New_York]
            show("nextStandup", nextStandup);
            Duration elapsed = Duration.between(standup, nextStandup);    // PT23H
            show("elapsed", elapsed);
        }
        {
            Date legacy = Date.from(Instant.parse("2026-01-31T10:00:00Z"));
            ZoneId zone = ZoneId.of("Europe/Paris");
            LocalDate asDate = legacy.toInstant().atZone(zone).toLocalDate();      // 2026-01-31
            show("asDate", asDate);
            LocalDate plusTen = asDate.plusDays(10);                               // 2026-02-10
            show("plusTen", plusTen);
            Date backToLegacy = Date.from(plusTen.atStartOfDay(zone).toInstant());
            Instant stored = backToLegacy.toInstant();                             // 2026-02-09T23:00:00Z
            show("stored", stored);
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
