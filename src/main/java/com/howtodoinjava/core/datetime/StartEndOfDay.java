package com.howtodoinjava.core.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class StartEndOfDay {
  public static void main(final String[] args) {

    LocalDate localDate = LocalDate.now();
    
    // 1
    LocalDateTime startOfDay = localDate.atStartOfDay();
    LocalDateTime startOfDay1 = localDate.atTime(LocalTime.MIN);
    LocalDateTime startOfDay2 = LocalTime.MIN.atDate(localDate);

    LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);
    LocalDateTime endOfDay1 = LocalTime.MAX.atDate(localDate);

    // 2
    ZonedDateTime startOfDayInEurope = localDate
      .atStartOfDay(ZoneId.of("Europe/Paris"));
    ZonedDateTime endOfDayInEurope = localDate.atTime(LocalTime.MAX)
      .atZone(ZoneId.of("Europe/Paris"));
    
    ZonedDateTime startOfDayInZone = localDate
      .atStartOfDay(ZoneId.systemDefault());
    ZonedDateTime endOfDayInZone = localDate.atTime(LocalTime.MAX)
      .atZone(ZoneId.systemDefault());

    System.out.println(startOfDay);
    System.out.println(startOfDay1);
    System.out.println(startOfDay2);

    System.out.println(endOfDay);
    System.out.println(endOfDay1);

    System.out.println(startOfDayInEurope);
    System.out.println(endOfDayInEurope);
    
    System.out.println(startOfDayInZone);
    System.out.println(endOfDayInZone);
  }
}
