package com.howtodoinjava.core.datetime;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class FirstLastDayOfYear {

  public static void main(String[] args) {
    int year = 2024;

    LocalDate firstDay = LocalDate.of(year, 1, 1);
    System.out.println(firstDay);

    LocalDate lastDay = LocalDate.of(year, 12, 31);
    System.out.println(lastDay);

    System.out.println(LocalDate.now().with(TemporalAdjusters.firstDayOfYear()));
    System.out.println(LocalDate.now().with(TemporalAdjusters.lastDayOfYear()));
  }
}
