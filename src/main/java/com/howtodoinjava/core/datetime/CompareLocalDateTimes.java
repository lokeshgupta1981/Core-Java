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
 * Examples for the tutorial "Compare LocalDateTime in Java: isBefore, isEqual, compareTo".
 * https://howtodoinjava.com/java/date-time/compare-localdatetime/
 */
public class CompareLocalDateTimes {
    static String classify(LocalDateTime submitted, LocalDateTime deadline) {
        LocalDateTime graceEnd = deadline.plusMinutes(2);
        if (!submitted.isAfter(deadline)) {
            return "on time";
        }
        return submitted.isAfter(graceEnd) ? "rejected" : "late";
    }
    public static void main(String[] args) throws Exception {
        {
            LocalDateTime deadline = LocalDateTime.of(2025, 3, 21, 17, 0);
            LocalDateTime submitted = LocalDateTime.of(2025, 3, 21, 16, 58, 30);
            boolean inTime = submitted.isBefore(deadline);             // true
            show("inTime", inTime);
            boolean late = submitted.isAfter(deadline);                // false
            show("late", late);
            boolean atDeadline = submitted.isEqual(deadline);          // false
            show("atDeadline", atDeadline);
            int order = submitted.compareTo(deadline);                 // -1
            show("order", order);
            boolean sameDay = submitted.toLocalDate().isEqual(deadline.toLocalDate());   // true
            show("sameDay", sameDay);
        }
        {
            LocalDateTime opened = LocalDateTime.parse("2025-03-21T09:15:30.250");
            LocalDateTime closed = LocalDateTime.parse("2025-03-21T09:15:45");
            boolean isBefore = opened.isBefore(closed);       // true
            show("isBefore", isBefore);
            boolean isAfter = opened.isAfter(closed);         // false
            show("isAfter", isAfter);
            boolean isEqual = opened.isEqual(closed);         // false
            show("isEqual", isEqual);
        }
        {
            LocalDateTime base = LocalDateTime.parse("2017-01-14T15:32:56");
            int yearsLater = LocalDateTime.parse("2019-04-28T22:32:38.536").compareTo(base);   // 2
            show("yearsLater", yearsLater);
            int daysLater = LocalDateTime.parse("2017-01-20T08:00").compareTo(base);           // 6
            show("daysLater", daysLater);
            int hoursLater = LocalDateTime.parse("2017-01-14T20:00").compareTo(base);          // 1
            show("hoursLater", hoursLater);
            int same = LocalDateTime.parse("2017-01-14T15:32:56").compareTo(base);             // 0
            show("same", same);
        }
        {
            List<LocalDateTime> logins = List.of(LocalDateTime.parse("2025-03-20T08:10"), LocalDateTime.parse("2025-03-21T07:55"), LocalDateTime.parse("2025-03-19T22:40"));
            LocalDateTime lastLogin = Collections.max(logins);                     // 2025-03-21T07:55
            show("lastLogin", lastLogin);
            List<LocalDateTime> newestFirst = logins.stream().sorted(Comparator.reverseOrder()).toList();   // [2025-03-21T07:55, 2025-03-20T08:10, 2025-03-19T22:40]
            show("newestFirst", newestFirst);
        }
        {
            LocalDateTime ldt1 = LocalDateTime.of(2019, 4, 9, 10, 10, 50);
            LocalDateTime ldt2 = LocalDateTime.of(2019, 4, 9, 10, 10, 50);
            LocalDateTime ldt3 = LocalDateTime.of(2019, 4, 9, 11, 12, 50);
            boolean same = ldt1.equals(ldt2);            // true
            show("same", same);
            boolean different = ldt1.equals(ldt3);       // false
            show("different", different);
            boolean withNull = ldt1.equals(null);        // false
            show("withNull", withNull);
            try { boolean crash = ldt1.isEqual(null); show("crash", crash); } catch (Throwable _t) { System.out.println("crash -> " + _t); }
        }
        {
            LocalDateTime opened = LocalDateTime.parse("2025-03-21T09:15:30.250");
            LocalDateTime closed = LocalDateTime.parse("2025-03-21T09:15:45");
            boolean sameMinute = opened.truncatedTo(ChronoUnit.MINUTES).isEqual(closed.truncatedTo(ChronoUnit.MINUTES));   // true
            show("sameMinute", sameMinute);
            boolean sameHour = opened.truncatedTo(ChronoUnit.HOURS).isEqual(closed.truncatedTo(ChronoUnit.HOURS));         // true
            show("sameHour", sameHour);
            boolean sameDay = opened.toLocalDate().isEqual(closed.toLocalDate());                                           // true
            show("sameDay", sameDay);
            boolean timeBefore = opened.toLocalTime().isBefore(LocalTime.of(10, 0));                                        // true
            show("timeBefore", timeBefore);
        }
        {
            LocalDateTime a = LocalDateTime.parse("2025-03-02T10:00");
            LocalDateTime b = LocalDateTime.parse("2025-03-30T18:45");
            boolean sameMonth = YearMonth.from(a).equals(YearMonth.from(b));     // true
            show("sameMonth", sameMonth);
            try { LocalDateTime bad = a.truncatedTo(ChronoUnit.MONTHS); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
        }
        {
            LocalDateTime berlinClock = LocalDateTime.parse("2025-03-21T09:00");
            LocalDateTime chicagoClock = LocalDateTime.parse("2025-03-21T09:00");
            boolean looksEqual = berlinClock.isEqual(chicagoClock);                        // true
            show("looksEqual", looksEqual);
            ZonedDateTime berlin = berlinClock.atZone(ZoneId.of("Europe/Berlin"));
            ZonedDateTime chicago = chicagoClock.atZone(ZoneId.of("America/Chicago"));
            boolean berlinFirst = berlin.isBefore(chicago);                                // true
            show("berlinFirst", berlinFirst);
            long hoursApart = Duration.between(berlin, chicago).toHours();                 // 6
            show("hoursApart", hoursApart);
        }
        {
            LocalDateTime deadline = LocalDateTime.of(2025, 3, 21, 17, 0);
            String early = classify(LocalDateTime.of(2025, 3, 21, 16, 59, 59), deadline);   // "on time"
            show("early", early);
            String exact = classify(deadline, deadline);                                    // "on time"
            show("exact", exact);
            String late = classify(LocalDateTime.of(2025, 3, 21, 17, 1, 30), deadline);     // "late"
            show("late", late);
            String rejected = classify(LocalDateTime.of(2025, 3, 21, 17, 2, 1), deadline);  // "rejected"
            show("rejected", rejected);
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
