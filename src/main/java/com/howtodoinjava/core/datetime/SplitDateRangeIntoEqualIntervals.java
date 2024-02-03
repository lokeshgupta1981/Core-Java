package com.howtodoinjava.core.datetime;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SplitDateRangeIntoEqualIntervals {

  public static void main(String[] args) {

    int equalParts = 52;

    LocalDateTime start = LocalDateTime.of(2021, 1, 1, 0, 0, 0);
    LocalDateTime end = LocalDateTime.of(2021, 12, 31, 0, 0, 0);

    System.out.println(splitInEqualIntervals(start, end, equalParts));

  }

  public static List<LocalDateTime> splitInEqualIntervals(
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
}
