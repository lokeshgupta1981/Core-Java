package com.howtodoinjava.core.datetime;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;

public class NewDateTimeAPI {

  public static void main(String[] args) {
    OffsetDateTime offsetDate = OffsetDateTime.now();
    System.out.println(offsetDate);

    ZonedDateTime zonedDateTime = ZonedDateTime.now();
    System.out.println(zonedDateTime);

    Optional optional = Stream.empty().findAny();
    Assertions.assertTrue(optional.isEmpty());

    optional = Stream.of("one", "two", "three", "four")
        .findAny();

    Assertions.assertTrue(optional.isPresent());
    Assertions.assertEquals("one", optional.get());


    optional = Stream.of("one", "two", "three", "four")
        .parallel()
        .findAny();

    Assertions.assertTrue(optional.isPresent());
  }
}
