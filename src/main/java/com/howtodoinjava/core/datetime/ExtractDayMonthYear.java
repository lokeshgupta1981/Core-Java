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
 * Examples for the tutorial "Get Year, Month and Day From a Date in Java".
 * https://howtodoinjava.com/java/date-time/get-year-month-day-from-date/
 */
public class ExtractDayMonthYear {

    public static void main(String[] args) throws Exception {
        {
            LocalDate shipped = LocalDate.of(2026, 10, 10);
            int year = shipped.getYear();                   // 2026
            show("year", year);
            int month = shipped.getMonthValue();            // 10
            show("month", month);
            Month monthEnum = shipped.getMonth();           // OCTOBER
            show("monthEnum", monthEnum);
            int day = shipped.getDayOfMonth();              // 10
            show("day", day);
            DayOfWeek weekday = shipped.getDayOfWeek();     // SATURDAY
            show("weekday", weekday);
        }
        {
            LocalDate shipped = LocalDate.of(2026, 10, 10);
            int dayOfYear = shipped.getDayOfYear();        // 283
            show("dayOfYear", dayOfYear);
            int daysInMonth = shipped.lengthOfMonth();     // 31
            show("daysInMonth", daysInMonth);
            boolean leap = shipped.isLeapYear();           // false
            show("leap", leap);
            int weekdayNumber = shipped.getDayOfWeek().getValue();   // 6, Monday is 1
            show("weekdayNumber", weekdayNumber);
        }
        {
            Month october = LocalDate.of(2026, 10, 10).getMonth();
            int number = october.getValue();                                    // 10
            show("number", number);
            String full = october.getDisplayName(TextStyle.FULL, Locale.US);     // "October"
            show("full", full);
            String brief = october.getDisplayName(TextStyle.SHORT, Locale.US);   // "Oct"
            show("brief", brief);
            String german = october.getDisplayName(TextStyle.FULL, Locale.GERMAN);   // "Oktober"
            show("german", german);
            try { Month invalid = Month.of(13); show("invalid", invalid); } catch (Throwable _t) { System.out.println("invalid -> " + _t); }
        }
        {
            Instant moment = Instant.parse("2026-12-31T20:00:00Z");
            int yearInNewYork = moment.atZone(ZoneId.of("America/New_York")).getYear();   // 2026
            show("yearInNewYork", yearInNewYork);
            int yearInTokyo = moment.atZone(ZoneId.of("Asia/Tokyo")).getYear();           // 2027
            show("yearInTokyo", yearInTokyo);
            int dayInTokyo = moment.atZone(ZoneId.of("Asia/Tokyo")).getDayOfMonth();      // 1
            show("dayInTokyo", dayInTokyo);
            LocalDateTime local = LocalDateTime.of(2026, 10, 10, 18, 45);
            int hour = local.getHour();                                                   // 18
            show("hour", hour);
        }
        {
            LocalDate date = LocalDate.of(2026, 10, 10);
            int y = date.get(ChronoField.YEAR);                         // 2026
            show("y", y);
            int m = date.get(ChronoField.MONTH_OF_YEAR);                // 10
            show("m", m);
            int d = date.get(ChronoField.DAY_OF_MONTH);                 // 10
            show("d", d);
            long epochDay = date.getLong(ChronoField.EPOCH_DAY);        // 20736
            show("epochDay", epochDay);
            int week = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);     // 41
            show("week", week);
        }
        {
            Instant fixed = Instant.parse("2026-10-10T08:00:00Z");
            boolean hasYear = fixed.isSupported(ChronoField.YEAR);      // false
            show("hasYear", hasYear);
            try { int fails = fixed.get(ChronoField.YEAR); show("fails", fails); } catch (Throwable _t) { System.out.println("fails -> " + _t); }
        }
        {
            Date legacy = Date.from(Instant.parse("2026-10-09T20:00:00Z"));
            LocalDate converted = LocalDate.ofInstant(legacy.toInstant(), ZoneId.of("Asia/Kolkata"));   // 2026-10-10
            show("converted", converted);
            int legacyYear = converted.getYear();                         // 2026
            show("legacyYear", legacyYear);
            int legacyMonth = converted.getMonthValue();                  // 10
            show("legacyMonth", legacyMonth);
            int legacyDay = converted.getDayOfMonth();                    // 10
            show("legacyDay", legacyDay);
        }
        {
            LocalDate parsed = LocalDate.parse("10/10/2026", DateTimeFormatter.ofPattern("dd/MM/uuuu"));
            int parsedMonth = parsed.getMonthValue();                     // 10
            show("parsedMonth", parsedMonth);
            try { LocalDate wrong = LocalDate.parse("2026-02-30"); show("wrong", wrong); } catch (Throwable _t) { System.out.println("wrong -> " + _t); }
        }
        {
            LocalDate invoiceDate = LocalDate.of(2026, 10, 10);
            YearMonth period = YearMonth.from(invoiceDate);                              // 2026-10
            show("period", period);
            String folder = "invoices/%d/%02d".formatted(period.getYear(), period.getMonthValue());   // "invoices/2026/10"
            show("folder", folder);
            LocalDate periodEnd = period.atEndOfMonth();                                 // 2026-10-31
            show("periodEnd", periodEnd);
            MonthDay birthday = MonthDay.from(LocalDate.of(1992, 2, 29));                // --02-29
            show("birthday", birthday);
            boolean validIn2027 = birthday.isValidYear(2027);                            // false
            show("validIn2027", validIn2027);
            LocalDate greetOn = birthday.atYear(2027);                                   // 2027-02-28
            show("greetOn", greetOn);
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
