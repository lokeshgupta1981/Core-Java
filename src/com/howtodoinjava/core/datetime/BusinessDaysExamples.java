package com.howtodoinjava.core.datetime;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BusinessDaysExamples {
    public static void main(final String[] args) {
        LocalDate today = LocalDate.of(2020, 5, 5);

        System.out.println(addBusinessDays(today, 8, Optional.empty()));

        // Add one holiday for testing
        List<LocalDate> holidays = new ArrayList<>();
        holidays.add(LocalDate.of(2020, 5, 11));
        holidays.add(LocalDate.of(2020, 5, 1));

        System.out.println(addBusinessDays(today, 8, Optional.of(holidays)));

        System.out.println(subtractBusinessDays(today, 8, Optional.empty()));
        System.out
            .println(subtractBusinessDays(today, 8, Optional.of(holidays)));

        System.out.println(countBusinessDaysBetween_Java8(today,
            today.plusDays(20), Optional.empty()).size());
        System.out.println(countBusinessDaysBetween_Java9(today,
            today.plusDays(20), Optional.of(holidays)).size());
    }

    private static LocalDate addBusinessDays(final LocalDate localDate,
        int days, final Optional<List<LocalDate>> holidays) {
        if (localDate == null || days <= 0) {
            throw new IllegalArgumentException(
                "Invalid method argument(s) to addBusinessDays(" + localDate
                    + "," + days + "," + holidays + ")");
        }

        Predicate<LocalDate> isHoliday = date -> holidays.isPresent()
            && holidays.get()
                .contains(date);

        Predicate<LocalDate> isWeekend = date -> date
            .getDayOfWeek() == DayOfWeek.SATURDAY
            || date.getDayOfWeek() == DayOfWeek.SUNDAY;

        LocalDate result = localDate;
        while (days > 0) {
            result = result.plusDays(1);
            if (isHoliday.or(isWeekend)
                .negate()
                .test(result)) {
                days--;
            }
        }
        return result;
    }

    private static LocalDate subtractBusinessDays(final LocalDate localDate,
        int days, final Optional<List<LocalDate>> holidays) {

        if (localDate == null || days <= 0) {
            throw new IllegalArgumentException(
                "Invalid method argument(s) to addBusinessDays(" + localDate
                    + "," + days + "," + holidays + ")");
        }

        Predicate<LocalDate> isHoliday = date -> holidays.isPresent()
            && holidays.get()
                .contains(date);

        Predicate<LocalDate> isWeekend = date -> date
            .getDayOfWeek() == DayOfWeek.SATURDAY
            || date.getDayOfWeek() == DayOfWeek.SUNDAY;

        LocalDate result = localDate;
        while (days > 0) {
            result = result.minusDays(1);
            if (isHoliday.or(isWeekend)
                .negate()
                .test(result)) {
                days--;
            }
        }
        return result;
    }

    private static List<LocalDate> countBusinessDaysBetween_Java8(
        final LocalDate startDate, final LocalDate endDate,
        final Optional<List<LocalDate>> holidays) {
        // Validate method arguments
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException(
                "Invalid method argument(s) to countBusinessDaysBetween ("
                    + startDate + "," + endDate + "," + holidays + ")");
        }

        // Predicate 1 : Is a given date is holiday
        Predicate<LocalDate> isHoliday = date -> holidays.isPresent()
            && holidays.get()
                .contains(date);

        // Predicate 2 : Is a given date is weekday
        Predicate<LocalDate> isWeekend = date -> date
            .getDayOfWeek() == DayOfWeek.SATURDAY
            || date.getDayOfWeek() == DayOfWeek.SUNDAY;

        // Get all days between two dates
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        // Iterate over stream of all dates and check each day against any weekday or
        // holiday
        return Stream.iterate(startDate, date -> date.plusDays(1))
            .limit(daysBetween)
            .filter(isHoliday.or(isWeekend)
                .negate())
            .collect(Collectors.toList());
    }

    private static List<LocalDate> countBusinessDaysBetween_Java9(
        final LocalDate startDate, final LocalDate endDate,
        final Optional<List<LocalDate>> holidays) {
        // Validate method arguments
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException(
                "Invalid method argument(s) to countBusinessDaysBetween ("
                    + startDate + "," + endDate + "," + holidays + ")");
        }

        // Predicate 1 : Is a given date is holiday
        Predicate<LocalDate> isHoliday = date -> holidays.isPresent()
            && holidays.get()
                .contains(date);

        // Predicate 2 : Is a given date is weekday
        Predicate<LocalDate> isWeekend = date -> date
            .getDayOfWeek() == DayOfWeek.SATURDAY
            || date.getDayOfWeek() == DayOfWeek.SUNDAY;

        // Iterate over stream of all dates and check each day against any weekday or
        // holiday
        List<LocalDate> businessDays = startDate.datesUntil(endDate)
            .filter(isWeekend.or(isHoliday)
                .negate())
            .collect(Collectors.toList());

        return businessDays;
    }
}
