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
 * Examples for the tutorial "Days Between Two Dates in Java (ChronoUnit.DAYS.between)".
 * https://howtodoinjava.com/java/date-time/calculate-days-between-dates/
 */
public class DaysBetweenDates {
    static String paymentStatus(LocalDate invoiced, LocalDate today) {
        LocalDate due = invoiced.plusDays(30);
        long days = ChronoUnit.DAYS.between(today, due);
        if (days > 0) {
            return days + " days left";
        }
        return days == 0 ? "due today" : -days + " days overdue";
    }
    public static void main(String[] args) throws Exception {
        {
            LocalDate issued = LocalDate.parse("2023-06-01");
            LocalDate due = LocalDate.parse("2023-06-15");
            long days = ChronoUnit.DAYS.between(issued, due);           // 14
            show("days", days);
            long viaUntil = issued.until(due, ChronoUnit.DAYS);         // 14
            show("viaUntil", viaUntil);
            long viaEpochDay = due.toEpochDay() - issued.toEpochDay();  // 14
            show("viaEpochDay", viaEpochDay);
            long inclusive = ChronoUnit.DAYS.between(issued, due) + 1;  // 15
            show("inclusive", inclusive);
        }
        {
            LocalDate start = LocalDate.of(2024, 2, 1);
            LocalDate end = LocalDate.of(2024, 3, 1);
            long leapFebruary = ChronoUnit.DAYS.between(start, end);                                 // 29
            show("leapFebruary", leapFebruary);
            long year2024 = ChronoUnit.DAYS.between(LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1));   // 366
            show("year2024", year2024);
            long sameDay = ChronoUnit.DAYS.between(start, start);                                    // 0
            show("sameDay", sameDay);
            long backwards = ChronoUnit.DAYS.between(end, start);                                    // -29
            show("backwards", backwards);
        }
        {
            LocalDate today = LocalDate.of(2026, 10, 10);
            LocalDate conference = LocalDate.of(2026, 12, 3);
            long daysLeft = today.until(conference, ChronoUnit.DAYS);   // 54
            show("daysLeft", daysLeft);
            String banner = daysLeft + " days to go";                   // "54 days to go"
            show("banner", banner);
        }
        {
            LocalDate first = LocalDate.of(2025, 6, 1);
            LocalDate last = LocalDate.of(2025, 6, 3);
            long nights = ChronoUnit.DAYS.between(first, last);          // 2
            show("nights", nights);
            long eventDays = ChronoUnit.DAYS.between(first, last) + 1;   // 3
            show("eventDays", eventDays);
            long listed = first.datesUntil(last.plusDays(1)).count();    // 3
            show("listed", listed);
        }
        {
            LocalDateTime pickup = LocalDateTime.parse("2023-06-01T10:00:00");
            LocalDateTime dropOff = LocalDateTime.parse("2023-06-15T09:00:00");
            long fullDays = ChronoUnit.DAYS.between(pickup, dropOff);                                  // 13
            show("fullDays", fullDays);
            long calendarDays = ChronoUnit.DAYS.between(pickup.toLocalDate(), dropOff.toLocalDate());  // 14
            show("calendarDays", calendarDays);
        }
        {
            ZoneId paris = ZoneId.of("Europe/Paris");
            ZonedDateTime from = ZonedDateTime.of(2025, 3, 29, 12, 0, 0, 0, paris);
            ZonedDateTime to = ZonedDateTime.of(2025, 4, 1, 12, 0, 0, 0, paris);
            long days = ChronoUnit.DAYS.between(from, to);              // 3
            show("days", days);
            long hours = ChronoUnit.HOURS.between(from, to);            // 71
            show("hours", hours);
            long fromHours = ChronoUnit.HOURS.between(from, to) / 24;   // 2
            show("fromHours", fromHours);
        }
        {
            long mixed = ChronoUnit.DAYS.between(LocalDate.of(2024, 1, 1), LocalDateTime.of(2024, 1, 5, 10, 0));   // 4
            show("mixed", mixed);
            try { long reverse = ChronoUnit.DAYS.between(LocalDateTime.of(2024, 1, 5, 10, 0), LocalDate.of(2024, 1, 1)); show("reverse", reverse); } catch (Throwable _t) { System.out.println("reverse -> " + _t); }
        }
        {
            LocalDate start = LocalDate.parse("2023-06-01");
            LocalDate end = LocalDate.parse("2023-07-20");
            long correct = ChronoUnit.DAYS.between(start, end);   // 49
            show("correct", correct);
            Period period = start.until(end);                     // P1M19D
            show("period", period);
            int daysPart = period.getDays();                      // 19
            show("daysPart", daysPart);
        }
        {
            LocalDate invoiced = LocalDate.of(2026, 9, 1);
            String early = paymentStatus(invoiced, LocalDate.of(2026, 9, 21));     // "10 days left"
            show("early", early);
            String dueToday = paymentStatus(invoiced, LocalDate.of(2026, 10, 1));  // "due today"
            show("dueToday", dueToday);
            String late = paymentStatus(invoiced, LocalDate.of(2026, 10, 10));     // "9 days overdue"
            show("late", late);
            LocalDate realToday = LocalDate.now(ZoneId.of("America/Chicago"));     // the current date in Chicago
            show("realToday", realToday);
        }
        {
            Date sent = Date.from(Instant.parse("2024-03-09T17:00:00Z"));
            Date paid = Date.from(Instant.parse("2024-03-12T16:00:00Z"));
            ZoneId zone = ZoneId.of("America/New_York");
            long days = ChronoUnit.DAYS.between(LocalDate.ofInstant(sent.toInstant(), zone), LocalDate.ofInstant(paid.toInstant(), zone));   // 3
            show("days", days);
            long millisDays = (paid.getTime() - sent.getTime()) / 86_400_000L;   // 2
            show("millisDays", millisDays);
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
