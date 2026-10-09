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
 * Examples for the tutorial "Get the Day of Week From a Date in Java (DayOfWeek)".
 * https://howtodoinjava.com/java/date-time/finding-day-of-week/
 */
public class DayOfWeekExample {
    static Optional<DayOfWeek> dayOfWeek(String text, DateTimeFormatter format) {
        if (text == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(LocalDate.parse(text.strip(), format).getDayOfWeek());
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }
    static Optional<LocalTime> lastSlot(LocalDate date) {
        return switch (date.getDayOfWeek()) {
            case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> Optional.of(LocalTime.of(20, 0));
            case SATURDAY -> Optional.of(LocalTime.of(14, 0));
            case SUNDAY -> Optional.empty();
        };
    }
    public static void main(String[] args) throws Exception {
        {
            LocalDate deliveryDate = LocalDate.of(2026, 6, 18);
            DayOfWeek day = deliveryDate.getDayOfWeek();                       // THURSDAY
            show("day", day);
            int isoNumber = day.getValue();                                    // 4
            show("isoNumber", isoNumber);
            String english = day.getDisplayName(TextStyle.FULL, Locale.US);    // "Thursday"
            show("english", english);
            String german = day.getDisplayName(TextStyle.FULL, Locale.GERMAN); // "Donnerstag"
            show("german", german);
        }
        {
            DayOfWeek friday = DayOfWeek.of(5);                  // FRIDAY
            show("friday", friday);
            int value = friday.getValue();                       // 5
            show("value", value);
            int position = friday.ordinal();                     // 4
            show("position", position);
            DayOfWeek afterSunday = DayOfWeek.SUNDAY.plus(1);    // MONDAY
            show("afterSunday", afterSunday);
            DayOfWeek beforeMonday = DayOfWeek.MONDAY.minus(1);  // SUNDAY
            show("beforeMonday", beforeMonday);
            DayOfWeek parsed = DayOfWeek.valueOf("FRIDAY");      // FRIDAY
            show("parsed", parsed);
            try { DayOfWeek invalid = DayOfWeek.of(0); show("invalid", invalid); } catch (Throwable _t) { System.out.println("invalid -> " + _t); }
        }
        {
            LocalDateTime pickup = LocalDateTime.of(2026, 6, 18, 9, 30);
            DayOfWeek fromDateTime = pickup.getDayOfWeek();                       // THURSDAY
            show("fromDateTime", fromDateTime);
            ZonedDateTime meeting = ZonedDateTime.of(2026, 6, 21, 10, 0, 0, 0, ZoneId.of("Europe/Paris"));
            DayOfWeek fromZoned = meeting.getDayOfWeek();                         // SUNDAY
            show("fromZoned", fromZoned);
            DayOfWeek fromAnyTemporal = DayOfWeek.from(pickup);                   // THURSDAY
            show("fromAnyTemporal", fromAnyTemporal);
            int fieldValue = pickup.get(ChronoField.DAY_OF_WEEK);                 // 4
            show("fieldValue", fieldValue);
        }
        {
            Instant orderTime = Instant.parse("2026-06-19T02:30:00Z");
            DayOfWeek inUtc = orderTime.atZone(ZoneOffset.UTC).getDayOfWeek();                       // FRIDAY
            show("inUtc", inUtc);
            DayOfWeek inLosAngeles = orderTime.atZone(ZoneId.of("America/Los_Angeles")).getDayOfWeek(); // THURSDAY
            show("inLosAngeles", inLosAngeles);
            DayOfWeek today = LocalDate.now(ZoneId.of("Asia/Kolkata")).getDayOfWeek();              // today's day in India
            show("today", today);
        }
        {
            DayOfWeek isoText = LocalDate.parse("2026-06-18").getDayOfWeek();                              // THURSDAY
            show("isoText", isoText);
            DateTimeFormatter dayFirst = DateTimeFormatter.ofPattern("dd/MM/uuuu");
            DayOfWeek customText = LocalDate.parse("21/06/2026", dayFirst).getDayOfWeek();                // SUNDAY
            show("customText", customText);
            try { DayOfWeek badText = LocalDate.parse("2026-13-01").getDayOfWeek(); show("badText", badText); } catch (Throwable _t) { System.out.println("badText -> " + _t); }
        }
        {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/uuuu");
            Optional<DayOfWeek> valid = dayOfWeek(" 18/06/2026 ", format);     // Optional[THURSDAY]
            show("valid", valid);
            Optional<DayOfWeek> invalid = dayOfWeek("2026-06-18", format);     // Optional.empty
            show("invalid", invalid);
        }
        {
            LocalDate thursday = LocalDate.of(2026, 6, 18);
            int iso = thursday.getDayOfWeek().getValue();                                       // 4
            show("iso", iso);
            int us = thursday.get(WeekFields.of(Locale.US).dayOfWeek());                        // 5
            show("us", us);
            int uk = thursday.get(WeekFields.of(Locale.UK).dayOfWeek());                        // 4
            show("uk", uk);
            String usPattern = thursday.format(DateTimeFormatter.ofPattern("e", Locale.US));    // "5"
            show("usPattern", usPattern);
            int legacy = new GregorianCalendar(2026, Calendar.JUNE, 18).get(Calendar.DAY_OF_WEEK);   // 5
            show("legacy", legacy);
        }
        {
            Date legacyDate = Date.from(Instant.parse("2026-06-18T10:00:00Z"));
            LocalDate converted = legacyDate.toInstant().atZone(ZoneId.of("UTC")).toLocalDate();   // 2026-06-18
            show("converted", converted);
            DayOfWeek fromLegacy = converted.getDayOfWeek();                                        // THURSDAY
            show("fromLegacy", fromLegacy);
        }
        {
            int calendarDay = Calendar.SUNDAY;                       // 1
            show("calendarDay", calendarDay);
            DayOfWeek mapped = DayOfWeek.of((calendarDay + 5) % 7 + 1);   // SUNDAY
            show("mapped", mapped);
        }
        {
            Optional<LocalTime> thursdaySlot = lastSlot(LocalDate.of(2026, 6, 18));   // Optional[20:00]
            show("thursdaySlot", thursdaySlot);
            Optional<LocalTime> saturdaySlot = lastSlot(LocalDate.of(2026, 6, 20));   // Optional[14:00]
            show("saturdaySlot", saturdaySlot);
            Optional<LocalTime> sundaySlot = lastSlot(LocalDate.of(2026, 6, 21));     // Optional.empty
            show("sundaySlot", sundaySlot);
            LocalDate nextMonday = LocalDate.of(2026, 6, 21).with(TemporalAdjusters.next(DayOfWeek.MONDAY));   // 2026-06-22
            show("nextMonday", nextMonday);
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
