package com.howtodoinjava.core.datetime;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;

public class GetQuarterInfo {

  public static void main(String[] args) {

    LocalDate localDate = LocalDate.now();

    //What is current quarter
    int currentQuarter = localDate.get(IsoFields.QUARTER_OF_YEAR);
    System.out.println(currentQuarter);

    String currentQuarterStr = localDate.format(DateTimeFormatter.ofPattern("QQQQ"));
    System.out.println(currentQuarterStr);

    //Current quarter start date and end date
    LocalDate firstDay = localDate.with(IsoFields.DAY_OF_QUARTER, 1L);
    LocalDate lastDay = firstDay.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth());
    System.out.println(firstDay);
    System.out.println(lastDay);
  }
}
