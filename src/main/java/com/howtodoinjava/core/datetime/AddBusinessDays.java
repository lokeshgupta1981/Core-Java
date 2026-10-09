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
 * Examples for the tutorial "Add Business Days in Java: Skip Weekends and Holidays".
 * https://howtodoinjava.com/java/date-time/add-subtract-business-days/
 */
public class AddBusinessDays {
    static boolean isBusinessDay(LocalDate date, Set<LocalDate> holidays) {
        DayOfWeek day = date.getDayOfWeek();
        boolean weekend = day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
        return !weekend && !holidays.contains(date);
    }
    static LocalDate addBusinessDays(LocalDate date, int days, Set<LocalDate> holidays) {
        Objects.requireNonNull(date, "date");
        Objects.requireNonNull(holidays, "holidays");
        int step = days < 0 ? -1 : 1;
        int remaining = Math.abs(days);
        LocalDate result = date;
        while (remaining > 0) {
            result = result.plusDays(step);
            if (isBusinessDay(result, holidays)) {
                remaining--;
            }
        }
        return result;
    }
    static boolean isWorkingDay(LocalDate date, Set<DayOfWeek> weekend, Set<LocalDate> holidays, Set<MonthDay> yearlyHolidays) {
        return !weekend.contains(date.getDayOfWeek())
                && !holidays.contains(date)
                && !yearlyHolidays.contains(MonthDay.from(date));
    }
    static LocalDate addWeekdays(LocalDate date, int days) {
        if (days <= 0) {
            return addBusinessDays(date, days, Set.of());
        }
        int weeks = (days - 1) / 5;
        LocalDate result = date.plusWeeks(weeks);
        return addBusinessDays(result, days - weeks * 5, Set.of());
    }
    public static void main(String[] args) throws Exception {
        {
            LocalDate orderDate = LocalDate.of(2026, 10, 9);                                  // a Friday
            show("orderDate", orderDate);
            LocalDate shipBy = addBusinessDays(orderDate, 3, Set.of());                       // 2026-10-14
            show("shipBy", shipBy);
            LocalDate shipByHoliday = addBusinessDays(orderDate, 3, Set.of(LocalDate.of(2026, 10, 12)));   // 2026-10-15
            show("shipByHoliday", shipByHoliday);
            LocalDate threeBack = addBusinessDays(orderDate, -3, Set.of());                   // 2026-10-06
            show("threeBack", threeBack);
        }
        {
            Set<LocalDate> holidays = Set.of(LocalDate.of(2026, 10, 12));
            boolean friday = isBusinessDay(LocalDate.of(2026, 10, 9), holidays);     // true
            show("friday", friday);
            boolean saturday = isBusinessDay(LocalDate.of(2026, 10, 10), holidays);  // false
            show("saturday", saturday);
            boolean holiday = isBusinessDay(LocalDate.of(2026, 10, 12), holidays);   // false
            show("holiday", holiday);
        }
        {
            Set<LocalDate> noHolidays = Set.of();
            LocalDate fromSaturday = addBusinessDays(LocalDate.of(2026, 10, 10), 1, noHolidays);   // 2026-10-12
            show("fromSaturday", fromSaturday);
            LocalDate zeroDays = addBusinessDays(LocalDate.of(2026, 10, 10), 0, noHolidays);      // 2026-10-10
            show("zeroDays", zeroDays);
            LocalDate tenBack = addBusinessDays(LocalDate.of(2026, 10, 9), -10, noHolidays);      // 2026-09-25
            show("tenBack", tenBack);
        }
        {
            Set<DayOfWeek> friSat = EnumSet.of(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY);
            Set<DayOfWeek> satSun = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
            Set<MonthDay> fixed = Set.of(MonthDay.of(12, 25), MonthDay.of(1, 1));
            boolean fridayInRiyadh = isWorkingDay(LocalDate.of(2026, 10, 9), friSat, Set.of(), Set.of());   // false
            show("fridayInRiyadh", fridayInRiyadh);
            boolean sundayInRiyadh = isWorkingDay(LocalDate.of(2026, 10, 11), friSat, Set.of(), Set.of()); // true
            show("sundayInRiyadh", sundayInRiyadh);
            boolean christmas = isWorkingDay(LocalDate.of(2026, 12, 25), satSun, Set.of(), fixed);          // false
            show("christmas", christmas);
        }
        {
            LocalDate start = LocalDate.of(2026, 10, 10);
            LocalDate yearOfWork = addWeekdays(start, 250);                        // 2027-09-24
            show("yearOfWork", yearOfWork);
            boolean sameAsLoop = IntStream.rangeClosed(1, 1000).allMatch(n -> addWeekdays(start, n).equals(addBusinessDays(start, n, Set.of())));   // true
            show("sameAsLoop", sameAsLoop);
        }
        {
            LocalDateTime ticketOpened = LocalDateTime.of(2026, 10, 9, 16, 0);
            LocalDateTime slaDue = ticketOpened.with(addBusinessDays(ticketOpened.toLocalDate(), 2, Set.of()));   // 2026-10-13T16:00
            show("slaDue", slaDue);
            ZonedDateTime paymentAt = ZonedDateTime.of(2026, 10, 9, 10, 0, 0, 0, ZoneId.of("Europe/Berlin"));
            ZonedDateTime settles = paymentAt.with(addBusinessDays(paymentAt.toLocalDate(), 1, Set.of()));       // 2026-10-12T10:00+02:00[Europe/Berlin]
            show("settles", settles);
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
