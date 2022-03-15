package com.howtodoinjava.core.streams;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamOfDates {
  public static void main(String[] args) {

    //Java 9
    LocalDate today = LocalDate.now();
    Stream<LocalDate> next3Days = today.datesUntil(today.plusDays(3));
    next3Days.forEach(System.out::println);

    Stream<LocalDate> sameDayNext3Weeks = today.datesUntil(today.plusDays(21),
        Period.ofWeeks(1));
    sameDayNext3Weeks.forEach(System.out::println);

    //Java 8
    Stream<LocalDate> nextThreeDays = Stream.iterate(today, d -> d.plusDays(1));
    List<LocalDate> dates = nextThreeDays
        .limit(3)
        .collect(Collectors.toList());
  }
}
