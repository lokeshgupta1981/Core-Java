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
 * Examples for the tutorial "Check if a Date Is a Weekend in Java".
 * https://howtodoinjava.com/java/date-time/check-weekend/
 */
public class CheckWeekend {
    static boolean isWeekend(LocalDate date) {
        return WEEKEND.contains(date.getDayOfWeek());
    }

    static final Set<DayOfWeek> WEEKEND = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
    static boolean isWeekend(LocalDate date, Set<DayOfWeek> weekendDays) {
        return weekendDays.contains(date.getDayOfWeek());
    }
    static boolean isWeekendAt(TemporalAccessor value) {
        return WEEKEND.contains(DayOfWeek.from(value));
    }
    static long weekendDays(LocalDate start, LocalDate endExclusive) {
        return start.datesUntil(endExclusive).filter(d -> isWeekend(d)).count();
    }
    public static void main(String[] args) throws Exception {
        {
            LocalDate saturday = LocalDate.of(2026, 10, 17);
            DayOfWeek day = saturday.getDayOfWeek();                                     // SATURDAY
            show("day", day);
            boolean isWeekend = day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;    // true
            show("isWeekend", isWeekend);
            DayOfWeek friday = LocalDate.of(2026, 10, 16).getDayOfWeek();                // FRIDAY
            show("friday", friday);
            boolean fridayIsWeekend = friday == DayOfWeek.SATURDAY || friday == DayOfWeek.SUNDAY;   // false
            show("fridayIsWeekend", fridayIsWeekend);
        }
        {
            boolean saturday = isWeekend(LocalDate.of(2026, 10, 17));   // true
            show("saturday", saturday);
            boolean sunday = isWeekend(LocalDate.of(2026, 10, 18));     // true
            show("sunday", sunday);
            boolean monday = isWeekend(LocalDate.of(2026, 10, 19));     // false
            show("monday", monday);
        }
        {
            Set<DayOfWeek> fridaySaturday = EnumSet.of(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY);
            LocalDate friday = LocalDate.of(2026, 10, 16);
            boolean weekendThere = isWeekend(friday, fridaySaturday);   // true
            show("weekendThere", weekendThere);
            boolean weekendHere = isWeekend(friday, WEEKEND);           // false
            show("weekendHere", weekendHere);
        }
        {
            boolean saturdayMorning = isWeekendAt(LocalDateTime.of(2026, 10, 17, 9, 0));   // true
            show("saturdayMorning", saturdayMorning);
            Instant orderTime = Instant.parse("2026-10-17T03:30:00Z");
            boolean inUtc = isWeekendAt(orderTime.atZone(ZoneOffset.UTC));                  // true
            show("inUtc", inUtc);
            boolean inNewYork = isWeekendAt(orderTime.atZone(ZoneId.of("America/New_York"))); // false
            show("inNewYork", inNewYork);
            try { boolean rawInstant = isWeekendAt(orderTime); show("rawInstant", rawInstant); } catch (Throwable _t) { System.out.println("rawInstant -> " + _t); }
        }
        {
            LocalDate pickUp = LocalDate.of(2026, 10, 15);
            LocalDate dropOff = LocalDate.of(2026, 10, 20);
            long totalDays = ChronoUnit.DAYS.between(pickUp, dropOff);    // 5
            show("totalDays", totalDays);
            long weekend = weekendDays(pickUp, dropOff);                  // 2
            show("weekend", weekend);
            long price = weekend * 55 + (totalDays - weekend) * 40;       // 230
            show("price", price);
        }
        {
            LocalDate thursday = LocalDate.of(2026, 10, 15);
            LocalDate nextSaturday = thursday.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));   // 2026-10-17
            show("nextSaturday", nextSaturday);
            LocalDate sundayDue = LocalDate.of(2026, 10, 18);
            LocalDate nextWorkday = sundayDue.with(TemporalAdjusters.next(DayOfWeek.MONDAY));           // 2026-10-19
            show("nextWorkday", nextWorkday);
        }
        {
            Date legacy = Date.from(Instant.parse("2026-10-17T10:00:00Z"));
            LocalDate converted = legacy.toInstant().atZone(ZoneId.of("UTC")).toLocalDate();   // 2026-10-17
            show("converted", converted);
            boolean legacyWeekend = isWeekend(converted);                                       // true
            show("legacyWeekend", legacyWeekend);
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
