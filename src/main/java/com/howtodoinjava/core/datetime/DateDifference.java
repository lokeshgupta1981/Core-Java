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
 * Examples for the tutorial "Difference Between Two Dates in Java: Period and Duration".
 * https://howtodoinjava.com/java/date-time/calculate-difference-between-two-dates-in-java/
 */
public class DateDifference {
    static String difference(LocalDateTime start, LocalDateTime end) {
        LocalDateTime cursor = start;
        long years = cursor.until(end, ChronoUnit.YEARS);
        cursor = cursor.plusYears(years);
        long months = cursor.until(end, ChronoUnit.MONTHS);
        cursor = cursor.plusMonths(months);
        long days = cursor.until(end, ChronoUnit.DAYS);
        cursor = cursor.plusDays(days);
        long hours = cursor.until(end, ChronoUnit.HOURS);
        cursor = cursor.plusHours(hours);
        long minutes = cursor.until(end, ChronoUnit.MINUTES);
        return "%dy %dm %dd %dh %dmin".formatted(years, months, days, hours, minutes);
    }
    static String tenureText(LocalDate joined, LocalDate today) {
        Period p = Period.between(joined, today);
        if (p.isNegative()) {
            return "starts on " + joined;
        }
        return p.getYears() + " years, " + p.getMonths() + " months";
    }
    static boolean overtime(ZonedDateTime start, ZonedDateTime end) {
        return Duration.between(start, end).compareTo(Duration.ofHours(8)) > 0;
    }
    public static void main(String[] args) throws Exception {
        {
            LocalDate joined = LocalDate.of(2019, 3, 18);
            LocalDate today = LocalDate.of(2026, 10, 10);
            Period tenure = Period.between(joined, today);                   // P7Y6M22D
            show("tenure", tenure);
            int years = tenure.getYears();                                   // 7
            show("years", years);
            long totalMonths = ChronoUnit.MONTHS.between(joined, today);     // 90
            show("totalMonths", totalMonths);
            long totalDays = ChronoUnit.DAYS.between(joined, today);         // 2763
            show("totalDays", totalDays);
            LocalDateTime shiftStart = LocalDateTime.of(2026, 10, 9, 22, 15);
            LocalDateTime shiftEnd = LocalDateTime.of(2026, 10, 10, 6, 40);
            Duration shift = Duration.between(shiftStart, shiftEnd);         // PT8H25M
            show("shift", shift);
            long shiftMinutes = shift.toMinutes();                           // 505
            show("shiftMinutes", shiftMinutes);
        }
        {
            LocalDateTime from = LocalDateTime.of(2024, 1, 31, 22, 0);
            LocalDateTime to = LocalDateTime.of(2024, 3, 2, 6, 30);
            Period calendar = Period.between(from.toLocalDate(), to.toLocalDate());   // P1M2D
            show("calendar", calendar);
            Duration exact = Duration.between(from, to);                              // PT728H30M
            show("exact", exact);
            long fullDays = ChronoUnit.DAYS.between(from, to);                        // 30
            show("fullDays", fullDays);
            long fullMonths = ChronoUnit.MONTHS.between(from, to);                    // 1
            show("fullMonths", fullMonths);
        }
        {
            LocalDate joined = LocalDate.of(2019, 3, 18);
            LocalDate today = LocalDate.of(2026, 10, 10);
            long days = ChronoUnit.DAYS.between(joined, today);       // 2763
            show("days", days);
            long weeks = ChronoUnit.WEEKS.between(joined, today);     // 394
            show("weeks", weeks);
            long months = ChronoUnit.MONTHS.between(joined, today);   // 90
            show("months", months);
            long years = joined.until(today, ChronoUnit.YEARS);       // 7
            show("years", years);
        }
        {
            LocalDateTime shiftStart = LocalDateTime.of(2026, 10, 9, 22, 15);
            LocalDateTime shiftEnd = LocalDateTime.of(2026, 10, 10, 6, 40);
            long hours = ChronoUnit.HOURS.between(shiftStart, shiftEnd);       // 8
            show("hours", hours);
            long minutes = ChronoUnit.MINUTES.between(shiftStart, shiftEnd);   // 505
            show("minutes", minutes);
            long millis = ChronoUnit.MILLIS.between(shiftStart, shiftEnd);     // 30300000
            show("millis", millis);
            try { long dateHours = ChronoUnit.HOURS.between(LocalDate.of(2026, 10, 9), LocalDate.of(2026, 10, 10)); show("dateHours", dateHours); } catch (Throwable _t) { System.out.println("dateHours -> " + _t); }
        }
        {
            Period tenure = Period.between(LocalDate.of(2019, 3, 18), LocalDate.of(2026, 10, 10));
            int years = tenure.getYears();               // 7
            show("years", years);
            int months = tenure.getMonths();             // 6
            show("months", months);
            int days = tenure.getDays();                 // 22
            show("days", days);
            long totalMonths = tenure.toTotalMonths();   // 90
            show("totalMonths", totalMonths);
            String text = "%d years, %d months and %d days".formatted(years, months, days);   // "7 years, 6 months and 22 days"
            show("text", text);
        }
        {
            Duration shift = Duration.between(LocalDateTime.of(2026, 10, 9, 22, 15), LocalDateTime.of(2026, 10, 10, 6, 40));
            long totalMinutes = shift.toMinutes();     // 505
            show("totalMinutes", totalMinutes);
            long totalHours = shift.toHours();         // 8
            show("totalHours", totalHours);
            int minutePart = shift.toMinutesPart();    // 25
            show("minutePart", minutePart);
            long seconds = shift.getSeconds();         // 30300
            show("seconds", seconds);
            int nanoPart = shift.getNano();            // 0
            show("nanoPart", nanoPart);
            String hhmm = "%02d:%02d".formatted(shift.toHours(), shift.toMinutesPart());   // "08:25"
            show("hhmm", hhmm);
        }
        {
            String exact = difference(LocalDateTime.of(2024, 1, 31, 22, 0), LocalDateTime.of(2024, 3, 2, 6, 30));   // "0y 1m 1d 8h 30min"
            show("exact", exact);
            String tenure = difference(LocalDateTime.of(2019, 3, 18, 9, 0), LocalDateTime.of(2026, 10, 10, 17, 45)); // "7y 6m 22d 8h 45min"
            show("tenure", tenure);
        }
        {
            ZoneId newYork = ZoneId.of("America/New_York");
            ZonedDateTime before = ZonedDateTime.of(2024, 3, 9, 12, 0, 0, 0, newYork);
            ZonedDateTime after = ZonedDateTime.of(2024, 3, 10, 12, 0, 0, 0, newYork);
            long days = ChronoUnit.DAYS.between(before, after);         // 1
            show("days", days);
            long hours = ChronoUnit.HOURS.between(before, after);       // 23
            show("hours", hours);
            Duration elapsed = Duration.between(before, after);         // PT23H
            show("elapsed", elapsed);
            long durationDays = elapsed.toDays();                       // 0
            show("durationDays", durationDays);
        }
        {
            Date legacyStart = Date.from(Instant.parse("2024-03-09T17:00:00Z"));
            Date legacyEnd = Date.from(Instant.parse("2024-04-15T16:00:00Z"));
            Duration exact = Duration.between(legacyStart.toInstant(), legacyEnd.toInstant());   // PT887H
            show("exact", exact);
            ZoneId zone = ZoneId.of("America/New_York");
            LocalDate startDay = LocalDate.ofInstant(legacyStart.toInstant(), zone);             // 2024-03-09
            show("startDay", startDay);
            LocalDate endDay = LocalDate.ofInstant(legacyEnd.toInstant(), zone);                 // 2024-04-15
            show("endDay", endDay);
            Period calendar = Period.between(startDay, endDay);                                  // P1M6D
            show("calendar", calendar);
            long millisDays = (legacyEnd.getTime() - legacyStart.getTime()) / 86_400_000L;       // 36
            show("millisDays", millisDays);
        }
        {
            String veteran = tenureText(LocalDate.of(2019, 3, 18), LocalDate.of(2026, 10, 10));   // "7 years, 6 months"
            show("veteran", veteran);
            String future = tenureText(LocalDate.of(2026, 11, 2), LocalDate.of(2026, 10, 10));     // "starts on 2026-11-02"
            show("future", future);
            ZoneId berlin = ZoneId.of("Europe/Berlin");
            boolean normal = overtime(ZonedDateTime.of(2026, 10, 24, 22, 0, 0, 0, berlin), ZonedDateTime.of(2026, 10, 25, 6, 0, 0, 0, berlin));   // true
            show("normal", normal);
            boolean summer = overtime(ZonedDateTime.of(2026, 7, 24, 22, 0, 0, 0, berlin), ZonedDateTime.of(2026, 7, 25, 6, 0, 0, 0, berlin));     // false
            show("summer", summer);
        }
        {
            LocalDate a = LocalDate.of(2019, 3, 18);
            LocalDate b = LocalDate.of(2026, 10, 10);
            Period reversed = Period.between(b, a);              // P-7Y-6M-23D
            show("reversed", reversed);
            Period negated = reversed.negated();                 // P7Y6M23D
            show("negated", negated);
            Period swapped = a.isBefore(b) ? Period.between(a, b) : Period.between(b, a);   // P7Y6M22D
            show("swapped", swapped);
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
