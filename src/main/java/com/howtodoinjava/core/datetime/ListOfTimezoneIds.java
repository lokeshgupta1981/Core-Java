package com.howtodoinjava.core.datetime;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ListOfTimezoneIds {

  public static void main(String[] args) {

    Instant instant = Instant.now();

    Map<String, String> result = ZoneId.getAvailableZoneIds()
        .stream()
        .map(ZoneId::of)
        .map(zoneId -> new SimpleEntry<>(zoneId.toString(), instant.atZone(zoneId)
            .getOffset()
            .getId()
            .replaceAll("Z", "+00:00")))
        .sorted(Map.Entry.<String, String>comparingByValue().reversed())
        .collect(Collectors.toMap(
            SimpleEntry::getKey,
            SimpleEntry::getValue,
            (oldValue, newValue) -> oldValue,
            LinkedHashMap::new));

    result.forEach((k, v) -> System.out.printf(String.format("%35s %s %n", k, v)));

    System.out.println("\nNumber of Zone IDs " + result.size());

    System.out.println("========================");

    ZonedDateTime zdt = ZonedDateTime.now(ZoneOffset.of("+05:30"));
    System.out.println(zdt);

    ZonedDateTime zdt1 = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
    System.out.println(zdt1);

    ZonedDateTime zdt2 = ZonedDateTime.now(ZoneId.of(ZoneId.SHORT_IDS.get("IST")));
    System.out.println(zdt2);

    LocalDateTime ldt = LocalDateTime.now();
    ZonedDateTime zdt3 = ldt.atZone(ZoneId.of("Asia/Kolkata"));
    System.out.println(zdt3);

    LocalDateTime ldt1 = LocalDateTime.now();
    ZonedDateTime zdt4 = ldt.atZone(ZoneOffset.of("+05:30"));
    System.out.println(zdt4);
  }

}
