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
 * Examples for the tutorial "First and Last Day of Week, Month or Year in Java".
 * https://howtodoinjava.com/java/date-time/first-last-day-of-week-month-year/
 */
public class FirstAndLastDay {

    public static void main(String[] args) throws Exception {
        {
            LocalDate today = LocalDate.of(2024, 4, 26);                                                 // Friday
            show("today", today);

            LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));   // 2024-04-22
            show("firstDayOfWeek", firstDayOfWeek);
            LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));        // 2024-04-28
            show("lastDayOfWeek", lastDayOfWeek);

            LocalDate usWeekStart = today.with(WeekFields.of(Locale.US).dayOfWeek(), 1);                 // 2024-04-21
            show("usWeekStart", usWeekStart);
            LocalDate usWeekEnd = today.with(WeekFields.of(Locale.US).dayOfWeek(), 7);                   // 2024-04-27
            show("usWeekEnd", usWeekEnd);

            LocalDate firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());                 // 2024-04-01
            show("firstDayOfMonth", firstDayOfMonth);
            LocalDate lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());                   // 2024-04-30
            show("lastDayOfMonth", lastDayOfMonth);

            LocalDate firstDayOfYear = today.with(TemporalAdjusters.firstDayOfYear());                   // 2024-01-01
            show("firstDayOfYear", firstDayOfYear);
            LocalDate lastDayOfYear = today.with(TemporalAdjusters.lastDayOfYear());                     // 2024-12-31
            show("lastDayOfYear", lastDayOfYear);
        }
        {
            LocalDate today = LocalDate.of(2024, 4, 26);
            LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));   // 2024-04-22
            show("firstDayOfWeek", firstDayOfWeek);
            LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));        // 2024-04-28
            show("lastDayOfWeek", lastDayOfWeek);

            LocalDate monday = LocalDate.of(2024, 4, 22);
            LocalDate sameMonday = monday.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));      // 2024-04-22
            show("sameMonday", sameMonday);
            LocalDate mondayBefore = monday.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));          // 2024-04-15
            show("mondayBefore", mondayBefore);

            LocalDate newYear = LocalDate.of(2025, 1, 1);
            LocalDate weekStart = newYear.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));      // 2024-12-30
            show("weekStart", weekStart);
        }
        {
            LocalDate today = LocalDate.of(2024, 4, 26);
            WeekFields us = WeekFields.of(Locale.US);
            DayOfWeek usFirstDay = us.getFirstDayOfWeek();                                         // SUNDAY
            show("usFirstDay", usFirstDay);
            LocalDate usWeekStart = today.with(us.dayOfWeek(), 1);                                 // 2024-04-21
            show("usWeekStart", usWeekStart);
            LocalDate usWeekEnd = today.with(us.dayOfWeek(), 7);                                   // 2024-04-27
            show("usWeekEnd", usWeekEnd);
            LocalDate germanWeekStart = today.with(WeekFields.of(Locale.GERMANY).dayOfWeek(), 1);  // 2024-04-22
            show("germanWeekStart", germanWeekStart);
            LocalDate isoWeekStart = today.with(WeekFields.ISO.dayOfWeek(), 1);                    // 2024-04-22
            show("isoWeekStart", isoWeekStart);
        }
        {
            Locale usMondayWeek = Locale.forLanguageTag("en-US-u-fw-mon");
            DayOfWeek firstDay = WeekFields.of(usMondayWeek).getFirstDayOfWeek();   // MONDAY
            show("firstDay", firstDay);
        }
        {
            LocalDate today = LocalDate.of(2024, 4, 26);
            LocalDate firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());                   // 2024-04-01
            show("firstDayOfMonth", firstDayOfMonth);
            LocalDate lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());                     // 2024-04-30
            show("lastDayOfMonth", lastDayOfMonth);
            LocalDate leapFebruary = LocalDate.of(2024, 2, 10).with(TemporalAdjusters.lastDayOfMonth());   // 2024-02-29
            show("leapFebruary", leapFebruary);
            LocalDate normalFebruary = LocalDate.of(2023, 2, 10).with(TemporalAdjusters.lastDayOfMonth()); // 2023-02-28
            show("normalFebruary", normalFebruary);
        }
        {
            LocalDate today = LocalDate.of(2024, 4, 26);
            LocalDate firstDay = today.withDayOfMonth(1);                         // 2024-04-01
            show("firstDay", firstDay);
            LocalDate lastDay = today.withDayOfMonth(today.lengthOfMonth());      // 2024-04-30
            show("lastDay", lastDay);
        }
        {
            YearMonth february = YearMonth.of(2024, 2);
            LocalDate first = february.atDay(1);                                              // 2024-02-01
            show("first", first);
            LocalDate last = february.atEndOfMonth();                                         // 2024-02-29
            show("last", last);
            int days = february.lengthOfMonth();                                              // 29
            show("days", days);
            LocalDate fromDate = YearMonth.from(LocalDate.of(2024, 4, 26)).atEndOfMonth();    // 2024-04-30
            show("fromDate", fromDate);
        }
        {
            LocalDate today = LocalDate.of(2024, 4, 26);
            LocalDate previousMonthStart = today.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());   // 2024-03-01
            show("previousMonthStart", previousMonthStart);
            LocalDate previousMonthEnd = today.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());      // 2024-03-31
            show("previousMonthEnd", previousMonthEnd);
            LocalDate nextMonthStart = today.with(TemporalAdjusters.firstDayOfNextMonth());                  // 2024-05-01
            show("nextMonthStart", nextMonthStart);
            LocalDate firstMonday = today.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));            // 2024-04-01
            show("firstMonday", firstMonday);
            LocalDate lastFriday = today.with(TemporalAdjusters.lastInMonth(DayOfWeek.FRIDAY));              // 2024-04-26
            show("lastFriday", lastFriday);
            LocalDate secondTuesday = today.with(TemporalAdjusters.dayOfWeekInMonth(2, DayOfWeek.TUESDAY));  // 2024-04-09
            show("secondTuesday", secondTuesday);
        }
        {
            LocalDate today = LocalDate.of(2024, 4, 26);
            LocalDate firstDayOfYear = today.with(TemporalAdjusters.firstDayOfYear());       // 2024-01-01
            show("firstDayOfYear", firstDayOfYear);
            LocalDate lastDayOfYear = today.with(TemporalAdjusters.lastDayOfYear());         // 2024-12-31
            show("lastDayOfYear", lastDayOfYear);
            LocalDate nextYearStart = today.with(TemporalAdjusters.firstDayOfNextYear());    // 2025-01-01
            show("nextYearStart", nextYearStart);
            LocalDate yearStart = Year.of(2024).atDay(1);                                    // 2024-01-01
            show("yearStart", yearStart);
            LocalDate yearEnd = today.withDayOfYear(today.lengthOfYear());                   // 2024-12-31
            show("yearEnd", yearEnd);
        }
        {
            LocalDate today = LocalDate.of(2024, 4, 26);
            int quarter = today.get(IsoFields.QUARTER_OF_YEAR);                                                         // 2
            show("quarter", quarter);
            LocalDate quarterStart = today.with(IsoFields.DAY_OF_QUARTER, 1);                                           // 2024-04-01
            show("quarterStart", quarterStart);
            LocalDate quarterEnd = today.with(IsoFields.DAY_OF_QUARTER, today.range(IsoFields.DAY_OF_QUARTER).getMaximum());   // 2024-06-30
            show("quarterEnd", quarterEnd);
        }
        {
            LocalDate today = LocalDate.of(2024, 4, 26);
            LocalDateTime start = today.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();                 // 2024-04-01T00:00
            show("start", start);
            LocalDateTime end = today.with(TemporalAdjusters.firstDayOfNextMonth()).atStartOfDay();               // 2024-05-01T00:00
            show("end", end);
            LocalDateTime lastMoment = today.with(TemporalAdjusters.lastDayOfMonth()).atTime(LocalTime.MAX);      // 2024-04-30T23:59:59.999999999
            show("lastMoment", lastMoment);
        }
        {
            LocalDate lastDayOfThisMonth = LocalDate.now(ZoneId.of("UTC")).with(TemporalAdjusters.lastDayOfMonth());   // last day of the current month in UTC
            show("lastDayOfThisMonth", lastDayOfThisMonth);
        }
        {
            Date legacy = Date.from(Instant.parse("2024-04-26T10:00:00Z"));
            LocalDate date = legacy.toInstant().atZone(ZoneId.of("UTC")).toLocalDate();       // 2024-04-26
            show("date", date);
            LocalDate monthEnd = date.with(TemporalAdjusters.lastDayOfMonth());               // 2024-04-30
            show("monthEnd", monthEnd);
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
