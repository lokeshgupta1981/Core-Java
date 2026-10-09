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
 * Examples for the tutorial "Java LocalDate: Create, Parse, Format and Compare Dates".
 * https://howtodoinjava.com/java/date-time/java-time-localdate-class/
 */
public class LocalDateExamples {
    static Optional<LocalDate> parseDate(String text) {
        try {
            return Optional.of(LocalDate.parse(text.strip()));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }
    static List<LocalDate> renewalDates(LocalDate joined, int months) {
        List<LocalDate> dates = new ArrayList<>();
        for (int n = 1; n <= months; n++) {
            dates.add(joined.plusMonths(n));        // always from the join date
        }
        return dates;
    }
    public static void main(String[] args) throws Exception {
        {
            LocalDate joined = LocalDate.of(2026, 1, 31);              // 2026-01-31
            show("joined", joined);
            LocalDate trialEnds = joined.plusDays(14);                 // 2026-02-14
            show("trialEnds", trialEnds);
            LocalDate renewal = joined.plusMonths(1);                  // 2026-02-28, February has no 31st
            show("renewal", renewal);
            LocalDate parsed = LocalDate.parse("2026-03-01");          // 2026-03-01
            show("parsed", parsed);
            boolean afterRenewal = parsed.isAfter(renewal);            // true
            show("afterRenewal", afterRenewal);
            DayOfWeek day = renewal.getDayOfWeek();                    // SATURDAY
            show("day", day);
            String text = renewal.format(DateTimeFormatter.ofPattern("d MMM uuuu", Locale.US));   // "28 Feb 2026"
            show("text", text);
        }
        {
            LocalDate today = LocalDate.now();                         // today in the default zone
            show("today", today);
            LocalDate todayInTokyo = LocalDate.now(ZoneId.of("Asia/Tokyo"));   // today in Tokyo
            show("todayInTokyo", todayInTokyo);
        }
        {
            Clock clock = Clock.fixed(Instant.parse("2026-05-14T10:00:00Z"), ZoneOffset.UTC);
            LocalDate testToday = LocalDate.now(clock);                // 2026-05-14
            show("testToday", testToday);
        }
        {
            LocalDate fromNumbers = LocalDate.of(2026, 5, 14);                 // 2026-05-14
            show("fromNumbers", fromNumbers);
            LocalDate fromEnum = LocalDate.of(2026, Month.MAY, 14);            // 2026-05-14
            show("fromEnum", fromEnum);
            LocalDate fromEpochDay = LocalDate.ofEpochDay(18823);              // 2021-07-15, days since 1970-01-01
            show("fromEpochDay", fromEpochDay);
            LocalDate fromYearDay = LocalDate.ofYearDay(2022, 37);             // 2022-02-06, the 37th day of 2022
            show("fromYearDay", fromYearDay);
            LocalDate fromInstant = LocalDate.ofInstant(Instant.parse("2026-05-14T22:30:00Z"), ZoneId.of("Asia/Kolkata"));   // 2026-05-15
            show("fromInstant", fromInstant);
        }
        {
            try { LocalDate invalid = LocalDate.of(2023, 2, 29); show("invalid", invalid); } catch (Throwable _t) { System.out.println("invalid -> " + _t); }
        }
        {
            LocalDate iso = LocalDate.parse("2026-02-06");                   // 2026-02-06
            show("iso", iso);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMM-yyyy", Locale.US);
            LocalDate custom = LocalDate.parse("6-Feb-2026", formatter);     // 2026-02-06
            show("custom", custom);
            try { LocalDate wrong = LocalDate.parse("06/02/2026"); show("wrong", wrong); } catch (Throwable _t) { System.out.println("wrong -> " + _t); }
        }
        {
            Optional<LocalDate> good = parseDate(" 2026-05-14 ");   // Optional[2026-05-14]
            show("good", good);
            Optional<LocalDate> bad = parseDate("14.05.2026");       // Optional.empty
            show("bad", bad);
        }
        {
            LocalDate date = LocalDate.of(2026, 5, 14);
            String iso = date.toString();                                                          // "2026-05-14"
            show("iso", iso);
            String slashes = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));               // "14/05/2026"
            show("slashes", slashes);
            String words = date.format(DateTimeFormatter.ofPattern("EEEE, d MMMM uuuu", Locale.US));   // "Thursday, 14 May 2026"
            show("words", words);
            String german = date.format(DateTimeFormatter.ofPattern("d. MMMM uuuu", Locale.GERMANY));  // "14. Mai 2026"
            show("german", german);
        }
        {
            LocalDate start = LocalDate.of(2026, 5, 14);
            LocalDate nextWeek = start.plusWeeks(1);                       // 2026-05-21
            show("nextWeek", nextWeek);
            LocalDate lastMonth = start.minusMonths(1);                    // 2026-04-14
            show("lastMonth", lastMonth);
            LocalDate inThreeYears = start.plusYears(3);                   // 2029-05-14
            show("inThreeYears", inThreeYears);
            LocalDate tenDaysAgo = start.minus(10, ChronoUnit.DAYS);       // 2026-05-04
            show("tenDaysAgo", tenDaysAgo);
            LocalDate later = start.plus(Period.of(0, 2, 5));              // 2026-07-19
            show("later", later);
        }
        {
            LocalDate endOfJanuary = LocalDate.of(2026, 1, 31);
            LocalDate february = endOfJanuary.plusMonths(1);               // 2026-02-28
            show("february", february);
            LocalDate leapDay = LocalDate.of(2028, 2, 29);
            LocalDate nextYear = leapDay.plusYears(1);                     // 2029-02-28
            show("nextYear", nextYear);
        }
        {
            LocalDate day = LocalDate.of(2026, 5, 14);
            LocalDate firstOfMonth = day.with(TemporalAdjusters.firstDayOfMonth());          // 2026-05-01
            show("firstOfMonth", firstOfMonth);
            LocalDate nextMonday = day.with(TemporalAdjusters.next(DayOfWeek.MONDAY));       // 2026-05-18
            show("nextMonday", nextMonday);
            LocalDate sameDayIn2030 = day.withYear(2030);                                     // 2030-05-14
            show("sameDayIn2030", sameDayIn2030);
        }
        {
            LocalDate date = LocalDate.of(2026, 5, 14);
            int year = date.getYear();                    // 2026
            show("year", year);
            Month month = date.getMonth();                // MAY
            show("month", month);
            int monthNumber = date.getMonthValue();       // 5
            show("monthNumber", monthNumber);
            int dayOfMonth = date.getDayOfMonth();        // 14
            show("dayOfMonth", dayOfMonth);
            DayOfWeek weekday = date.getDayOfWeek();      // THURSDAY
            show("weekday", weekday);
            int dayOfYear = date.getDayOfYear();          // 134
            show("dayOfYear", dayOfYear);
            int daysInMonth = date.lengthOfMonth();       // 31
            show("daysInMonth", daysInMonth);
            boolean leap = date.isLeapYear();             // false
            show("leap", leap);
        }
        {
            LocalDate checkIn = LocalDate.of(2026, 5, 14);
            LocalDate checkOut = LocalDate.of(2026, 6, 2);
            boolean valid = checkOut.isAfter(checkIn);                           // true
            show("valid", valid);
            boolean sameDay = checkIn.isEqual(LocalDate.parse("2026-05-14"));    // true
            show("sameDay", sameDay);
            long nights = ChronoUnit.DAYS.between(checkIn, checkOut);            // 19
            show("nights", nights);
            Period period = Period.between(checkIn, checkOut);                   // P19D
            show("period", period);
            long dayCount = checkIn.datesUntil(checkOut).count();                // 19
            show("dayCount", dayCount);
        }
        {
            LocalDate joined = LocalDate.of(2026, 1, 31);
            List<LocalDate> correct = renewalDates(joined, 3);                 // [2026-02-28, 2026-03-31, 2026-04-30]
            show("correct", correct);
            LocalDate drifted = joined.plusMonths(1).plusMonths(1);            // 2026-03-28, a day of the month is lost
            show("drifted", drifted);
            boolean dueToday = correct.contains(LocalDate.of(2026, 3, 31));    // true
            show("dueToday", dueToday);
        }
        {
            LocalDate date = LocalDate.of(2026, 5, 14);
            LocalDateTime midnight = date.atStartOfDay();                                 // 2026-05-14T00:00
            show("midnight", midnight);
            LocalDateTime lunch = date.atTime(12, 30);                                    // 2026-05-14T12:30
            show("lunch", lunch);
            ZonedDateTime parisStart = date.atStartOfDay(ZoneId.of("Europe/Paris"));      // 2026-05-14T00:00+02:00[Europe/Paris]
            show("parisStart", parisStart);
            LocalDate back = lunch.toLocalDate();                                         // 2026-05-14
            show("back", back);
        }
        {
            LocalDate date = LocalDate.of(2026, 5, 14);
            java.sql.Date sqlDate = java.sql.Date.valueOf(date);                          // 2026-05-14
            show("sqlDate", sqlDate);
            LocalDate fromSql = sqlDate.toLocalDate();                                    // 2026-05-14
            show("fromSql", fromSql);
            Date legacy = Date.from(date.atStartOfDay(ZoneId.of("UTC")).toInstant());
            LocalDate fromLegacy = LocalDate.ofInstant(legacy.toInstant(), ZoneId.of("UTC"));   // 2026-05-14
            show("fromLegacy", fromLegacy);
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
