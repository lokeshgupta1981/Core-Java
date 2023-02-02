package com.howtodoinjava.core.datetime;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class InstantToLocalDateTime {

  public static void main(String[] args) {
    Instant instant = Instant.now();
    System.out.println(instant);

    //1
    LocalDateTime ldt1 = LocalDateTime.ofInstant(instant, ZoneOffset.systemDefault());
    LocalDate ld1 = LocalDate.ofInstant(instant, ZoneOffset.systemDefault());
    LocalTime lt1 = LocalTime.ofInstant(instant, ZoneOffset.systemDefault());

    System.out.println(ldt1);
    System.out.println(ld1);
    System.out.println(lt1);

    LocalDateTime ldt2 = LocalDateTime.ofInstant(instant, ZoneOffset.of("+05:30"));
    LocalDate ld2 = LocalDate.ofInstant(instant, ZoneOffset.of("+05:30"));
    LocalTime lt2 = LocalTime.ofInstant(instant, ZoneOffset.of("+05:30"));

    System.out.println(ldt2);
    System.out.println(ld2);
    System.out.println(lt2);

    //2
    LocalDateTime ldt3 = instant.atZone(ZoneOffset.systemDefault()).toLocalDateTime();
    LocalDate ld3 = instant.atZone(ZoneOffset.systemDefault()).toLocalDate();
    LocalTime lt3 = instant.atZone(ZoneOffset.systemDefault()).toLocalTime();

    System.out.println(ldt3);
    System.out.println(ld3);
    System.out.println(lt3);

    //3
    LocalDateTime ldt = LocalDateTime.now();

    Instant instant1 = ldt.atZone(ZoneId.systemDefault())
        .withZoneSameInstant(ZoneId.of("UTC"))
        .toInstant();
    System.out.println(instant1);
  }
}
