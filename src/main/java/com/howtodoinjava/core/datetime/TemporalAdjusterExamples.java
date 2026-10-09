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
 * Examples for the tutorial "Java TemporalAdjusters: Built-in and Custom Date Adjusters".
 * https://howtodoinjava.com/java/date-time/java8-temporal-adjusters/
 */
public class TemporalAdjusterExamples {
    static TemporalAdjuster nextWorkingDay() {
        return TemporalAdjusters.ofDateAdjuster(date -> {
            LocalDate next = date.plusDays(1);
            while (next.getDayOfWeek() == DayOfWeek.SATURDAY || next.getDayOfWeek() == DayOfWeek.SUNDAY) {
                next = next.plusDays(1);
            }
            return next;
        });
    }
    static record NextAnniversary(MonthDay day) implements TemporalAdjuster {
        @Override
        public Temporal adjustInto(Temporal temporal) {
            LocalDate date = LocalDate.from(temporal);
            LocalDate candidate = day.atYear(date.getYear());
            if (!candidate.isAfter(date)) {
                candidate = day.atYear(date.getYear() + 1);
            }
            return temporal.with(candidate);
        }
    }
    static TemporalAdjuster lastWorkingDayOfMonth() {
        return TemporalAdjusters.ofDateAdjuster(date -> {
            LocalDate last = date.with(TemporalAdjusters.lastDayOfMonth());
            return switch (last.getDayOfWeek()) {
                case SATURDAY, SUNDAY -> last.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));
                default -> last;
            };
        });
    }
    static List<LocalDate> nextMondays(LocalDate from, int count) {
        return Stream.iterate(from.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY)), d -> d.plusWeeks(1))
                .limit(count)
                .toList();
    }
    public static void main(String[] args) throws Exception {
        {
            LocalDate today = LocalDate.of(2026, 10, 14);                                       // 2026-10-14, a Wednesday
            show("today", today);
            LocalDate monthEnd = today.with(TemporalAdjusters.lastDayOfMonth());                // 2026-10-31
            show("monthEnd", monthEnd);
            LocalDate nextFriday = today.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));        // 2026-10-16
            show("nextFriday", nextFriday);
            LocalDate firstMonday = today.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));   // 2026-10-05
            show("firstMonday", firstMonday);
            LocalDate secondTuesday = today.with(TemporalAdjusters.dayOfWeekInMonth(2, DayOfWeek.TUESDAY));   // 2026-10-13
            show("secondTuesday", secondTuesday);
            LocalDate nextYear = today.with(TemporalAdjusters.firstDayOfNextYear());            // 2027-01-01
            show("nextYear", nextYear);
        }
        {
            LocalDate date = LocalDate.of(2026, 10, 14);
            LocalDate viaWith = date.with(TemporalAdjusters.lastDayOfMonth());                         // 2026-10-31
            show("viaWith", viaWith);
            LocalDate viaAdjustInto = (LocalDate) TemporalAdjusters.lastDayOfMonth().adjustInto(date);   // 2026-10-31
            show("viaAdjustInto", viaAdjustInto);
        }
        {
            LocalDate wednesday = LocalDate.of(2026, 10, 14);
            LocalDate sameWeekMonday = wednesday.with(DayOfWeek.MONDAY);                 // 2026-10-12
            show("sameWeekMonday", sameWeekMonday);
            LocalDate sameWeekSunday = wednesday.with(DayOfWeek.SUNDAY);                 // 2026-10-18
            show("sameWeekSunday", sameWeekSunday);
            LocalDate february = LocalDate.of(2026, 3, 31).with(Month.FEBRUARY);         // 2026-02-28
            show("february", february);
            LocalDate otherMonth = wednesday.with(YearMonth.of(2027, 2));                // 2027-02-14
            show("otherMonth", otherMonth);
            LocalDateTime atNoon = LocalDateTime.of(2026, 10, 14, 9, 30).with(LocalTime.NOON);   // 2026-10-14T12:00
            show("atNoon", atNoon);
        }
        {
            LocalDate leapFebruary = LocalDate.of(2028, 2, 10).with(TemporalAdjusters.lastDayOfMonth());   // 2028-02-29
            show("leapFebruary", leapFebruary);
            LocalDate yearEnd = LocalDate.of(2026, 10, 14).with(TemporalAdjusters.lastDayOfYear());         // 2026-12-31
            show("yearEnd", yearEnd);
            LocalDate nextMonthStart = LocalDate.of(2026, 12, 20).with(TemporalAdjusters.firstDayOfNextMonth());   // 2027-01-01
            show("nextMonthStart", nextMonthStart);
        }
        {
            LocalDate wednesday = LocalDate.of(2026, 10, 14);
            LocalDate nextWednesday = wednesday.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY));         // 2026-10-21
            show("nextWednesday", nextWednesday);
            LocalDate sameWednesday = wednesday.with(TemporalAdjusters.nextOrSame(DayOfWeek.WEDNESDAY));   // 2026-10-14
            show("sameWednesday", sameWednesday);
            LocalDate lastMonday = wednesday.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));           // 2026-10-12
            show("lastMonday", lastMonday);
            LocalDate comingMonday = wednesday.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));       // 2026-10-19
            show("comingMonday", comingMonday);
        }
        {
            LocalDate october = LocalDate.of(2026, 10, 1);
            LocalDate firstMonday = october.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));           // 2026-10-05
            show("firstMonday", firstMonday);
            LocalDate lastFriday = october.with(TemporalAdjusters.lastInMonth(DayOfWeek.FRIDAY));             // 2026-10-30
            show("lastFriday", lastFriday);
            LocalDate lastThursday = october.with(TemporalAdjusters.dayOfWeekInMonth(-1, DayOfWeek.THURSDAY));   // 2026-10-29
            show("lastThursday", lastThursday);
            LocalDate fifthMonday = october.with(TemporalAdjusters.dayOfWeekInMonth(5, DayOfWeek.MONDAY));       // 2026-11-02
            show("fifthMonday", fifthMonday);
        }
        {
            LocalDateTime meeting = LocalDateTime.of(2026, 10, 14, 9, 30);
            LocalDateTime monthEndMeeting = meeting.with(TemporalAdjusters.lastDayOfMonth());   // 2026-10-31T09:30
            show("monthEndMeeting", monthEndMeeting);
            ZonedDateTime berlin = ZonedDateTime.of(2026, 10, 14, 9, 30, 0, 0, ZoneId.of("Europe/Berlin"));
            ZonedDateTime berlinMonthEnd = berlin.with(TemporalAdjusters.lastDayOfMonth());    // 2026-10-31T09:30+01:00[Europe/Berlin]
            show("berlinMonthEnd", berlinMonthEnd);
        }
        {
            try { LocalTime noon = LocalTime.NOON.with(TemporalAdjusters.lastDayOfMonth()); show("noon", noon); } catch (Throwable _t) { System.out.println("noon -> " + _t); }
            try { Instant instant = Instant.parse("2026-10-14T00:00:00Z").with(TemporalAdjusters.lastDayOfMonth()); show("instant", instant); } catch (Throwable _t) { System.out.println("instant -> " + _t); }
        }
        {
            LocalDate afterFriday = LocalDate.of(2026, 10, 16).with(nextWorkingDay());                  // 2026-10-19
            show("afterFriday", afterFriday);
            LocalDate afterTuesday = LocalDate.of(2026, 10, 13).with(nextWorkingDay());                 // 2026-10-14
            show("afterTuesday", afterTuesday);
            LocalDateTime withTime = LocalDateTime.of(2026, 10, 16, 17, 0).with(nextWorkingDay());      // 2026-10-19T17:00
            show("withTime", withTime);
        }
        {
            TemporalAdjuster naive = t -> t.with(ChronoField.MONTH_OF_YEAR, 11).with(ChronoField.DAY_OF_MONTH, 22);
            LocalDate wrong = LocalDate.of(2026, 12, 1).with(naive);   // 2026-11-22, which is already in the past
            show("wrong", wrong);
        }
        {
            TemporalAdjuster contractDay = new NextAnniversary(MonthDay.of(11, 22));
            LocalDate beforeIt = LocalDate.of(2026, 10, 14).with(contractDay);                    // 2026-11-22
            show("beforeIt", beforeIt);
            LocalDate afterIt = LocalDate.of(2026, 12, 1).with(contractDay);                      // 2027-11-22
            show("afterIt", afterIt);
            LocalDate leapBirthday = LocalDate.of(2026, 3, 1).with(new NextAnniversary(MonthDay.of(2, 29)));   // 2027-02-28
            show("leapBirthday", leapBirthday);
        }
        {
            LocalDate octoberPay = LocalDate.of(2026, 10, 1).with(lastWorkingDayOfMonth());    // 2026-10-30
            show("octoberPay", octoberPay);
            LocalDate novemberPay = LocalDate.of(2026, 11, 1).with(lastWorkingDayOfMonth());   // 2026-11-30
            show("novemberPay", novemberPay);
            LocalDate januaryPay = LocalDate.of(2027, 1, 15).with(lastWorkingDayOfMonth());    // 2027-01-29
            show("januaryPay", januaryPay);
            List<LocalDate> meetings = nextMondays(LocalDate.of(2026, 10, 14), 4);             // [2026-10-19, 2026-10-26, 2026-11-02, 2026-11-09]
            show("meetings", meetings);
        }
        {
            LocalDate quarterStart = LocalDate.of(2026, 10, 14).with(IsoFields.DAY_OF_QUARTER, 1);   // 2026-10-01
            show("quarterStart", quarterStart);
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
