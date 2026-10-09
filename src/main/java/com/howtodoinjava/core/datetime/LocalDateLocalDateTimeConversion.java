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
 * Examples for the tutorial "Convert LocalDate to LocalDateTime in Java and Back".
 * https://howtodoinjava.com/java/date-time/localdate-localdatetime-conversions/
 */
public class LocalDateLocalDateTimeConversion {
    static record Order(int id, LocalDateTime placedAt) {}
    static List<Order> placedOn(List<Order> orders, LocalDate day) {
        LocalDateTime from = day.atStartOfDay();
        LocalDateTime to = day.plusDays(1).atStartOfDay();          // exclusive upper bound
        return orders.stream()
                .filter(o -> !o.placedAt().isBefore(from) && o.placedAt().isBefore(to))
                .toList();
    }
    public static void main(String[] args) throws Exception {
        {
            LocalDate deliveryDay = LocalDate.of(2026, 10, 10);
            LocalDateTime midnight = deliveryDay.atStartOfDay();                    // 2026-10-10T00:00
            show("midnight", midnight);
            LocalDateTime slot = deliveryDay.atTime(12, 30);                        // 2026-10-10T12:30
            show("slot", slot);
            LocalDateTime exact = deliveryDay.atTime(LocalTime.of(18, 45, 10));     // 2026-10-10T18:45:10
            show("exact", exact);
            LocalDate dayAgain = exact.toLocalDate();                               // 2026-10-10
            show("dayAgain", dayAgain);
            LocalTime timeOnly = exact.toLocalTime();                               // 18:45:10
            show("timeOnly", timeOnly);
        }
        {
            LocalDate trialStart = LocalDate.of(2026, 10, 10);
            LocalDateTime startsAt = trialStart.atStartOfDay();        // 2026-10-10T00:00
            show("startsAt", startsAt);
            LocalDateTime sameValue = trialStart.atTime(LocalTime.MIDNIGHT);   // 2026-10-10T00:00
            show("sameValue", sameValue);
        }
        {
            LocalDate pickupDay = LocalDate.of(2026, 10, 10);
            LocalDateTime pickup = pickupDay.atTime(17, 5);                     // 2026-10-10T17:05
            show("pickup", pickup);
            LocalDateTime precise = pickupDay.atTime(17, 5, 30, 250_000_000);   // 2026-10-10T17:05:30.250
            show("precise", precise);
            try { LocalDateTime invalid = pickupDay.atTime(24, 0); show("invalid", invalid); } catch (Throwable _t) { System.out.println("invalid -> " + _t); }
        }
        {
            LocalDate day = LocalDate.of(2026, 10, 10);
            LocalDateTime noon = day.atTime(LocalTime.NOON);                       // 2026-10-10T12:00
            show("noon", noon);
            LocalDateTime lastNano = day.atTime(LocalTime.MAX);                    // 2026-10-10T23:59:59.999999999
            show("lastNano", lastNano);
            LocalDateTime combined = LocalDateTime.of(day, LocalTime.of(8, 0));    // 2026-10-10T08:00
            show("combined", combined);
        }
        {
            Clock clock = Clock.fixed(Instant.parse("2026-10-10T06:30:00Z"), ZoneId.of("Europe/Madrid"));
            LocalDateTime withNow = LocalDate.of(2026, 12, 24).atTime(LocalTime.now(clock));   // 2026-12-24T08:30
            show("withNow", withNow);
        }
        {
            LocalDateTime placedAt = LocalDateTime.of(2026, 10, 10, 23, 58, 40);
            LocalDate orderDay = placedAt.toLocalDate();                    // 2026-10-10
            show("orderDay", orderDay);
            LocalTime orderTime = placedAt.toLocalTime();                   // 23:58:40
            show("orderTime", orderTime);
            LocalDate fromTemporal = LocalDate.from(placedAt);              // 2026-10-10
            show("fromTemporal", fromTemporal);
            LocalDateTime cutToDay = placedAt.truncatedTo(ChronoUnit.DAYS); // 2026-10-10T00:00
            show("cutToDay", cutToDay);
        }
        {
            LocalDateTime recordedInLondon = LocalDateTime.of(2026, 10, 10, 23, 30);
            LocalDate dayInTokyo = recordedInLondon.atZone(ZoneId.of("Europe/London")).withZoneSameInstant(ZoneId.of("Asia/Tokyo")).toLocalDate();   // 2026-10-11
            show("dayInTokyo", dayInTokyo);
        }
        {
            LocalDate dstStart = LocalDate.of(2026, 3, 8);
            ZoneId havana = ZoneId.of("America/Havana");
            LocalDateTime plainMidnight = dstStart.atStartOfDay();          // 2026-03-08T00:00
            show("plainMidnight", plainMidnight);
            ZonedDateTime realStart = dstStart.atStartOfDay(havana);        // 2026-03-08T01:00-04:00[America/Havana]
            show("realStart", realStart);
        }
        {
            List<Order> orders = List.of(new Order(1, LocalDateTime.of(2026, 10, 9, 23, 59, 59)), new Order(2, LocalDateTime.of(2026, 10, 10, 0, 0)), new Order(3, LocalDateTime.of(2026, 10, 10, 23, 59, 59, 900_000_000)), new Order(4, LocalDateTime.of(2026, 10, 11, 0, 0)));
            List<Integer> ids = placedOn(orders, LocalDate.of(2026, 10, 10)).stream().map(Order::id).toList();   // [2, 3]
            show("ids", ids);
        }
        {
            LocalDate due = LocalDate.of(2026, 10, 10);
            LocalDateTime delivered = LocalDateTime.of(2026, 10, 10, 21, 15);
            boolean onTime = !delivered.toLocalDate().isAfter(due);          // true
            show("onTime", onTime);
            boolean beforeDueDay = delivered.isBefore(due.atStartOfDay());   // false
            show("beforeDueDay", beforeDueDay);
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
