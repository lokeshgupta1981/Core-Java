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
 * Examples for the tutorial "Split a Date-Time Range into Equal Intervals in Java".
 * https://howtodoinjava.com/java/date-time/split-date-time-range-into-intervals/
 */
public class SplitDateRangeIntoEqualIntervals {
    static record Slot(LocalDateTime start, LocalDateTime end) {
        @Override
        public String toString() {
            return start + "/" + end;
        }
    }
    static List<Slot> splitIntoEqualParts(LocalDateTime start, LocalDateTime end, int parts) {
        if (parts < 1 || !start.isBefore(end)) {
            throw new IllegalArgumentException("start must be before end and parts must be at least 1");
        }
        Duration total = Duration.between(start, end);
        List<Slot> slots = new ArrayList<>(parts);
        for (int i = 0; i < parts; i++) {
            LocalDateTime from = start.plus(total.multipliedBy(i).dividedBy(parts));
            LocalDateTime to = start.plus(total.multipliedBy(i + 1).dividedBy(parts));
            slots.add(new Slot(from, to));
        }
        return slots;
    }
    static record DateRange(LocalDate start, LocalDate end) {
        @Override
        public String toString() {
            return start + "/" + end;
        }
    }
    static List<DateRange> splitBy(LocalDate start, LocalDate end, UnaryOperator<LocalDate> nextBoundary) {
        List<DateRange> ranges = new ArrayList<>();
        LocalDate from = start;
        while (from.isBefore(end)) {
            LocalDate boundary = nextBoundary.apply(from);
            LocalDate to = boundary.isBefore(end) ? boundary : end;
            ranges.add(new DateRange(from, to));
            from = to;
        }
        return ranges;
    }
    static record YearWeek(int year, int week) {
        static YearWeek from(LocalDate date) {
            return new YearWeek(date.get(IsoFields.WEEK_BASED_YEAR), date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
        }

        @Override
        public String toString() {
            return "%d-W%02d".formatted(year, week);
        }
    }
    static List<Slot> splitByDuration(LocalDateTime start, LocalDateTime end, Duration step) {
        if (step.isZero() || step.isNegative()) {
            throw new IllegalArgumentException("step must be positive");
        }
        List<Slot> slots = new ArrayList<>();
        for (LocalDateTime from = start; from.isBefore(end); from = from.plus(step)) {
            LocalDateTime to = from.plus(step);
            slots.add(new Slot(from, to.isBefore(end) ? to : end));
        }
        return slots;
    }
    static List<Duration> dayLengths(ZonedDateTime start, ZonedDateTime end) {
        List<Duration> lengths = new ArrayList<>();
        ZonedDateTime from = start;
        while (from.isBefore(end)) {
            ZonedDateTime midnight = from.toLocalDate().plusDays(1).atStartOfDay(from.getZone());
            ZonedDateTime to = midnight.isBefore(end) ? midnight : end;
            lengths.add(Duration.between(from, to));
            from = to;
        }
        return lengths;
    }
    public static void main(String[] args) throws Exception {
        {
            LocalDateTime start = LocalDateTime.of(2026, 1, 1, 0, 0);
            LocalDateTime end = LocalDateTime.of(2026, 1, 1, 10, 0);
            List<Slot> parts = splitIntoEqualParts(start, end, 4);
            int count = parts.size();                            // 4
            show("count", count);
            String first = parts.get(0).toString();              // "2026-01-01T00:00/2026-01-01T02:30"
            show("first", first);
            String last = parts.get(3).toString();               // "2026-01-01T07:30/2026-01-01T10:00"
            show("last", last);
        }
        {
            LocalDateTime start = LocalDateTime.of(2026, 1, 1, 0, 0);
            LocalDateTime end = LocalDateTime.of(2026, 1, 1, 10, 0);
            String middleThird = splitIntoEqualParts(start, end, 3).get(1).toString();   // "2026-01-01T03:20/2026-01-01T06:40"
            show("middleThird", middleThird);
            int quarterYear = splitIntoEqualParts(LocalDateTime.of(2026, 1, 1, 0, 0), LocalDateTime.of(2026, 4, 1, 0, 0), 6).size();   // 6
            show("quarterYear", quarterYear);
            try { List<Slot> reversed = splitIntoEqualParts(end, start, 3); show("reversed", reversed); } catch (Throwable _t) { System.out.println("reversed -> " + _t); }
        }
        {
            Duration day = Duration.ofDays(1);
            Duration piece = day.dividedBy(7);                   // PT3H25M42.857142857S
            show("piece", piece);
            Duration sevenPieces = piece.multipliedBy(7);        // PT23H59M59.999999999S
            show("sevenPieces", sevenPieces);
            Duration exact = day.multipliedBy(7).dividedBy(7);   // PT24H
            show("exact", exact);
        }
        {
            LocalDate from = LocalDate.of(2026, 3, 1);
            LocalDate to = LocalDate.of(2026, 3, 5);
            List<LocalDate> days = from.datesUntil(to).toList();                      // [2026-03-01, 2026-03-02, 2026-03-03, 2026-03-04]
            show("days", days);
            List<LocalDate> everyOther = from.datesUntil(to, Period.ofDays(2)).toList();   // [2026-03-01, 2026-03-03]
            show("everyOther", everyOther);
            long dayCount = from.datesUntil(to).count();                              // 4
            show("dayCount", dayCount);
        }
        {
            LocalDate from = LocalDate.of(2026, 1, 15);
            LocalDate to = LocalDate.of(2026, 4, 10);
            List<DateRange> months = splitBy(from, to, d -> d.with(TemporalAdjusters.firstDayOfNextMonth()));   // [2026-01-15/2026-02-01, 2026-02-01/2026-03-01, 2026-03-01/2026-04-01, 2026-04-01/2026-04-10]
            show("months", months);
            List<YearMonth> labels = months.stream().map(r -> YearMonth.from(r.start())).toList();               // [2026-01, 2026-02, 2026-03, 2026-04]
            show("labels", labels);
        }
        {
            long completeMonths = ChronoUnit.MONTHS.between(LocalDate.of(2026, 1, 15), LocalDate.of(2026, 4, 10));   // 2
            show("completeMonths", completeMonths);
        }
        {
            LocalDate from = LocalDate.of(2025, 7, 1);
            LocalDate to = LocalDate.of(2027, 3, 1);
            List<DateRange> years = splitBy(from, to, d -> d.with(TemporalAdjusters.firstDayOfNextYear()));   // [2025-07-01/2026-01-01, 2026-01-01/2027-01-01, 2027-01-01/2027-03-01]
            show("years", years);
            List<Year> yearLabels = years.stream().map(r -> Year.from(r.start())).toList();                  // [2025, 2026, 2027]
            show("yearLabels", yearLabels);
        }
        {
            LocalDate from = LocalDate.of(2026, 12, 23);
            LocalDate to = LocalDate.of(2027, 1, 6);
            List<DateRange> weeks = splitBy(from, to, d -> d.with(TemporalAdjusters.next(DayOfWeek.MONDAY)));   // [2026-12-23/2026-12-28, 2026-12-28/2027-01-04, 2027-01-04/2027-01-06]
            show("weeks", weeks);
            List<YearWeek> weekLabels = weeks.stream().map(r -> YearWeek.from(r.start())).toList();             // [2026-W52, 2026-W53, 2027-W01]
            show("weekLabels", weekLabels);
            int alignedWeek = LocalDate.of(2027, 1, 1).get(ChronoField.ALIGNED_WEEK_OF_YEAR);                 // 1
            show("alignedWeek", alignedWeek);
            int isoWeek = LocalDate.of(2027, 1, 1).get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);                     // 53
            show("isoWeek", isoWeek);
        }
        {
            LocalDateTime opens = LocalDateTime.of(2026, 5, 4, 9, 0);
            LocalDateTime closes = LocalDateTime.of(2026, 5, 4, 10, 40);
            List<Slot> slots = splitByDuration(opens, closes, Duration.ofMinutes(30));
            int slotCount = slots.size();                        // 4
            show("slotCount", slotCount);
            String shortSlot = slots.get(3).toString();          // "2026-05-04T10:30/2026-05-04T10:40"
            show("shortSlot", shortSlot);
            long fullSlots = slots.stream().filter(s -> Duration.between(s.start(), s.end()).equals(Duration.ofMinutes(30))).count();   // 3
            show("fullSlots", fullSlots);
        }
        {
            ZoneId newYork = ZoneId.of("America/New_York");
            ZonedDateTime from = ZonedDateTime.of(2026, 3, 7, 0, 0, 0, 0, newYork);
            ZonedDateTime to = ZonedDateTime.of(2026, 3, 10, 0, 0, 0, 0, newYork);
            List<Duration> lengths = dayLengths(from, to);                         // [PT24H, PT23H, PT24H]
            show("lengths", lengths);
            ZonedDateTime fixedCut = from.plus(Duration.ofHours(48));              // 2026-03-09T01:00-04:00[America/New_York]
            show("fixedCut", fixedCut);
        }
        {
            LocalDate quarterStart = LocalDate.of(2026, 1, 1);
            LocalDate quarterEnd = LocalDate.of(2026, 4, 1);
            List<DateRange> windows = splitBy(quarterStart, quarterEnd, d -> d.plusDays(31));
            int requests = windows.size();                       // 3
            show("requests", requests);
            String lastWindow = windows.get(2).toString();       // "2026-03-04/2026-04-01"
            show("lastWindow", lastWindow);
            long longest = windows.stream().mapToLong(w -> ChronoUnit.DAYS.between(w.start(), w.end())).max().orElse(0);   // 31
            show("longest", longest);
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
