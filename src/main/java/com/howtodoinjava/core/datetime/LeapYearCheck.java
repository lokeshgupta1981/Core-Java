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
 * Examples for the tutorial "Check Leap Year in Java With Year.isLeap()".
 * https://howtodoinjava.com/java/date-time/check-leap-year/
 */
public class LeapYearCheck {
    static Optional<Boolean> isLeapYear(String text) {
        if (text == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Year.isLeap(Long.parseLong(text.strip())));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
    static boolean isLeap(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }
    static boolean isLeapWrong(int year) {
        if (year % 4 != 0) {
            return false;
        } else if (year % 100 == 0) {
            return false;               // also catches 2000
        } else if (year % 400 == 0) {
            return true;                // never reached
        }
        return true;
    }
    static LocalDate renewal(LocalDate start, int year) {
        MonthDay day = MonthDay.from(start);
        return day.isValidYear(year) ? day.atYear(year) : LocalDate.of(year, 3, 1);
    }
    public static void main(String[] args) throws Exception {
        {
            boolean y2024 = Year.isLeap(2024);                            // true
            show("y2024", y2024);
            boolean y2026 = Year.isLeap(2026);                            // false
            show("y2026", y2026);
            boolean y1900 = Year.isLeap(1900);                            // false
            show("y1900", y1900);
            boolean y2000 = Year.isLeap(2000);                            // true
            show("y2000", y2000);
            boolean fromDate = LocalDate.of(2028, 3, 1).isLeapYear();    // true
            show("fromDate", fromDate);
            int days = Year.of(2024).length();                            // 366
            show("days", days);
        }
        {
            boolean staticCheck = Year.isLeap(2028);                         // true
            show("staticCheck", staticCheck);
            boolean yearObject = Year.of(2028).isLeap();                      // true
            show("yearObject", yearObject);
            boolean yearMonth = YearMonth.of(2028, 2).isLeapYear();           // true
            show("yearMonth", yearMonth);
            boolean localDate = LocalDate.of(2027, 12, 31).isLeapYear();      // false
            show("localDate", localDate);
            boolean thisYear = Year.now(ZoneId.of("UTC")).isLeap();           // whether the current year is a leap year
            show("thisYear", thisYear);
        }
        {
            int daysIn2024 = LocalDate.of(2024, 6, 1).lengthOfYear();      // 366
            show("daysIn2024", daysIn2024);
            int february2024 = YearMonth.of(2024, 2).lengthOfMonth();         // 29
            show("february2024", february2024);
            int february2026 = YearMonth.of(2026, 2).lengthOfMonth();         // 28
            show("february2026", february2026);
            int februaryLeap = Month.FEBRUARY.length(Year.isLeap(2026));      // 28
            show("februaryLeap", februaryLeap);
        }
        {
            Optional<Boolean> typed = isLeapYear(" 2032 ");     // Optional[true]
            show("typed", typed);
            Optional<Boolean> garbage = isLeapYear("20x2");      // Optional.empty
            show("garbage", garbage);
        }
        {
            boolean leap2024 = isLeap(2024);    // true
            show("leap2024", leap2024);
            boolean leap2100 = isLeap(2100);    // false
            show("leap2100", leap2100);
            boolean leap2000 = isLeap(2000);    // true
            show("leap2000", leap2000);
        }
        {
            boolean wrong2000 = isLeapWrong(2000);     // false
            show("wrong2000", wrong2000);
            boolean right2000 = Year.isLeap(2000);      // true
            show("right2000", right2000);
        }
        {
            LocalDate start = LocalDate.of(2024, 2, 29);
            LocalDate nextYear = start.plusYears(1);                     // 2025-02-28
            show("nextYear", nextYear);
            LocalDate fourYears = start.plusYears(4);                    // 2028-02-29
            show("fourYears", fourYears);
            LocalDate sameDay2026 = start.withYear(2026);                // 2026-02-28
            show("sameDay2026", sameDay2026);
            boolean validIn2026 = MonthDay.of(2, 29).isValidYear(2026);  // false
            show("validIn2026", validIn2026);
            try { LocalDate invalid = LocalDate.of(2023, 2, 29); show("invalid", invalid); } catch (Throwable _t) { System.out.println("invalid -> " + _t); }
        }
        {
            LocalDate renewal2025 = renewal(LocalDate.of(2024, 2, 29), 2025);   // 2025-03-01
            show("renewal2025", renewal2025);
            LocalDate renewal2028 = renewal(LocalDate.of(2024, 2, 29), 2028);   // 2028-02-29
            show("renewal2028", renewal2028);
        }
        {
            List<Integer> leapYears = IntStream.rangeClosed(2020, 2040).filter(Year::isLeap).boxed().toList();   // [2020, 2024, 2028, 2032, 2036, 2040]
            show("leapYears", leapYears);
            long count = IntStream.rangeClosed(1901, 2000).filter(Year::isLeap).count();                         // 25
            show("count", count);
            int nextLeap = IntStream.iterate(2097, y -> y + 1).filter(Year::isLeap).findFirst().getAsInt();      // 2104
            show("nextLeap", nextLeap);
        }
        {
            boolean modern1500 = Year.isLeap(1500);                               // false
            show("modern1500", modern1500);
            boolean legacy1500 = new GregorianCalendar().isLeapYear(1500);        // true
            show("legacy1500", legacy1500);
            boolean legacy2000 = new GregorianCalendar().isLeapYear(2000);        // true
            show("legacy2000", legacy2000);
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
