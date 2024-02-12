package com.howtodoinjava.core.datetime;

import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_YEAR;
import static java.time.temporal.ChronoField.YEAR;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SplitDateRangeIntoEqualIntervals {

  public static void main(String[] args) {

    int equalParts = 15;
    LocalDateTime start = LocalDateTime.of(2021, 10, 1, 0, 0, 0);
    LocalDateTime end = LocalDateTime.of(2022, 03, 31, 0, 0, 0);

    System.out.println(split_LocalDateTime_Range_In_N_Equal_Intervals(start, end, equalParts));
    System.out.println(split_LocalDate_Range_Into_Months(start.toLocalDate(), end.toLocalDate()));
    System.out.println(split_LocalDate_Range_Into_Weeks(start.toLocalDate(), end.toLocalDate()));
  }

  public static List<LocalDateTime> split_LocalDateTime_Range_In_N_Equal_Intervals(
      LocalDateTime start, LocalDateTime end, int n) {

    Duration range = Duration.between(start, end);
    Duration interval = range.dividedBy(n - 1);
    List<LocalDateTime> listOfDates = new ArrayList<>();
    LocalDateTime timeline = start;
    for (int i = 0; i < n - 1; i++) {
      listOfDates.add(timeline);
      timeline = timeline.plus(interval);
    }
    listOfDates.add(end);
    return listOfDates;
  }

  public static List<LocalDate> split_LocalDate_Range_Into_Days(
      LocalDate startDate, LocalDate endDate) {

    long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);
    return IntStream.iterate(0, i -> i + 1)
        .limit(numOfDaysBetween)
        .mapToObj(i -> startDate.plusDays(i))
        .collect(Collectors.toList());
  }

  public static List<YearWeek> split_LocalDate_Range_Into_Weeks(
      LocalDate startDate, LocalDate endDate) {

    long numOfDaysBetween = ChronoUnit.WEEKS.between(startDate, endDate);
    return IntStream.iterate(0, i -> i + 1)
        .limit(numOfDaysBetween)
        .mapToObj(i -> YearWeek.from(startDate.plusWeeks(i)))
        .collect(Collectors.toList());
  }

  public static List<YearMonth> split_LocalDate_Range_Into_Months(
      LocalDate startDate, LocalDate endDate) {

    long numOfDaysBetween = ChronoUnit.MONTHS.between(startDate, endDate);
    return IntStream.iterate(0, i -> i + 1)
        .limit(numOfDaysBetween)
        .mapToObj(i -> YearMonth.from(startDate.plusMonths(i)))
        .collect(Collectors.toList());
  }

  public static List<Year> split_LocalDate_Range_Into_Years(
      LocalDate startDate, LocalDate endDate) {

    long numOfDaysBetween = ChronoUnit.YEARS.between(startDate, endDate);
    return IntStream.iterate(0, i -> i + 1)
        .limit(numOfDaysBetween)
        .mapToObj(i -> Year.from(startDate.plusYears(i)))
        .collect(Collectors.toList());
  }
}

record YearWeek(int year, int week) {

  public static YearWeek from(TemporalAccessor temporal) {
    Objects.requireNonNull(temporal, "temporal");
    try {
      if (IsoChronology.INSTANCE.equals(Chronology.from(temporal)) == false) {
        temporal = LocalDate.from(temporal);
      }
      return new YearWeek(temporal.get(YEAR), temporal.get(ALIGNED_WEEK_OF_YEAR));
    } catch (DateTimeException ex) {
      throw new DateTimeException(
          STR."Unable to obtain YearWeek from TemporalAccessor: \{temporal} of type \{temporal.getClass().getName()}", ex);
    }
  }

  @Override
  public String toString() {
    return STR."\{year}-\{week}";
  }
}
