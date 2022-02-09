package com.howtodoinjava.core.datetime;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class GetAllDatesBetweenTwoDates {
  public static void main(final String[] args) {
    LocalDate startDate = LocalDate.now();
    LocalDate endDate = startDate.plusMonths(2);

    // Java 9

    List<LocalDate> listOfDates = startDate.datesUntil(endDate).toList();

    System.out.println(listOfDates.size());

    // Java 8 - Method 1

    long numOfDays = ChronoUnit.DAYS.between(startDate, endDate);

    List<LocalDate> listOfDates1 = Stream
        .iterate(startDate, date -> date.plusDays(1))
        .limit(numOfDays).toList();

    System.out.println(listOfDates1.size());

    // Java 8 - Method 2

    List<LocalDate> listOfDates2 = LongStream.range(0, numOfDays)
        .mapToObj(startDate::plusDays).toList();

    System.out.println(listOfDates2.size());

    // Java 7
    Date todaysDate = new Date();
    Date anotherDate = new Date(
        todaysDate.getTime() + 61 * 24 * 60 * 60 * 1000L);

    List<Date> listOfDates3 = getDaysBetweenDates(todaysDate, anotherDate);

    System.out.println(listOfDates3.size());
  }

  public static List<Date> getDaysBetweenDates(final Date startdate,
                                               final Date enddate) {
    List<Date> dates = new ArrayList<>();
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(startdate);

    while (calendar.getTime().before(enddate)) {
      Date result = calendar.getTime();
      dates.add(result);
      calendar.add(Calendar.DATE, 1);
    }
    return dates;
  }
}
