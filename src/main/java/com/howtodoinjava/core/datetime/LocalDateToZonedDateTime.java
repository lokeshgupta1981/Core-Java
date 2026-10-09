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
 * Examples for the tutorial "Convert LocalDate to ZonedDateTime and Back in Java".
 * https://howtodoinjava.com/java/date-time/localdate-zoneddatetime-conversion/
 */
public class LocalDateToZonedDateTime {
    static record Hotel(ZoneId zone, LocalTime checkInTime) {}
    static ZonedDateTime checkInFor(Hotel hotel, LocalDate date, ZoneId guestZone) {
        return date.atTime(hotel.checkInTime()).atZone(hotel.zone()).withZoneSameInstant(guestZone);
    }
    public static void main(String[] args) throws Exception {
        {
            LocalDate checkIn = LocalDate.of(2026, 6, 12);
            ZoneId rome = ZoneId.of("Europe/Rome");
            ZonedDateTime dayStart = checkIn.atStartOfDay(rome);                         // 2026-06-12T00:00+02:00[Europe/Rome]
            show("dayStart", dayStart);
            ZonedDateTime arrival = checkIn.atTime(15, 0).atZone(rome);                  // 2026-06-12T15:00+02:00[Europe/Rome]
            show("arrival", arrival);
            ZonedDateTime inTokyo = arrival.withZoneSameInstant(ZoneId.of("Asia/Tokyo"));   // 2026-06-12T22:00+09:00[Asia/Tokyo]
            show("inTokyo", inTokyo);
            LocalDate back = arrival.toLocalDate();                                      // 2026-06-12
            show("back", back);
        }
        {
            LocalDate checkIn = LocalDate.of(2026, 6, 12);
            ZonedDateTime romeStart = checkIn.atStartOfDay(ZoneId.of("Europe/Rome"));        // 2026-06-12T00:00+02:00[Europe/Rome]
            show("romeStart", romeStart);
            ZonedDateTime utcStart = checkIn.atStartOfDay(ZoneOffset.UTC);                   // 2026-06-12T00:00Z
            show("utcStart", utcStart);
            ZonedDateTime winterStart = LocalDate.of(2026, 1, 12).atStartOfDay(ZoneId.of("Europe/Rome"));   // 2026-01-12T00:00+01:00[Europe/Rome]
            show("winterStart", winterStart);
        }
        {
            ZonedDateTime santiagoStart = LocalDate.of(2026, 9, 6).atStartOfDay(ZoneId.of("America/Santiago"));   // 2026-09-06T01:00-03:00[America/Santiago]
            show("santiagoStart", santiagoStart);
        }
        {
            LocalDate checkIn = LocalDate.of(2026, 6, 12);
            LocalTime checkInTime = LocalTime.of(15, 0);
            ZonedDateTime viaAtTime = checkIn.atTime(checkInTime).atZone(ZoneId.of("Europe/Rome"));       // 2026-06-12T15:00+02:00[Europe/Rome]
            show("viaAtTime", viaAtTime);
            ZonedDateTime viaOf = ZonedDateTime.of(checkIn, checkInTime, ZoneId.of("Europe/Rome"));       // 2026-06-12T15:00+02:00[Europe/Rome]
            show("viaOf", viaOf);
            ZonedDateTime withSeconds = checkIn.atTime(15, 0, 30).atZone(ZoneId.of("Europe/Rome"));      // 2026-06-12T15:00:30+02:00[Europe/Rome]
            show("withSeconds", withSeconds);
        }
        {
            ZonedDateTime booked = ZonedDateTime.of(2026, 6, 11, 23, 30, 0, 0, ZoneId.of("America/New_York"));
            LocalDate inNewYork = booked.toLocalDate();                                                     // 2026-06-11
            show("inNewYork", inNewYork);
            LocalDate inRome = booked.withZoneSameInstant(ZoneId.of("Europe/Rome")).toLocalDate();          // 2026-06-12
            show("inRome", inRome);
            LocalDate fromInstant = LocalDate.ofInstant(booked.toInstant(), ZoneId.of("Europe/Rome"));      // 2026-06-12
            show("fromInstant", fromInstant);
        }
        {
            Hotel romeHotel = new Hotel(ZoneId.of("Europe/Rome"), LocalTime.of(15, 0));
            LocalDate stay = LocalDate.of(2026, 6, 12);
            ZonedDateTime forTokyoGuest = checkInFor(romeHotel, stay, ZoneId.of("Asia/Tokyo"));              // 2026-06-12T22:00+09:00[Asia/Tokyo]
            show("forTokyoGuest", forTokyoGuest);
            ZonedDateTime cancelBy = stay.minusDays(1).atTime(18, 0).atZone(romeHotel.zone());               // 2026-06-11T18:00+02:00[Europe/Rome]
            show("cancelBy", cancelBy);
            boolean canCancel = Instant.parse("2026-06-11T15:30:00Z").isBefore(cancelBy.toInstant());         // true
            show("canCancel", canCancel);
        }
        {
            Instant romeMidnight = LocalDate.of(2026, 6, 12).atStartOfDay(ZoneId.of("Europe/Rome")).toInstant();   // 2026-06-11T22:00:00Z
            show("romeMidnight", romeMidnight);
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
