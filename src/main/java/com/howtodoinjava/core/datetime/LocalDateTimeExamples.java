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
 * Examples for the tutorial "Java LocalDateTime: Date and Time Without a Time Zone".
 * https://howtodoinjava.com/java/date-time/java-localdatetime-class/
 */
public class LocalDateTimeExamples {
    static record Appointment(String patient, LocalDateTime start, ZoneId clinicZone) {
        Instant reminderAt() {
            return start.atZone(clinicZone).minusHours(24).toInstant();
        }
    }
    public static void main(String[] args) throws Exception {
        {
            LocalDateTime appointment = LocalDateTime.of(2026, 5, 14, 9, 30);        // 2026-05-14T09:30
            show("appointment", appointment);
            LocalDateTime nextSlot = LocalDateTime.parse("2026-05-14T11:00");          // 2026-05-14T11:00
            show("nextSlot", nextSlot);
            LocalDateTime reminder = appointment.minusHours(24);                       // 2026-05-13T09:30
            show("reminder", reminder);
            boolean earlier = appointment.isBefore(nextSlot);                          // true
            show("earlier", earlier);
            long gapMinutes = Duration.between(appointment, nextSlot).toMinutes();     // 90
            show("gapMinutes", gapMinutes);
            String shown = appointment.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a", Locale.US));   // "14 May 2026, 09:30 AM"
            show("shown", shown);
            ZonedDateTime inBerlin = appointment.atZone(ZoneId.of("Europe/Berlin"));   // 2026-05-14T09:30+02:00[Europe/Berlin]
            show("inBerlin", inBerlin);
        }
        {
            LocalDateTime now = LocalDateTime.now();                                     // current date-time, default zone
            show("now", now);
            LocalDateTime utcNow = LocalDateTime.now(ZoneId.of("UTC"));                   // current date-time in UTC
            show("utcNow", utcNow);
            LocalDateTime fixed = LocalDateTime.now(Clock.fixed(Instant.parse("2026-05-14T07:30:00Z"), ZoneId.of("Europe/Berlin")));   // 2026-05-14T09:30
            show("fixed", fixed);
        }
        {
            LocalDateTime minutes = LocalDateTime.of(2026, 5, 14, 9, 30);                     // 2026-05-14T09:30
            show("minutes", minutes);
            LocalDateTime seconds = LocalDateTime.of(2026, Month.MAY, 14, 9, 30, 15);          // 2026-05-14T09:30:15
            show("seconds", seconds);
            LocalDateTime nanos = LocalDateTime.of(2026, 5, 14, 9, 30, 15, 500_000_000);       // 2026-05-14T09:30:15.500
            show("nanos", nanos);
            try { LocalDateTime bad = LocalDateTime.of(2026, 4, 31, 9, 30); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
        }
        {
            LocalDate date = LocalDate.of(2026, 5, 14);
            LocalTime time = LocalTime.of(9, 30);
            LocalDateTime joined = LocalDateTime.of(date, time);       // 2026-05-14T09:30
            show("joined", joined);
            LocalDateTime fromDate = date.atTime(time);                 // 2026-05-14T09:30
            show("fromDate", fromDate);
            LocalDateTime fromTime = time.atDate(date);                 // 2026-05-14T09:30
            show("fromTime", fromTime);
            LocalDate datePart = joined.toLocalDate();                  // 2026-05-14
            show("datePart", datePart);
            LocalTime timePart = joined.toLocalTime();                  // 09:30
            show("timePart", timePart);
        }
        {
            LocalDateTime iso = LocalDateTime.parse("2026-05-14T09:30:15");      // 2026-05-14T09:30:15
            show("iso", iso);
            DateTimeFormatter withSpace = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime spaced = LocalDateTime.parse("2026-05-14 14:15:30", withSpace);    // 2026-05-14T14:15:30
            show("spaced", spaced);
            DateTimeFormatter twelveHour = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a", Locale.US);
            LocalDateTime pm = LocalDateTime.parse("2026-05-14 02:15:30 PM", twelveHour);    // 2026-05-14T14:15:30
            show("pm", pm);
            DateTimeFormatter mixed = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a", Locale.US);
            try { LocalDateTime conflict = LocalDateTime.parse("2026-05-14 02:15:30 PM", mixed); show("conflict", conflict); } catch (Throwable _t) { System.out.println("conflict -> " + _t); }
        }
        {
            LocalDateTime visit = LocalDateTime.of(2026, 5, 14, 14, 5, 9);
            String isoText = visit.toString();                                                         // "2026-05-14T14:05:09"
            show("isoText", isoText);
            String noT = visit.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));              // "2026-05-14 14:05:09"
            show("noT", noT);
            String us = visit.format(DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a", Locale.US));      // "05/14/2026 2:05 PM"
            show("us", us);
            String french = visit.format(DateTimeFormatter.ofPattern("d MMMM yyyy HH'h'mm", Locale.FRANCE));   // "14 mai 2026 14h05"
            show("french", french);
        }
        {
            LocalDateTime start = LocalDateTime.of(2026, 5, 14, 23, 30);
            LocalDateTime threeHoursLater = start.plusHours(3);                  // 2026-05-15T02:30, next day
            show("threeHoursLater", threeHoursLater);
            LocalDateTime earlier = start.minusMinutes(45);                      // 2026-05-14T22:45
            show("earlier", earlier);
            LocalDateTime nextYear = start.plusYears(1);                         // 2027-05-14T23:30
            show("nextYear", nextYear);
            LocalDateTime morning = start.withHour(8).withMinute(0);             // 2026-05-14T08:00
            show("morning", morning);
            LocalDateTime hourStart = start.plusMinutes(20).truncatedTo(ChronoUnit.HOURS);   // 2026-05-14T23:00
            show("hourStart", hourStart);
        }
        {
            LocalDateTime first = LocalDateTime.of(2026, 5, 14, 9, 30);
            LocalDateTime second = LocalDateTime.of(2026, 5, 15, 11, 0);
            boolean before = first.isBefore(second);                        // true
            show("before", before);
            boolean equal = first.isEqual(LocalDateTime.parse("2026-05-14T09:30"));   // true
            show("equal", equal);
            Duration gap = Duration.between(first, second);                 // PT25H30M
            show("gap", gap);
            long hours = ChronoUnit.HOURS.between(first, second);           // 25
            show("hours", hours);
            boolean sameDay = first.toLocalDate().equals(second.toLocalDate());       // false
            show("sameDay", sameDay);
        }
        {
            LocalDateTime local = LocalDateTime.of(2026, 5, 14, 9, 30);
            ZonedDateTime zoned = local.atZone(ZoneId.of("America/Chicago"));    // 2026-05-14T09:30-05:00[America/Chicago]
            show("zoned", zoned);
            OffsetDateTime offset = local.atOffset(ZoneOffset.UTC);              // 2026-05-14T09:30Z
            show("offset", offset);
            Instant instant = zoned.toInstant();                                 // 2026-05-14T14:30:00Z
            show("instant", instant);
            long epochSecond = local.toEpochSecond(ZoneOffset.UTC);              // 1778751000
            show("epochSecond", epochSecond);
            LocalDateTime fromInstant = LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Kolkata"));   // 2026-05-14T20:00
            show("fromInstant", fromInstant);
        }
        {
            LocalDateTime inGap = LocalDateTime.of(2026, 3, 8, 2, 30);
            ZonedDateTime shifted = inGap.atZone(ZoneId.of("America/New_York"));    // 2026-03-08T03:30-04:00[America/New_York]
            show("shifted", shifted);
        }
        {
            Appointment visit = new Appointment("Lokesh", LocalDateTime.of(2026, 5, 14, 9, 30), ZoneId.of("Europe/Berlin"));
            Instant sendAt = visit.reminderAt();                                 // 2026-05-13T07:30:00Z
            show("sendAt", sendAt);
        }
        {
            LocalDateTime fromMillis = LocalDateTime.ofInstant(Instant.ofEpochMilli(1778751000000L), ZoneOffset.UTC);   // 2026-05-14T09:30
            show("fromMillis", fromMillis);
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
