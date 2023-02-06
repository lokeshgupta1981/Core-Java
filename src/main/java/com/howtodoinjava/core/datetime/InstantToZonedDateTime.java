package com.howtodoinjava.core.datetime;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class InstantToZonedDateTime {

  public static void main(String[] args) {
    Instant instant = Instant.now();
    System.out.println(instant);

    ZonedDateTime istDateTime = instant.atZone(ZoneId.of("Asia/Kolkata"));
    System.out.println(istDateTime);

    ZonedDateTime istDateTime1 = ZonedDateTime.ofInstant(instant, ZoneOffset.of("+05:30"));
    System.out.println(istDateTime1);

    Instant instant1 = ZonedDateTime.now().toInstant();
    System.out.println(instant1);
  }
}
