package com.howtodoinjava.core.datetime;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class FirstAndLastDay {

  public static void main(String[] args) {
    LocalDate today = LocalDate.of(2024, 4, 26);

    //Week
    LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

    System.out.println(firstDayOfWeek);  //2024-04-22
    System.out.println(lastDayOfWeek);  //2024-04-22

    //Month
    LocalDate firstDayOfMonth = today.withDayOfMonth(1);
    LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

    System.out.println(firstDayOfMonth);  //2024-04-01
    System.out.println(lastDayOfMonth);   //2024-04-30

    firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
    lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());

    System.out.println(firstDayOfMonth);  //2024-04-01
    System.out.println(lastDayOfMonth);   //2024-04-30

    //Year
    LocalDate firstDayOfYear = today.with(TemporalAdjusters.firstDayOfYear());
    LocalDate lastDayOfYear = today.with(TemporalAdjusters.lastDayOfYear());

    System.out.println(firstDayOfYear);   //2024-01-01
    System.out.println(lastDayOfYear);    //2024-12-31
  }
}
