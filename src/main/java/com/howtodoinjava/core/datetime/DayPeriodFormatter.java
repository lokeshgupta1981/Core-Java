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
 * Examples for the tutorial "Java Date Format with Day Period: Pattern B Examples".
 * https://howtodoinjava.com/java/date-time/date-format-with-day-period/
 */
public class DayPeriodFormatter {
    static String mealTime(LocalTime time) {
        return switch (time.getHour()) {
            case 6, 7, 8, 9, 10 -> "breakfast";
            case 11, 12, 13, 14 -> "lunch";
            case 15, 16, 17 -> "snack";
            case 18, 19, 20, 21, 22 -> "dinner";
            default -> "late night";
        };
    }
    public static void main(String[] args) throws Exception {
        {
            DateTimeFormatter friendly = DateTimeFormatter.ofPattern("h:mm B", Locale.US);
            String afternoon = LocalTime.of(17, 45).format(friendly);    // "5:45 in the afternoon"
            show("afternoon", afternoon);
            String night = LocalTime.of(21, 30).format(friendly);        // "9:30 at night"
            show("night", night);
            String noon = LocalTime.NOON.format(friendly);               // "12:00 noon"
            show("noon", noon);
            String german = LocalTime.of(17, 45).format(DateTimeFormatter.ofPattern("h:mm B", Locale.GERMANY));   // "5:45 nachmittags"
            show("german", german);
        }
        {
            LocalTime lunch = LocalTime.NOON;
            String one = LocalTime.of(17, 45).format(DateTimeFormatter.ofPattern("B", Locale.US));       // "in the afternoon"
            show("one", one);
            String four = LocalTime.of(17, 45).format(DateTimeFormatter.ofPattern("BBBB", Locale.US));   // "in the afternoon"
            show("four", four);
            String narrow = lunch.format(DateTimeFormatter.ofPattern("BBBBB", Locale.US));              // "n"
            show("narrow", narrow);
            try { DateTimeFormatter two = DateTimeFormatter.ofPattern("BB"); show("two", two); } catch (Throwable _t) { System.out.println("two -> " + _t); }
        }
        {
            LocalDateTime meeting = LocalDateTime.of(2026, 3, 14, 17, 45);
            String withDate = meeting.format(DateTimeFormatter.ofPattern("EEEE, MMM d 'at' h:mm B", Locale.US));   // "Saturday, Mar 14 at 5:45 in the afternoon"
            show("withDate", withDate);
            try { String dateOnly = LocalDate.of(2026, 3, 14).format(DateTimeFormatter.ofPattern("MMM d B", Locale.US)); show("dateOnly", dateOnly); } catch (Throwable _t) { System.out.println("dateOnly -> " + _t); }
        }
        {
            Instant lastActive = Instant.parse("2026-03-14T21:45:00Z");
            DateTimeFormatter lastSeen = DateTimeFormatter.ofPattern("'last seen at' h:mm B", Locale.US);
            String inNewYork = lastActive.atZone(ZoneId.of("America/New_York")).format(lastSeen);   // "last seen at 5:45 in the afternoon"
            show("inNewYork", inNewYork);
            String inKolkata = lastActive.atZone(ZoneId.of("Asia/Kolkata")).format(lastSeen);      // "last seen at 3:15 in the morning"
            show("inKolkata", inKolkata);
        }
        {
            DateTimeFormatter built = new DateTimeFormatterBuilder()
                    .appendPattern("h:mm ")
                    .appendDayPeriodText(TextStyle.FULL)
                    .toFormatter(Locale.US);
            String reminder = LocalTime.of(9, 15).format(built);       // "9:15 in the morning"
            show("reminder", reminder);
        }
        {
            LocalTime evening = LocalTime.of(17, 45);
            String usOrder = evening.format(DateTimeFormatter.ofLocalizedPattern("Bhm").withLocale(Locale.US));      // "5:45 in the afternoon"
            show("usOrder", usOrder);
            String jaOrder = evening.format(DateTimeFormatter.ofLocalizedPattern("Bhm").withLocale(Locale.JAPAN));   // "夕方5:45"
            show("jaOrder", jaOrder);
        }
        {
            DateTimeFormatter parser = DateTimeFormatter.ofPattern("h:mm B", Locale.US);
            LocalTime parsedAfternoon = LocalTime.parse("5:45 in the afternoon", parser);   // 17:45
            show("parsedAfternoon", parsedAfternoon);
            LocalTime parsedNight = LocalTime.parse("9:30 at night", parser);               // 21:30
            show("parsedNight", parsedNight);
        }
        {
            LocalTime middle = LocalTime.parse("in the morning", DateTimeFormatter.ofPattern("B", Locale.US));   // 06:00
            show("middle", middle);
            DateTimeFormatter clock24 = DateTimeFormatter.ofPattern("H:mm B", Locale.US);
            try { LocalTime conflict = LocalTime.parse("17:45 in the morning", clock24); show("conflict", conflict); } catch (Throwable _t) { System.out.println("conflict -> " + _t); }
            LocalTime ignored = LocalTime.parse("17:45 in the morning", clock24.withResolverStyle(ResolverStyle.LENIENT));   // 17:45
            show("ignored", ignored);
        }
        {
            String early = mealTime(LocalTime.of(6, 0));          // "breakfast"
            show("early", early);
            String midday = mealTime(LocalTime.of(12, 30));       // "lunch"
            show("midday", midday);
            String late = mealTime(LocalTime.of(23, 15));         // "late night"
            show("late", late);
            String label = LocalTime.of(19, 5).format(DateTimeFormatter.ofPattern("h:mm a", Locale.US)) + " (" + mealTime(LocalTime.of(19, 5)) + ")";   // "7:05 PM (dinner)"
            show("label", label);
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
