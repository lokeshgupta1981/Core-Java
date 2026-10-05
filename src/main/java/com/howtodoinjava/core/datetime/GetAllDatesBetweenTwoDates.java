package com.howtodoinjava.core.datetime;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Lists all dates between two dates: LocalDate.datesUntil() (Java 9+), including the end date,
 * steps of a week or a month, reverse order, the Java 8 way with Stream.iterate() and
 * LongStream, counting days, business days, LocalDateTime ranges and legacy Date/Calendar.
 *
 * <p>Source code for the article "Get All Dates Between Two Dates in Java" on howtodoinjava.com.
 */
public class GetAllDatesBetweenTwoDates {

  public static void main(final String[] args) {
    quickAnswer();
    edgeCases();
    stepByWeekOrMonth();
    java8Way();
    countingDays();
    businessDays();
    localDateTimeRanges();
    legacyDate();
    faqs();
  }

  static void quickAnswer() {
    System.out.println("--- quick answer ---");
    LocalDate start = LocalDate.of(2026, 10, 1);
    LocalDate end = LocalDate.of(2026, 10, 5);

    List<LocalDate> dates = start.datesUntil(end).toList();
    System.out.println(dates);
    List<LocalDate> withEnd = start.datesUntil(end.plusDays(1)).toList();
    System.out.println(withEnd);
  }

  static void edgeCases() {
    System.out.println("--- edge cases ---");
    LocalDate start = LocalDate.of(2026, 10, 1);
    System.out.println(start.datesUntil(start).toList());
    System.out.println(start.datesUntil(start.plusDays(1)).toList());
    try {
      start.datesUntil(LocalDate.of(2026, 9, 28)).toList();
    } catch (IllegalArgumentException e) {
      System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
    }
    // Leap year
    System.out.println(LocalDate.of(2028, 2, 27).datesUntil(LocalDate.of(2028, 3, 2)).toList());
    System.out.println(LocalDate.of(2026, 1, 1).datesUntil(LocalDate.of(2027, 1, 1)).count());
  }

  static void stepByWeekOrMonth() {
    System.out.println("--- steps ---");
    LocalDate start = LocalDate.of(2026, 10, 1);
    LocalDate end = LocalDate.of(2026, 11, 1);
    System.out.println(start.datesUntil(end, Period.ofWeeks(1)).toList());
    System.out.println(start.datesUntil(end, Period.ofDays(10)).toList());

    LocalDate jan31 = LocalDate.of(2026, 1, 31);
    LocalDate june1 = LocalDate.of(2026, 6, 1);
    System.out.println(jan31.datesUntil(june1, Period.ofMonths(1)).toList());
    System.out.println(Stream.iterate(jan31, d -> d.plusMonths(1)).limit(5).toList());

    // Reverse order
    System.out.println(LocalDate.of(2026, 10, 5)
        .datesUntil(LocalDate.of(2026, 9, 30), Period.ofDays(-1)).toList());
    try {
      start.datesUntil(end, Period.ZERO).toList();
    } catch (IllegalArgumentException e) {
      System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
    }
    try {
      start.datesUntil(end, Period.ofDays(-1)).toList();
    } catch (IllegalArgumentException e) {
      System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
    }
  }

  static void java8Way() {
    System.out.println("--- java 8 ---");
    LocalDate start = LocalDate.of(2026, 10, 1);
    LocalDate end = LocalDate.of(2026, 10, 5);

    long numOfDays = ChronoUnit.DAYS.between(start, end);
    System.out.println(numOfDays);

    List<LocalDate> a = Stream.iterate(start, date -> date.plusDays(1))
        .limit(numOfDays)
        .collect(Collectors.toList());
    System.out.println(a);

    List<LocalDate> b = LongStream.range(0, numOfDays)
        .mapToObj(start::plusDays)
        .collect(Collectors.toList());
    System.out.println(b);

    List<LocalDate> c = new ArrayList<>();
    for (LocalDate d = start; d.isBefore(end); d = d.plusDays(1)) {
      c.add(d);
    }
    System.out.println(c);

    // Java 9 three-argument iterate
    List<LocalDate> d = Stream.iterate(start, x -> x.isBefore(end), x -> x.plusDays(1)).toList();
    System.out.println(d);
  }

  static void countingDays() {
    System.out.println("--- counting ---");
    LocalDate start = LocalDate.of(2026, 10, 1);
    LocalDate end = LocalDate.of(2026, 12, 1);
    System.out.println(ChronoUnit.DAYS.between(start, end));
    System.out.println(start.datesUntil(end).count());
    System.out.println(ChronoUnit.DAYS.between(start, end) + 1);
    System.out.println(ChronoUnit.DAYS.between(end, start));
    System.out.println(start.until(end));
  }

