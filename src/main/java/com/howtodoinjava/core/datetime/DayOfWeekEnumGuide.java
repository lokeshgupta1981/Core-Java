package com.howtodoinjava.core.datetime;

import java.util.concurrent.locks.*;
import java.lang.management.*;
import java.time.format.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.zip.*;
import java.util.regex.*;
import java.net.*;
import java.text.*;
import java.nio.file.attribute.*;
import java.security.*;
import java.nio.charset.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.time.temporal.*;

/**
 * Examples for the tutorial "Java DayOfWeek Enum: Methods, Day Names and Examples".
 * https://howtodoinjava.com/java/date-time/find-dayofweek/
 */
public class DayOfWeekEnumGuide {
    static Optional<DayOfWeek> parseDay(String text) {
        if (text == null || text.isBlank()) {
            return Optional.empty();
        }
        String key = text.strip().toLowerCase(Locale.ROOT);
        for (DayOfWeek d : DayOfWeek.values()) {
            if (d.getDisplayName(TextStyle.FULL, Locale.US).toLowerCase(Locale.ROOT).equals(key)
                    || d.getDisplayName(TextStyle.SHORT, Locale.US).toLowerCase(Locale.ROOT).equals(key)) {
                return Optional.of(d);
            }
        }
        return Optional.empty();
    }
    static List<String> headerRow(Locale locale) {
        DayOfWeek first = WeekFields.of(locale).getFirstDayOfWeek();
        List<String> row = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            row.add(first.plus(i).getDisplayName(TextStyle.SHORT, locale));
        }
        return row;
    }
    static List<LocalDate> nextSessions(LocalDate today, Set<DayOfWeek> days, int count) {
        if (days.isEmpty()) {
            throw new IllegalArgumentException("No class days");
        }
        List<LocalDate> result = new ArrayList<>();
        LocalDate date = today;
        while (result.size() < count) {
            date = date.plusDays(1);
            if (days.contains(date.getDayOfWeek())) {
                result.add(date);
            }
        }
        return result;
    }
    static int surchargePercent(DayOfWeek day) {
        return switch (day) {
            case MONDAY, TUESDAY, WEDNESDAY, THURSDAY -> 0;
            case FRIDAY -> 10;
            case SATURDAY, SUNDAY -> 20;
        };
    }
    public static void main(String[] args) throws Exception {
        {
            DayOfWeek day = DayOfWeek.of(5);                                   // FRIDAY
            show("day", day);
            int iso = day.getValue();                                          // 5
            show("iso", iso);
            DayOfWeek inThreeDays = day.plus(3);                               // MONDAY
            show("inThreeDays", inThreeDays);
            String full = day.getDisplayName(TextStyle.FULL, Locale.US);       // "Friday"
            show("full", full);
            String shortName = day.getDisplayName(TextStyle.SHORT, Locale.US); // "Fri"
            show("shortName", shortName);
            DayOfWeek fromName = DayOfWeek.valueOf("FRIDAY");                  // FRIDAY
            show("fromName", fromName);
            boolean weekend = day.getValue() >= 6;                             // false
            show("weekend", weekend);
        }
        {
            DayOfWeek[] week = DayOfWeek.values();                // [MONDAY, TUESDAY, ..., SUNDAY]
            show("week", week);
            int days = week.length;                               // 7
            show("days", days);
            int sundayValue = DayOfWeek.SUNDAY.getValue();        // 7
            show("sundayValue", sundayValue);
            int sundayOrdinal = DayOfWeek.SUNDAY.ordinal();       // 6
            show("sundayOrdinal", sundayOrdinal);
            DayOfWeek back = DayOfWeek.MONDAY.minus(1);           // SUNDAY
            show("back", back);
            DayOfWeek far = DayOfWeek.MONDAY.plus(15);            // TUESDAY
            show("far", far);
            DayOfWeek negative = DayOfWeek.SUNDAY.plus(-1);       // SATURDAY
            show("negative", negative);
            try { DayOfWeek bad = DayOfWeek.of(8); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
        }
        {
            boolean before = DayOfWeek.TUESDAY.compareTo(DayOfWeek.FRIDAY) < 0;   // true
            show("before", before);
            boolean same = DayOfWeek.of(7) == DayOfWeek.SUNDAY;                     // true
            show("same", same);
            int gap = DayOfWeek.FRIDAY.getValue() - DayOfWeek.TUESDAY.getValue();   // 3
            show("gap", gap);
        }
        {
            DayOfWeek day = DayOfWeek.WEDNESDAY;
            String full = day.getDisplayName(TextStyle.FULL, Locale.US);         // "Wednesday"
            show("full", full);
            String abbr = day.getDisplayName(TextStyle.SHORT, Locale.US);        // "Wed"
            show("abbr", abbr);
            String letter = day.getDisplayName(TextStyle.NARROW, Locale.US);     // "W"
            show("letter", letter);
            String german = day.getDisplayName(TextStyle.FULL, Locale.GERMAN);   // "Mittwoch"
            show("german", german);
            String french = day.getDisplayName(TextStyle.SHORT, Locale.FRENCH);  // "mer."
            show("french", french);
            String constant = day.toString();                                    // "WEDNESDAY"
            show("constant", constant);
        }
        {
            DayOfWeek upper = DayOfWeek.valueOf("FRIDAY");                  // FRIDAY
            show("upper", upper);
            try { DayOfWeek mixed = DayOfWeek.valueOf("Friday"); show("mixed", mixed); } catch (Throwable _t) { System.out.println("mixed -> " + _t); }
            DayOfWeek fixed = DayOfWeek.valueOf("friday".toUpperCase(Locale.ROOT));   // FRIDAY
            show("fixed", fixed);
        }
        {
            DateTimeFormatter fullNames = DateTimeFormatter.ofPattern("EEEE", Locale.US);
            DateTimeFormatter shortNames = DateTimeFormatter.ofPattern("EEE", Locale.US);
            DayOfWeek fromFull = DayOfWeek.from(fullNames.parse("Friday"));      // FRIDAY
            show("fromFull", fromFull);
            DayOfWeek fromShort = DayOfWeek.from(shortNames.parse("Fri"));       // FRIDAY
            show("fromShort", fromShort);
            DateTimeFormatter germanNames = DateTimeFormatter.ofPattern("EEEE", Locale.GERMAN);
            DayOfWeek fromGerman = DayOfWeek.from(germanNames.parse("Montag"));  // MONDAY
            show("fromGerman", fromGerman);
        }
        {
            Optional<DayOfWeek> one = parseDay(" friday ");     // Optional[FRIDAY]
            show("one", one);
            Optional<DayOfWeek> two = parseDay("SAT");           // Optional[SATURDAY]
            show("two", two);
            Optional<DayOfWeek> three = parseDay("Fryday");      // Optional.empty
            show("three", three);
            Optional<DayOfWeek> four = parseDay(null);           // Optional.empty
            show("four", four);
        }
        {
            Set<DayOfWeek> weekdays = EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.FRIDAY);   // [MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY]
            show("weekdays", weekdays);
            Set<DayOfWeek> weekend = EnumSet.complementOf(EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.FRIDAY));   // [SATURDAY, SUNDAY]
            show("weekend", weekend);
            Set<DayOfWeek> classDays = EnumSet.of(DayOfWeek.THURSDAY, DayOfWeek.TUESDAY);   // [TUESDAY, THURSDAY]
            show("classDays", classDays);
            boolean saturdayClass = classDays.contains(DayOfWeek.SATURDAY);                  // false
            show("saturdayClass", saturdayClass);
        }
        {
            Map<DayOfWeek, String> hours = new EnumMap<>(DayOfWeek.class);
            hours.put(DayOfWeek.SATURDAY, "10-14");
            hours.put(DayOfWeek.MONDAY, "9-18");
            hours.put(DayOfWeek.FRIDAY, "9-20");
            String keys = hours.keySet().toString();                              // "[MONDAY, FRIDAY, SATURDAY]"
            show("keys", keys);
            String sunday = hours.getOrDefault(DayOfWeek.SUNDAY, "closed");      // "closed"
            show("sunday", sunday);
        }
        {
            DayOfWeek us = WeekFields.of(Locale.US).getFirstDayOfWeek();               // SUNDAY
            show("us", us);
            DayOfWeek germany = WeekFields.of(Locale.GERMANY).getFirstDayOfWeek();     // MONDAY
            show("germany", germany);
            DayOfWeek iso = WeekFields.ISO.getFirstDayOfWeek();                        // MONDAY
            show("iso", iso);
        }
        {
            List<String> usRow = headerRow(Locale.US);           // [Sun, Mon, Tue, Wed, Thu, Fri, Sat]
            show("usRow", usRow);
            List<String> ukRow = headerRow(Locale.UK);           // [Mon, Tue, Wed, Thu, Fri, Sat, Sun]
            show("ukRow", ukRow);
        }
        {
            LocalDate start = LocalDate.of(2026, 10, 10);                                        // a Saturday
            show("start", start);
            LocalDate nextMonday = start.with(TemporalAdjusters.next(DayOfWeek.MONDAY));         // 2026-10-12
            show("nextMonday", nextMonday);
            LocalDate sameOrNext = start.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY)); // 2026-10-10
            show("sameOrNext", sameOrNext);
            LocalDate lastFriday = start.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));     // 2026-10-09
            show("lastFriday", lastFriday);
            LocalDate secondTuesday = start.with(TemporalAdjusters.dayOfWeekInMonth(2, DayOfWeek.TUESDAY));   // 2026-10-13
            show("secondTuesday", secondTuesday);
            LocalDate lastFridayOfMonth = start.with(TemporalAdjusters.lastInMonth(DayOfWeek.FRIDAY));       // 2026-10-30
            show("lastFridayOfMonth", lastFridayOfMonth);
        }
        {
            LocalDate sunday = LocalDate.of(2026, 10, 11);
            LocalDate sameWeekMonday = sunday.with(DayOfWeek.MONDAY);                        // 2026-10-05
            show("sameWeekMonday", sameWeekMonday);
            LocalDate followingMonday = sunday.with(TemporalAdjusters.next(DayOfWeek.MONDAY));   // 2026-10-12
            show("followingMonday", followingMonday);
        }
        {
            Set<DayOfWeek> workDays = EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.FRIDAY);
            LocalDate from = LocalDate.of(2026, 10, 1);
            LocalDate to = LocalDate.of(2026, 11, 1);
            long weekdaysInOctober = from.datesUntil(to).filter(d -> workDays.contains(d.getDayOfWeek())).count();   // 22
            show("weekdaysInOctober", weekdaysInOctober);
            long mondays = from.datesUntil(to).filter(d -> d.getDayOfWeek() == DayOfWeek.MONDAY).count();             // 4
            show("mondays", mondays);
        }
        {
            Set<DayOfWeek> spin = EnumSet.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY);
            LocalDate today = LocalDate.of(2026, 10, 10);
            List<LocalDate> sessions = nextSessions(today, spin, 3);                                   // [2026-10-13, 2026-10-15, 2026-10-20]
            show("sessions", sessions);
            String label = sessions.get(0).getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.US);   // "Tue"
            show("label", label);
        }
        {
            int friday = surchargePercent(DayOfWeek.FRIDAY);    // 10
            show("friday", friday);
            int sunday = surchargePercent(DayOfWeek.SUNDAY);    // 20
            show("sunday", sunday);
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
