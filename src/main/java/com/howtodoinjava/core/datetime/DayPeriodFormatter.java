package com.howtodoinjava.core.datetime;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DayPeriodFormatter {

  static LocalTime night = LocalTime.of(21, 0, 0);
  static LocalTime morning = LocalTime.of(6, 0, 0);
  static LocalTime afternoon = LocalTime.of(12, 0, 0);
  static LocalTime evening = LocalTime.of(18, 0, 0);
  static LocalTime almostMidnight = LocalTime.of(23, 59, 59);
  static LocalTime midnight = LocalTime.of(0, 0, 0);

  public static void main(String[] args) {

    LocalDateTime localDateTime = LocalDateTime.now();

    System.out.println(DayPeriodFormatter.formatBeforeJava16(localDateTime, ZoneId.systemDefault()));
    System.out.println(DayPeriodFormatter.formatSinceJava16(localDateTime, ZoneId.systemDefault()));
  }

  public static String formatBeforeJava16(LocalDateTime localDateTime, ZoneId zoneId) {

    ZonedDateTime zdt = localDateTime.atZone(zoneId);
    LocalTime lt = zdt.toLocalTime();

    DateTimeFormatter formatter
        = DateTimeFormatter.ofPattern("yyyy-MMM-dd");

    String dayTime = "";

    if ((lt.isAfter(night) && lt.isBefore(almostMidnight))
        || lt.isAfter(midnight) && (lt.isBefore(morning))) {
      dayTime = " at night";
    } else if (lt.isAfter(morning) && lt.isBefore(afternoon)) {
      dayTime = " in the morning";
    } else if (lt.isAfter(afternoon) && lt.isBefore(evening)) {
      dayTime = " in the afternoon";
    } else if (lt.isAfter(evening) && lt.isBefore(night)) {
      dayTime = " in the evening";
    }

    return zdt.withZoneSameInstant(zoneId).format(formatter) + dayTime;
  }

  public static String formatSinceJava16(LocalDateTime localDateTime, ZoneId zoneId) {

    ZonedDateTime zdt = localDateTime.atZone(zoneId);
    DateTimeFormatter formatter
        = DateTimeFormatter.ofPattern("yyyy-MMM-dd [B]");
    return zdt.withZoneSameInstant(zoneId).format(formatter);
  }
}
