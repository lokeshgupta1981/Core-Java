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
 * Examples for the tutorial "Add or Subtract Hours, Minutes and Seconds in Java".
 * https://howtodoinjava.com/java/date-time/add-subtract-hours-minutes-seconds/
 */
public class AddHoursMinutesSecondsToDate {

    public static void main(String[] args) throws Exception {
        {
            LocalDateTime meeting = LocalDateTime.of(2026, 10, 10, 9, 30);
            LocalDateTime later = meeting.plusHours(2);                     // 2026-10-10T11:30
            show("later", later);
            LocalDateTime earlier = meeting.minusMinutes(45);               // 2026-10-10T08:45
            show("earlier", earlier);
            LocalDateTime withSeconds = meeting.plusSeconds(90);            // 2026-10-10T09:31:30
            show("withSeconds", withSeconds);
            LocalDateTime longCall = meeting.plus(Duration.ofMinutes(150)); // 2026-10-10T12:00
            show("longCall", longCall);
            Instant expiresAt = Instant.parse("2026-10-10T09:30:00Z").plus(2, ChronoUnit.HOURS);   // 2026-10-10T11:30:00Z
            show("expiresAt", expiresAt);
        }
        {
            LocalTime lateShift = LocalTime.of(23, 30);
            LocalTime shiftEnd = lateShift.plusHours(2);                    // 01:30
            show("shiftEnd", shiftEnd);
            LocalDateTime nightShift = LocalDateTime.of(2026, 10, 10, 23, 30);
            LocalDateTime nightEnd = nightShift.plusHours(2);               // 2026-10-11T01:30
            show("nightEnd", nightEnd);
            LocalDateTime cutoff = nightShift.minusSeconds(30);             // 2026-10-10T23:29:30
            show("cutoff", cutoff);
        }
        {
            Duration sessionLength = Duration.parse("PT1H30M");             // PT1H30M
            show("sessionLength", sessionLength);
            LocalDateTime login = LocalDateTime.of(2026, 10, 10, 22, 45);
            LocalDateTime logout = login.plus(sessionLength);               // 2026-10-11T00:15
            show("logout", logout);
            LocalDateTime warning = logout.minus(Duration.ofMinutes(5));    // 2026-10-11T00:10
            show("warning", warning);
            LocalDateTime halfDay = login.plus(1, ChronoUnit.HALF_DAYS);    // 2026-10-11T10:45
            show("halfDay", halfDay);
        }
        {
            LocalDateTime request = LocalDateTime.of(2026, 10, 10, 9, 30, 15);
            LocalDateTime retryAt = request.plus(Duration.ofMillis(2500));        // 2026-10-10T09:30:17.500
            show("retryAt", retryAt);
            LocalDateTime sameRetry = request.plus(2500, ChronoUnit.MILLIS);      // 2026-10-10T09:30:17.500
            show("sameRetry", sameRetry);
            LocalDateTime minuteOnly = retryAt.truncatedTo(ChronoUnit.MINUTES);   // 2026-10-10T09:30
            show("minuteOnly", minuteOnly);
        }
        {
            Instant issued = Instant.parse("2026-10-10T09:30:00Z");
            Instant accessExpiry = issued.plus(15, ChronoUnit.MINUTES);      // 2026-10-10T09:45:00Z
            show("accessExpiry", accessExpiry);
            Instant refreshExpiry = issued.plus(Duration.ofHours(12));       // 2026-10-10T21:30:00Z
            show("refreshExpiry", refreshExpiry);
            Instant skewed = issued.minusSeconds(30);                        // 2026-10-10T09:29:30Z
            show("skewed", skewed);
            Instant nextDay = issued.plus(1, ChronoUnit.DAYS);               // 2026-10-11T09:30:00Z
            show("nextDay", nextDay);
        }
        {
            Instant tokenIssued = Instant.parse("2026-10-10T09:30:00Z");
            try { Instant monthLater = tokenIssued.plus(1, ChronoUnit.MONTHS); show("monthLater", monthLater); } catch (Throwable _t) { System.out.println("monthLater -> " + _t); }
        }
        {
            ZoneId newYork = ZoneId.of("America/New_York");
            ZonedDateTime beforeJump = ZonedDateTime.of(2026, 3, 8, 1, 30, 0, 0, newYork);
            ZonedDateTime oneHourLater = beforeJump.plusHours(1);           // 2026-03-08T03:30-04:00[America/New_York]
            show("oneHourLater", oneHourLater);
            LocalDateTime naive = beforeJump.toLocalDateTime().plusHours(1); // 2026-03-08T02:30
            show("naive", naive);
        }
        {
            ZoneId newYork = ZoneId.of("America/New_York");
            ZonedDateTime firstPass = ZonedDateTime.of(2026, 11, 1, 1, 30, 0, 0, newYork);   // 2026-11-01T01:30-04:00[America/New_York]
            show("firstPass", firstPass);
            ZonedDateTime secondPass = firstPass.plusHours(1);                                 // 2026-11-01T01:30-05:00[America/New_York]
            show("secondPass", secondPass);
            ZonedDateTime evening = ZonedDateTime.of(2026, 10, 31, 18, 0, 0, 0, newYork);
            ZonedDateTime plusDay = evening.plusDays(1);                    // 2026-11-01T18:00-05:00[America/New_York]
            show("plusDay", plusDay);
            ZonedDateTime plus24h = evening.plusHours(24);                  // 2026-11-01T17:00-05:00[America/New_York]
            show("plus24h", plus24h);
        }
        {
            ZoneId newYork = ZoneId.of("America/New_York");
            ZonedDateTime departure = ZonedDateTime.of(2026, 3, 8, 4, 0, 0, 0, newYork);
            ZonedDateTime reminder = departure.minusHours(3);               // 2026-03-08T00:00-05:00[America/New_York]
            show("reminder", reminder);
            LocalDateTime wrongReminder = departure.toLocalDateTime().minusHours(3);   // 2026-03-08T01:00
            show("wrongReminder", wrongReminder);
        }
        {
            Date legacyStart = Date.from(Instant.parse("2026-10-10T09:30:00Z"));
            Instant shifted = legacyStart.toInstant().plus(Duration.ofHours(2)).minusSeconds(15);   // 2026-10-10T11:29:45Z
            show("shifted", shifted);
            Date legacyEnd = Date.from(shifted);
            long endMillis = legacyEnd.getTime();                           // 1791631785000
            show("endMillis", endMillis);
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
