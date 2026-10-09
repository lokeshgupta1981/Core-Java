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
 * Examples for the tutorial "Compare LocalDate in Java: isBefore, isAfter, compareTo".
 * https://howtodoinjava.com/java/date-time/compare-localdates/
 */
public class CompareLocalDates {
    static record Booking(String guest, LocalDate checkIn) {}
    static String validateStay(String checkInText, String checkOutText, LocalDate today) {
        try {
            LocalDate checkIn = LocalDate.parse(checkInText.strip());
            LocalDate checkOut = LocalDate.parse(checkOutText.strip());
            if (checkIn.isBefore(today)) {
                return "check-in is in the past";
            }
            if (!checkOut.isAfter(checkIn)) {
                return "check-out must be after check-in";
            }
            if (checkOut.isAfter(checkIn.plusDays(30))) {
                return "stay is longer than 30 nights";
            }
            return "ok";
        } catch (DateTimeParseException e) {
            return "invalid date: " + e.getParsedString();
        }
    }
    public static void main(String[] args) throws Exception {
        {
            LocalDate checkIn = LocalDate.of(2025, 7, 14);
            LocalDate checkOut = LocalDate.of(2025, 7, 18);
            boolean valid = checkOut.isAfter(checkIn);           // true
            show("valid", valid);
            boolean before = checkOut.isBefore(checkIn);         // false
            show("before", before);
            boolean sameDay = checkOut.isEqual(checkIn);         // false
            show("sameDay", sameDay);
            int order = checkOut.compareTo(checkIn);             // 4
            show("order", order);
            boolean equal = checkIn.equals(LocalDate.parse("2025-07-14"));   // true
            show("equal", equal);
        }
        {
            LocalDate checkIn = LocalDate.of(2025, 7, 14);
            boolean sameDayCheckOut = LocalDate.of(2025, 7, 14).isAfter(checkIn);       // false
            show("sameDayCheckOut", sameDayCheckOut);
            boolean nextDayCheckOut = LocalDate.of(2025, 7, 15).isAfter(checkIn);       // true
            show("nextDayCheckOut", nextDayCheckOut);
            boolean notBefore = !LocalDate.of(2025, 7, 14).isBefore(checkIn);           // true, on or after
            show("notBefore", notBefore);
        }
        {
            LocalDate base = LocalDate.of(2025, 7, 14);
            int days = LocalDate.of(2025, 7, 18).compareTo(base);     // 4
            show("days", days);
            int months = LocalDate.of(2025, 10, 1).compareTo(base);   // 3
            show("months", months);
            int years = LocalDate.of(2027, 1, 1).compareTo(base);     // 2
            show("years", years);
            int same = LocalDate.of(2025, 7, 14).compareTo(base);     // 0
            show("same", same);
            int earlier = LocalDate.of(2024, 12, 31).compareTo(base); // -1
            show("earlier", earlier);
        }
        {
            List<Booking> bookings = List.of(new Booking("Anna", LocalDate.of(2025, 8, 2)), new Booking("Ravi", LocalDate.of(2025, 7, 14)));
            List<String> byDate = bookings.stream().sorted(Comparator.comparing(Booking::checkIn)).map(Booking::guest).toList();   // [Ravi, Anna]
            show("byDate", byDate);
            LocalDate first = bookings.stream().map(Booking::checkIn).min(Comparator.naturalOrder()).orElseThrow();              // 2025-07-14
            show("first", first);
        }
        {
            LocalDate checkIn = LocalDate.of(2025, 7, 14);
            ThaiBuddhistDate thai = ThaiBuddhistDate.from(checkIn);   // ThaiBuddhist BE 2568-07-14
            show("thai", thai);
            boolean sameDay = checkIn.isEqual(thai);                  // true
            show("sameDay", sameDay);
            boolean sameObject = checkIn.equals(thai);                // false
            show("sameObject", sameObject);
            boolean nullSafe = checkIn.equals(null);                  // false
            show("nullSafe", nullSafe);
        }
        {
            Clock clock = Clock.fixed(Instant.parse("2025-07-14T22:00:00Z"), ZoneOffset.UTC);
            LocalDate todayUtc = LocalDate.now(clock);                                          // 2025-07-14
            show("todayUtc", todayUtc);
            LocalDate todaySydney = LocalDate.now(clock.withZone(ZoneId.of("Australia/Sydney")));   // 2025-07-15
            show("todaySydney", todaySydney);
            boolean inPast = LocalDate.of(2025, 7, 14).isBefore(todaySydney);                   // true
            show("inPast", inPast);
            LocalDate realToday = LocalDate.now(ZoneId.of("Australia/Sydney"));                 // the current date in Sydney
            show("realToday", realToday);
        }
        {
            LocalDate checkIn = LocalDate.of(2025, 7, 14);
            LocalDate checkOut = null;
            try { boolean crash = checkOut.isAfter(checkIn); show("crash", crash); } catch (Throwable _t) { System.out.println("crash -> " + _t); }
            boolean safe = checkOut != null && checkOut.isAfter(checkIn);                  // false
            show("safe", safe);
            boolean viaOptional = Optional.ofNullable(checkOut).map(d -> d.isAfter(checkIn)).orElse(false);   // false
            show("viaOptional", viaOptional);
        }
        {
            LocalDate today = LocalDate.of(2025, 7, 1);
            String ok = validateStay("2025-07-14", "2025-07-18", today);        // "ok"
            show("ok", ok);
            String sameDay = validateStay("2025-07-14", "2025-07-14", today);   // "check-out must be after check-in"
            show("sameDay", sameDay);
            String past = validateStay("2025-06-30", "2025-07-02", today);      // "check-in is in the past"
            show("past", past);
            String tooLong = validateStay("2025-07-14", "2025-08-20", today);   // "stay is longer than 30 nights"
            show("tooLong", tooLong);
            String typo = validateStay("2025-07-41", "2025-07-45", today);      // "invalid date: 2025-07-41"
            show("typo", typo);
        }
        {
            LocalDate start = LocalDate.of(2025, 7, 1);
            LocalDate end = LocalDate.of(2025, 7, 31);
            LocalDate day = LocalDate.of(2025, 7, 31);
            boolean inJuly = !day.isBefore(start) && !day.isAfter(end);   // true
            show("inJuly", inJuly);
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
