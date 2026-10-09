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
 * Examples for the tutorial "Calculate Number of Weeks Between Two Dates in Java".
 * https://howtodoinjava.com/java/date-time/calculate-weeks-between-two-dates/
 */
public class WeeksBetweenDates {
    static long calendarWeeksBetween(LocalDate start, LocalDate end, DayOfWeek firstDay) {
        LocalDate startWeek = start.with(TemporalAdjusters.previousOrSame(firstDay));
        LocalDate endWeek = end.with(TemporalAdjusters.previousOrSame(firstDay));
        return ChronoUnit.WEEKS.between(startWeek, endWeek);
    }
    public static void main(String[] args) throws Exception {
        {
            LocalDate kickoff = LocalDate.of(2026, 1, 5);
            LocalDate release = LocalDate.of(2026, 3, 20);
            long weeks = ChronoUnit.WEEKS.between(kickoff, release);   // 10
            show("weeks", weeks);
            long days = ChronoUnit.DAYS.between(kickoff, release);     // 74
            show("days", days);
            long leftoverDays = days % 7;                               // 4
            show("leftoverDays", leftoverDays);
            long startedWeeks = Math.ceilDiv(days, 7);                  // 11
            show("startedWeeks", startedWeeks);
            double exactWeeks = days / 7.0;                             // 10.571428571428571
            show("exactWeeks", exactWeeks);
        }
        {
            LocalDate start = LocalDate.of(2026, 1, 5);
            long sixDays = ChronoUnit.WEEKS.between(start, start.plusDays(6));     // 0
            show("sixDays", sixDays);
            long thirteenDays = ChronoUnit.WEEKS.between(start, start.plusDays(13)); // 1
            show("thirteenDays", thirteenDays);
            long backwards = ChronoUnit.WEEKS.between(LocalDate.of(2026, 3, 20), start);   // -10
            show("backwards", backwards);
            long viaUntil = start.until(LocalDate.of(2026, 3, 20), ChronoUnit.WEEKS);      // 10
            show("viaUntil", viaUntil);
        }
        {
            long totalDays = ChronoUnit.DAYS.between(LocalDate.of(2026, 1, 5), LocalDate.of(2026, 3, 20));   // 74
            show("totalDays", totalDays);
            String label = (totalDays / 7) + " weeks and " + (totalDays % 7) + " days";   // "10 weeks and 4 days"
            show("label", label);
        }
        {
            Period span = Period.between(LocalDate.of(2026, 1, 5), LocalDate.of(2026, 3, 20));   // P2M15D
            show("span", span);
            Period threeWeeks = Period.ofWeeks(3);                                                // P21D
            show("threeWeeks", threeWeeks);
        }
        {
            LocalDate pickup = LocalDate.of(2026, 7, 1);
            LocalDate dropOff = LocalDate.of(2026, 7, 16);
            long rentalDays = ChronoUnit.DAYS.between(pickup, dropOff);    // 15
            show("rentalDays", rentalDays);
            long fullWeeks = ChronoUnit.WEEKS.between(pickup, dropOff);    // 2
            show("fullWeeks", fullWeeks);
            long billedWeeks = Math.ceilDiv(rentalDays, 7);                // 3
            show("billedWeeks", billedWeeks);
        }
        {
            LocalDate sunday = LocalDate.of(2026, 1, 4);
            LocalDate monday = LocalDate.of(2026, 1, 5);
            long elapsed = ChronoUnit.WEEKS.between(sunday, monday);                        // 0
            show("elapsed", elapsed);
            long isoWeeks = calendarWeeksBetween(sunday, monday, DayOfWeek.MONDAY);         // 1
            show("isoWeeks", isoWeeks);
            long usWeeks = calendarWeeksBetween(sunday, monday, DayOfWeek.SUNDAY);          // 0
            show("usWeeks", usWeeks);
        }
        {
            int lastWeekOf2026 = LocalDate.of(2026, 12, 31).get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);   // 53
            show("lastWeekOf2026", lastWeekOf2026);
            int newYearsDay = LocalDate.of(2027, 1, 1).get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);       // 53
            show("newYearsDay", newYearsDay);
        }
        {
            LocalDateTime from = LocalDateTime.of(2026, 1, 1, 10, 0);
            long almostAWeek = ChronoUnit.WEEKS.between(from, LocalDateTime.of(2026, 1, 8, 9, 0));   // 0
            show("almostAWeek", almostAWeek);
            long fullWeek = ChronoUnit.WEEKS.between(from, LocalDateTime.of(2026, 1, 8, 10, 0));     // 1
            show("fullWeek", fullWeek);
        }
        {
            ZoneId newYork = ZoneId.of("America/New_York");
            ZonedDateTime weekStart = ZonedDateTime.of(2026, 3, 2, 0, 0, 0, 0, newYork);
            ZonedDateTime weekEnd = ZonedDateTime.of(2026, 3, 9, 0, 0, 0, 0, newYork);
            long zonedWeeks = ChronoUnit.WEEKS.between(weekStart, weekEnd);   // 1
            show("zonedWeeks", zonedWeeks);
            long hours = ChronoUnit.HOURS.between(weekStart, weekEnd);        // 167
            show("hours", hours);
        }
        {
            try { long instantWeeks = ChronoUnit.WEEKS.between(Instant.parse("2026-03-02T05:00:00Z"), Instant.parse("2026-03-09T04:00:00Z")); show("instantWeeks", instantWeeks); } catch (Throwable _t) { System.out.println("instantWeeks -> " + _t); }
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
