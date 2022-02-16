package com.howtodoinjava.core.datetime;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CurrentTimeStamp {
  public static void main(final String[] args) {
    Instant instant = Instant.now();
    System.out.println(instant);

    DateTimeFormatter formatter = DateTimeFormatter
      .ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
      .withZone(ZoneId.of("GMT"));

    String formattedString = formatter.format(instant); // 15-02-2022 12:43
    System.out.println(formattedString);

    // Java 7

    Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());

    Date date = new Date();
    Timestamp timestamp2 = new Timestamp(date.getTime());
    
    System.out.println(timestamp1);     //2022-02-15 13:55:56.18
    System.out.println(timestamp2);     //2022-02-15 13:55:56.18
    
    formattedString = formatter.format(timestamp1.toInstant());
    System.out.println(formattedString);
  }
}
