package com.howtodoinjava.core.datetime;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class ParseZonedDateTime {
  final static DateTimeFormatter formatter
      = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a z");

  final static DateTimeFormatter formatterWithoutZone
      = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a")
      .withZone(ZoneId.systemDefault());

  public static void main(String[] args) {
    //1
    ZonedDateTime zdtWithZoneOffset = ZonedDateTime
          .parse("2019-03-27 10:15:30 am -05:00", formatter);

    ZonedDateTime zdtInLocalTimeline = zdtWithZoneOffset
        .withZoneSameInstant(ZoneId.systemDefault());

    System.out.println(zdtWithZoneOffset);
    System.out.println(zdtInLocalTimeline);

    //2
    LocalDateTime ldt = LocalDateTime
        .parse("2019-03-27 10:15:30 am", formatterWithoutZone);

    ZonedDateTime zdt = ZonedDateTime.of(ldt, ZoneId.systemDefault());

    System.out.println(zdt);

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
  }
}