  static void businessDays() {
    System.out.println("--- business days ---");
    LocalDate start = LocalDate.of(2026, 10, 1);
    LocalDate end = LocalDate.of(2026, 10, 15);
    Set<DayOfWeek> weekend = Set.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

    List<LocalDate> workDays = start.datesUntil(end)
        .filter(d -> !weekend.contains(d.getDayOfWeek()))
        .toList();
    System.out.println(workDays);
    System.out.println(workDays.size());

    Set<LocalDate> holidays = Set.of(LocalDate.of(2026, 10, 2));
    long count = start.datesUntil(end)
        .filter(d -> !weekend.contains(d.getDayOfWeek()))
        .filter(d -> !holidays.contains(d))
        .count();
    System.out.println(count);

    System.out.println(start.datesUntil(end)
        .filter(d -> d.getDayOfWeek() == DayOfWeek.MONDAY).toList());

    // Business days from Oct 1 to Oct 5, both included (the diagram in the article)
    System.out.println(start.datesUntil(LocalDate.of(2026, 10, 5).plusDays(1))
        .filter(d -> !weekend.contains(d.getDayOfWeek()))
        .toList());
    System.out.println(start.getDayOfWeek());
  }

  static void localDateTimeRanges() {
    System.out.println("--- LocalDateTime ---");
    LocalDateTime from = LocalDateTime.of(2026, 10, 1, 22, 30);
    LocalDateTime to = LocalDateTime.of(2026, 10, 4, 8, 0);

    List<LocalDate> days = from.toLocalDate()
        .datesUntil(to.toLocalDate().plusDays(1))
        .toList();
    System.out.println(days);

    List<LocalDateTime> slots = Stream.iterate(from, t -> t.isBefore(to), t -> t.plusHours(12))
        .toList();
    System.out.println(slots);
  }

  static void legacyDate() {
    System.out.println("--- legacy ---");
    Calendar cal = new GregorianCalendar(2026, Calendar.OCTOBER, 1);
    Date startDate = cal.getTime();
    cal.set(2026, Calendar.OCTOBER, 5);
    Date endDate = cal.getTime();

    ZoneId zone = ZoneId.systemDefault();
    LocalDate start = startDate.toInstant().atZone(zone).toLocalDate();
    LocalDate end = endDate.toInstant().atZone(zone).toLocalDate();
    List<LocalDate> dates = start.datesUntil(end).toList();
    System.out.println(dates);

    List<Date> back = dates.stream()
        .map(d -> Date.from(d.atStartOfDay(zone).toInstant()))
        .toList();
    System.out.println(back.size());

    List<Date> old = getDaysBetweenDates(startDate, endDate);
    System.out.println(old.size());

    // The Calendar loop keeps the time of day: a later end time adds one more date
    Date start10am = new GregorianCalendar(2026, Calendar.OCTOBER, 1, 10, 0).getTime();
    Date end12pm = new GregorianCalendar(2026, Calendar.OCTOBER, 5, 12, 0).getTime();
    System.out.println(getDaysBetweenDates(start10am, end12pm).size());
  }

  public static List<Date> getDaysBetweenDates(final Date startdate, final Date enddate) {
    List<Date> dates = new ArrayList<>();
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(startdate);
    while (calendar.getTime().before(enddate)) {
      dates.add(calendar.getTime());
      calendar.add(Calendar.DATE, 1);
    }
    return dates;
  }

  static void faqs() {
    System.out.println("--- faqs ---");
    YearMonth month = YearMonth.of(2026, 2);
    LocalDate start1 = LocalDate.of(2026, 10, 1);
    System.out.println(start1.datesUntil(LocalDate.of(2026, 10, 29).plusDays(1), Period.ofWeeks(1))
        .toList());
    List<LocalDate> feb = month.atDay(1).datesUntil(month.plusMonths(1).atDay(1)).toList();
    System.out.println(feb.size() + " " + feb.get(0) + " " + feb.get(feb.size() - 1));

    List<YearMonth> months = Stream.iterate(YearMonth.of(2026, 10),
        m -> !m.isAfter(YearMonth.of(2027, 1)), m -> m.plusMonths(1)).toList();
    System.out.println(months);

    LocalDate start = LocalDate.of(2026, 10, 1);
    System.out.println(start.datesUntil(LocalDate.of(2026, 10, 5))
        .map(d -> d.getDayOfWeek().toString()).toList());
  }
}
