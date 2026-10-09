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
 * Examples for the tutorial "Calculate Business Days Between Two Dates in Java".
 * https://howtodoinjava.com/java/date-time/calculate-business-days/
 */
public class BusinessDaysExamples {
    static boolean isBusinessDay(LocalDate date, Set<LocalDate> holidays) {
        DayOfWeek day = date.getDayOfWeek();
        return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY && !holidays.contains(date);
    }
    static long countBusinessDays(LocalDate start, LocalDate endInclusive, Set<LocalDate> holidays) {
        return start.datesUntil(endInclusive.plusDays(1))
                .filter(date -> isBusinessDay(date, holidays))
                .count();
    }
    static List<LocalDate> businessDaysBetween(LocalDate start, LocalDate endInclusive, Set<LocalDate> holidays) {
        return start.datesUntil(endInclusive.plusDays(1))
                .filter(date -> isBusinessDay(date, holidays))
                .toList();
    }
    static long countWeekdays(LocalDate start, LocalDate endExclusive) {
        long days = ChronoUnit.DAYS.between(start, endExclusive);
        if (days < 0) {
            throw new IllegalArgumentException(start + " is after " + endExclusive);
        }
        long count = days / 7 * 5;
        for (LocalDate d = start.plusDays(days / 7 * 7); d.isBefore(endExclusive); d = d.plusDays(1)) {
            if (isBusinessDay(d, Set.of())) {
                count++;
            }
        }
        return count;
    }
    static long countBusinessDaysFast(LocalDate start, LocalDate endExclusive, Set<LocalDate> holidays) {
        long holidaysOnWeekdays = holidays.stream()
                .filter(h -> !h.isBefore(start) && h.isBefore(endExclusive) && isBusinessDay(h, Set.of()))
                .count();
        return countWeekdays(start, endExclusive) - holidaysOnWeekdays;
    }
    public static void main(String[] args) throws Exception {
        {
            LocalDate first = LocalDate.of(2026, 10, 1);
            LocalDate last = LocalDate.of(2026, 10, 31);
            long workdays = first.datesUntil(last.plusDays(1)).filter(d -> isBusinessDay(d, Set.of())).count();   // 22
            show("workdays", workdays);
            Set<LocalDate> holidays = Set.of(LocalDate.of(2026, 10, 12));
            long afterHoliday = first.datesUntil(last.plusDays(1)).filter(d -> isBusinessDay(d, holidays)).count(); // 21
            show("afterHoliday", afterHoliday);
        }
        {
            LocalDate sprintStart = LocalDate.of(2026, 10, 5);
            LocalDate sprintEnd = LocalDate.of(2026, 10, 16);
            Set<LocalDate> octoberHoliday = Set.of(LocalDate.of(2026, 10, 12));
            long exclusive = sprintStart.datesUntil(sprintEnd).filter(d -> isBusinessDay(d, octoberHoliday)).count();   // 8
            show("exclusive", exclusive);
            long inclusive = countBusinessDays(sprintStart, sprintEnd, octoberHoliday);                                // 9
            show("inclusive", inclusive);
            long bothExcluded = sprintStart.plusDays(1).datesUntil(sprintEnd).filter(d -> isBusinessDay(d, octoberHoliday)).count();   // 7
            show("bothExcluded", bothExcluded);
            long calendarDays = ChronoUnit.DAYS.between(sprintStart, sprintEnd);                                       // 11
            show("calendarDays", calendarDays);
        }
        {
            try { long reversed = LocalDate.of(2026, 10, 16).datesUntil(LocalDate.of(2026, 10, 5)).count(); show("reversed", reversed); } catch (Throwable _t) { System.out.println("reversed -> " + _t); }
        }
        {
            List<LocalDate> week = businessDaysBetween(LocalDate.of(2026, 10, 9), LocalDate.of(2026, 10, 14), Set.of(LocalDate.of(2026, 10, 12)));   // [2026-10-09, 2026-10-13, 2026-10-14]
            show("week", week);
            int size = week.size();                                                    // 3
            show("size", size);
        }
        {
            LocalDate from = LocalDate.of(2026, 1, 1);
            long tenYears = countWeekdays(from, LocalDate.of(2036, 1, 1));                                 // 2608
            show("tenYears", tenYears);
            long fastOctober = countBusinessDaysFast(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 11, 1), Set.of(LocalDate.of(2026, 10, 12)));   // 21
            show("fastOctober", fastOctober);
            boolean matchesStream = IntStream.range(0, 400).allMatch(n -> countWeekdays(from, from.plusDays(n)) == from.datesUntil(from.plusDays(n)).filter(d -> isBusinessDay(d, Set.of())).count());   // true
            show("matchesStream", matchesStream);
        }
        {
            LocalDate leaveFrom = LocalDate.of(2026, 10, 9);
            LocalDate leaveTo = LocalDate.of(2026, 10, 13);
            Set<LocalDate> companyHolidays = Set.of(LocalDate.of(2026, 10, 12));
            long leaveDays = countBusinessDays(leaveFrom, leaveTo, companyHolidays);               // 2
            show("leaveDays", leaveDays);
            List<LocalDate> absentOn = businessDaysBetween(leaveFrom, leaveTo, companyHolidays);   // [2026-10-09, 2026-10-13]
            show("absentOn", absentOn);
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
