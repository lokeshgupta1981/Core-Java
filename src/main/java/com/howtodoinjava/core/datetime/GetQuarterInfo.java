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
 * Examples for the tutorial "Get the Quarter From a Date in Java, Start and End Dates".
 * https://howtodoinjava.com/java/date-time/current-quarter-start-end/
 */
public class GetQuarterInfo {
    static LocalDate quarterStart(int year, int quarter) {
        return Year.of(year).atDay(1).with(IsoFields.QUARTER_OF_YEAR, quarter);
    }

    static LocalDate quarterEnd(int year, int quarter) {
        return quarterStart(year, quarter).plusMonths(3).minusDays(1);
    }
    static long quarterIndex(LocalDate date) {
        return date.getYear() * 4L + date.get(IsoFields.QUARTER_OF_YEAR) - 1;
    }
    static int fiscalQuarter(LocalDate date, Month fiscalYearStart) {
        int shifted = (date.getMonthValue() - fiscalYearStart.getValue() + 12) % 12;
        return shifted / 3 + 1;
    }

    static LocalDate fiscalQuarterStart(LocalDate date, Month fiscalYearStart) {
        int shifted = (date.getMonthValue() - fiscalYearStart.getValue() + 12) % 12;
        return date.withDayOfMonth(1).minusMonths(shifted % 3);
    }
    public static void main(String[] args) throws Exception {
        {
            LocalDate date = LocalDate.of(2026, 8, 20);
            int quarter = date.get(IsoFields.QUARTER_OF_YEAR);                                                  // 3
            show("quarter", quarter);
            LocalDate quarterStart = date.with(IsoFields.DAY_OF_QUARTER, 1);                                    // 2026-07-01
            show("quarterStart", quarterStart);
            LocalDate quarterEnd = date.with(IsoFields.DAY_OF_QUARTER, date.range(IsoFields.DAY_OF_QUARTER).getMaximum());   // 2026-09-30
            show("quarterEnd", quarterEnd);
            String label = date.format(DateTimeFormatter.ofPattern("QQQ uuuu", Locale.US));                     // "Q3 2026"
            show("label", label);
        }
        {
            int fromDate = LocalDate.of(2026, 8, 20).get(IsoFields.QUARTER_OF_YEAR);                    // 3
            show("fromDate", fromDate);
            int fromDateTime = LocalDateTime.of(2026, 2, 10, 9, 0).get(IsoFields.QUARTER_OF_YEAR);         // 1
            show("fromDateTime", fromDateTime);
            ZonedDateTime order = ZonedDateTime.of(2027, 1, 1, 0, 30, 0, 0, ZoneId.of("Asia/Kolkata"));
            int inIndia = order.get(IsoFields.QUARTER_OF_YEAR);                                             // 1
            show("inIndia", inIndia);
            int inUtc = order.withZoneSameInstant(ZoneOffset.UTC).get(IsoFields.QUARTER_OF_YEAR);           // 4
            show("inUtc", inUtc);
            int current = LocalDate.now(ZoneId.of("UTC")).get(IsoFields.QUARTER_OF_YEAR);                  // the current quarter in UTC
            show("current", current);
            int byFormula = (LocalDate.of(2026, 8, 20).getMonthValue() - 1) / 3 + 1;                        // 3
            show("byFormula", byFormula);
        }
        {
            LocalDate date = LocalDate.of(2026, 8, 20);
            int dayInQuarter = date.get(IsoFields.DAY_OF_QUARTER);                    // 51
            show("dayInQuarter", dayInQuarter);
            ValueRange days = date.range(IsoFields.DAY_OF_QUARTER);                   // 1 - 92
            show("days", days);
            LocalDate first = date.with(IsoFields.DAY_OF_QUARTER, 1);                 // 2026-07-01
            show("first", first);
            LocalDate last = date.with(IsoFields.DAY_OF_QUARTER, days.getMaximum());  // 2026-09-30
            show("last", last);
            LocalDate alsoLast = first.plusMonths(3).minusDays(1);                    // 2026-09-30
            show("alsoLast", alsoLast);
        }
        {
            LocalDate q1Start = quarterStart(2027, 1);        // 2027-01-01
            show("q1Start", q1Start);
            LocalDate q1End = quarterEnd(2027, 1);            // 2027-03-31
            show("q1End", q1End);
            LocalDate q4End = quarterEnd(2026, 4);            // 2026-12-31
            show("q4End", q4End);
            try { LocalDate invalid = quarterStart(2026, 5); show("invalid", invalid); } catch (Throwable _t) { System.out.println("invalid -> " + _t); }
        }
        {
            LocalDate date = LocalDate.of(2026, 8, 20);
            LocalDate previousStart = date.minus(1, IsoFields.QUARTER_YEARS).with(IsoFields.DAY_OF_QUARTER, 1);   // 2026-04-01
            show("previousStart", previousStart);
            LocalDate nextStart = date.plus(1, IsoFields.QUARTER_YEARS).with(IsoFields.DAY_OF_QUARTER, 1);        // 2026-10-01
            show("nextStart", nextStart);
        }
        {
            LocalDate date = LocalDate.of(2026, 8, 20);
            String number = date.format(DateTimeFormatter.ofPattern("Q"));                           // "3"
            show("number", number);
            String shortLabel = date.format(DateTimeFormatter.ofPattern("QQQ uuuu", Locale.US));     // "Q3 2026"
            show("shortLabel", shortLabel);
            String fullLabel = date.format(DateTimeFormatter.ofPattern("QQQQ", Locale.US));          // "3rd quarter"
            show("fullLabel", fullLabel);
            String german = date.format(DateTimeFormatter.ofPattern("QQQQ", Locale.GERMAN));         // "3. Quartal"
            show("german", german);
            String isoStyle = date.format(DateTimeFormatter.ofPattern("uuuu-'Q'Q"));                 // "2026-Q3"
            show("isoStyle", isoStyle);
        }
        {
            DateTimeFormatter quarterLabel = new DateTimeFormatterBuilder()
                    .appendPattern("uuuu-'Q'Q")
                    .parseDefaulting(IsoFields.DAY_OF_QUARTER, 1)
                    .toFormatter();
            LocalDate parsed = LocalDate.parse("2026-Q3", quarterLabel);     // 2026-07-01
            show("parsed", parsed);
        }
        {
            long sameYearDay = IsoFields.QUARTER_YEARS.between(LocalDate.of(2025, 8, 20), LocalDate.of(2026, 8, 20));   // 4
            show("sameYearDay", sameYearDay);
            long partial = IsoFields.QUARTER_YEARS.between(LocalDate.of(2026, 1, 15), LocalDate.of(2026, 10, 14));       // 2
            show("partial", partial);
            long acrossBoundary = IsoFields.QUARTER_YEARS.between(LocalDate.of(2026, 3, 31), LocalDate.of(2026, 4, 1)); // 0
            show("acrossBoundary", acrossBoundary);
        }
        {
            long calendarQuarters = quarterIndex(LocalDate.of(2026, 4, 1)) - quarterIndex(LocalDate.of(2026, 3, 31));   // 1
            show("calendarQuarters", calendarQuarters);
        }
        {
            LocalDate invoiceDate = LocalDate.of(2026, 8, 20);
            int indiaQuarter = fiscalQuarter(invoiceDate, Month.APRIL);                    // 2
            show("indiaQuarter", indiaQuarter);
            int usGovQuarter = fiscalQuarter(invoiceDate, Month.OCTOBER);                  // 4
            show("usGovQuarter", usGovQuarter);
            LocalDate indiaStart = fiscalQuarterStart(invoiceDate, Month.APRIL);           // 2026-07-01
            show("indiaStart", indiaStart);
            LocalDate fiscalEnd = indiaStart.plusMonths(3).minusDays(1);                    // 2026-09-30
            show("fiscalEnd", fiscalEnd);
            int januaryQuarter = fiscalQuarter(LocalDate.of(2027, 1, 10), Month.APRIL);    // 4
            show("januaryQuarter", januaryQuarter);
        }
        {
            ZoneId company = ZoneId.of("Europe/Berlin");
            LocalDate reportDay = LocalDate.of(2026, 8, 20);
            ZonedDateTime from = reportDay.with(IsoFields.DAY_OF_QUARTER, 1).atStartOfDay(company);                   // 2026-07-01T00:00+02:00[Europe/Berlin]
            show("from", from);
            ZonedDateTime to = reportDay.plus(1, IsoFields.QUARTER_YEARS).with(IsoFields.DAY_OF_QUARTER, 1).atStartOfDay(company);   // 2026-10-01T00:00+02:00[Europe/Berlin]
            show("to", to);
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
