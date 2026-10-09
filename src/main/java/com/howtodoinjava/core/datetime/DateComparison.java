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
 * Examples for the tutorial "Compare Two Dates in Java: LocalDate, ZonedDateTime, Date".
 * https://howtodoinjava.com/java/date-time/compare-dates/
 */
public class DateComparison {
    static String loanStatus(LocalDate due, LocalDate returned, LocalDate today) {
        if (returned == null) {
            return today.isAfter(due) ? "overdue" : "on loan";
        }
        return returned.isAfter(due) ? "returned late" : "returned on time";
    }
    public static void main(String[] args) throws Exception {
        {
            LocalDate due = LocalDate.of(2024, 5, 10);
            LocalDate returned = LocalDate.of(2024, 5, 14);
            boolean late = returned.isAfter(due);          // true
            show("late", late);
            boolean early = returned.isBefore(due);        // false
            show("early", early);
            boolean onTime = returned.isEqual(due);        // false
            show("onTime", onTime);
            int order = returned.compareTo(due);           // 4
            show("order", order);
            LocalDateTime pickedUp = LocalDateTime.of(2024, 5, 10, 9, 0);
            LocalDateTime closed = LocalDateTime.of(2024, 5, 10, 18, 30);
            boolean sameMoment = pickedUp.isEqual(closed);                            // false
            show("sameMoment", sameMoment);
            boolean sameDay = pickedUp.toLocalDate().isEqual(closed.toLocalDate());   // true
            show("sameDay", sameDay);
        }
        {
            LocalDate due = LocalDate.of(2024, 5, 10);
            LocalDate returned = LocalDate.of(2024, 5, 14);
            int days = returned.compareTo(due);                          // 4
            show("days", days);
            int years = LocalDate.of(2026, 1, 1).compareTo(due);         // 2
            show("years", years);
            String result = days > 0 ? "late" : days < 0 ? "early" : "on time";   // "late"
            show("result", result);
        }
        {
            LocalDateTime pickedUp = LocalDateTime.of(2024, 5, 10, 9, 0);
            LocalDateTime closed = LocalDateTime.of(2024, 5, 10, 18, 30);
            boolean sameDay = pickedUp.toLocalDate().isEqual(closed.toLocalDate());   // true
            show("sameDay", sameDay);
            boolean before = pickedUp.isBefore(closed);                               // true
            show("before", before);
            LocalDateTime scan = LocalDateTime.of(2024, 5, 10, 9, 42, 15);
            boolean sameHour = pickedUp.truncatedTo(ChronoUnit.HOURS).isEqual(scan.truncatedTo(ChronoUnit.HOURS));   // true
            show("sameHour", sameHour);
        }
        {
            ZonedDateTime india = ZonedDateTime.of(2024, 4, 26, 9, 0, 0, 0, ZoneId.of("Asia/Kolkata"));
            ZonedDateTime newYork = india.withZoneSameInstant(ZoneId.of("America/New_York"));   // 2024-04-25T23:30-04:00[America/New_York]
            show("newYork", newYork);
            boolean sameInstant = india.isEqual(newYork);                              // true
            show("sameInstant", sameInstant);
            boolean equalObjects = india.equals(newYork);                              // false
            show("equalObjects", equalObjects);
            int order = india.compareTo(newYork);                                      // 1
            show("order", order);
            int instantOrder = india.toInstant().compareTo(newYork.toInstant());       // 0
            show("instantOrder", instantOrder);
            boolean sameLocalDate = india.toLocalDate().isEqual(newYork.toLocalDate());   // false
            show("sameLocalDate", sameLocalDate);
        }
        {
            LocalDate start = LocalDate.of(2024, 5, 1);
            LocalDate end = LocalDate.of(2024, 5, 31);
            LocalDate returned = LocalDate.of(2024, 5, 31);
            boolean inclusive = !returned.isBefore(start) && !returned.isAfter(end);   // true
            show("inclusive", inclusive);
            boolean exclusive = returned.isAfter(start) && returned.isBefore(end);     // false
            show("exclusive", exclusive);
        }
        {
            List<LocalDate> dates = List.of(LocalDate.of(2024, 5, 14), LocalDate.of(2024, 3, 2), LocalDate.of(2024, 8, 20));
            List<LocalDate> oldestFirst = dates.stream().sorted().toList();                          // [2024-03-02, 2024-05-14, 2024-08-20]
            show("oldestFirst", oldestFirst);
            List<LocalDate> newestFirst = dates.stream().sorted(Comparator.reverseOrder()).toList(); // [2024-08-20, 2024-05-14, 2024-03-02]
            show("newestFirst", newestFirst);
            LocalDate latest = Collections.max(dates);                                               // 2024-08-20
            show("latest", latest);
            LocalDate earliest = dates.stream().min(Comparator.naturalOrder()).orElseThrow();        // 2024-03-02
            show("earliest", earliest);
        }
        {
            List<LocalDate> returnDates = new ArrayList<>(Arrays.asList(LocalDate.of(2024, 5, 14), null, LocalDate.of(2024, 3, 2)));
            returnDates.sort(Comparator.nullsLast(Comparator.naturalOrder()));
            String sortedText = returnDates.toString();                                              // "[2024-03-02, 2024-05-14, null]"
            show("sortedText", sortedText);
            LocalDate notReturned = null;
            try { boolean unsafe = returnDates.get(0).isBefore(notReturned); show("unsafe", unsafe); } catch (Throwable _t) { System.out.println("unsafe -> " + _t); }
            boolean safe = notReturned != null && returnDates.get(0).isBefore(notReturned);          // false
            show("safe", safe);
        }
        {
            Date date1 = Date.from(Instant.parse("2024-05-10T09:00:00Z"));
            Date date2 = Date.from(Instant.parse("2024-05-10T18:30:00Z"));
            boolean isEqual = date1.equals(date2);       // false
            show("isEqual", isEqual);
            boolean isBefore = date1.before(date2);      // true
            show("isBefore", isBefore);
            int order = date1.compareTo(date2);          // -1
            show("order", order);
        }
        {
            Date date1 = Date.from(Instant.parse("2024-05-10T09:00:00Z"));
            Date date2 = Date.from(Instant.parse("2024-05-10T18:30:00Z"));
            ZoneId zone = ZoneId.of("UTC");
            LocalDate day1 = date1.toInstant().atZone(zone).toLocalDate();     // 2024-05-10
            show("day1", day1);
            LocalDate day2 = date2.toInstant().atZone(zone).toLocalDate();     // 2024-05-10
            show("day2", day2);
            boolean sameDate = day1.isEqual(day2);                             // true
            show("sameDate", sameDate);
            LocalDate kolkataDay2 = date2.toInstant().atZone(ZoneId.of("Asia/Kolkata")).toLocalDate();   // 2024-05-11
            show("kolkataDay2", kolkataDay2);
        }
        {
            LocalDate due = LocalDate.of(2024, 5, 10);
            String onLoan = loanStatus(due, null, LocalDate.of(2024, 5, 9));                     // "on loan"
            show("onLoan", onLoan);
            String overdue = loanStatus(due, null, LocalDate.of(2024, 5, 11));                // "overdue"
            show("overdue", overdue);
            String onTime = loanStatus(due, LocalDate.of(2024, 5, 10), LocalDate.of(2024, 6, 1));   // "returned on time"
            show("onTime", onTime);
            String lateReturn = loanStatus(due, LocalDate.of(2024, 5, 14), LocalDate.of(2024, 6, 1)); // "returned late"
            show("lateReturn", lateReturn);
            LocalDate todayInLibrary = LocalDate.now(ZoneId.of("Europe/London"));             // the current date in London
            show("todayInLibrary", todayInLibrary);
        }
        {
            LocalDate a = LocalDate.of(2024, 5, 10);
            LocalDate b = LocalDate.parse("2024-05-10");
            boolean sameObject = a == b;            // false
            show("sameObject", sameObject);
            boolean sameDate = a.isEqual(b);        // true
            show("sameDate", sameDate);
        }
        {
            LocalDate due = LocalDate.of(2024, 5, 10);
            LocalDateTime pickedUp = LocalDateTime.of(2024, 5, 10, 9, 0);
            boolean sameDay = pickedUp.toLocalDate().isEqual(due);           // true
            show("sameDay", sameDay);
            boolean afterMidnight = due.atStartOfDay().isBefore(pickedUp);   // true
            show("afterMidnight", afterMidnight);
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
