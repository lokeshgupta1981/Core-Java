package com.howtodoinjava.core.datetime;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class WeeksBetweenDates {

  public static void main(String[] args) {
    LocalDate startLd = LocalDate.of(2023, 8, 5);
    LocalDate endLd = LocalDate.of(2023, 9, 5);
    long numWeeks = ChronoUnit.WEEKS.between(startLd, endLd);
    System.out.println(numWeeks);

    long daysDifference = ChronoUnit.DAYS.between(startLd, endLd);  // 31
    int extraDays = Long.valueOf(daysDifference % 7).intValue(); //3
    System.out.println(extraDays);
  }

}
