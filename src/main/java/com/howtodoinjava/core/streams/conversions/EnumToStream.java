package com.howtodoinjava.core.streams.conversions;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class EnumToStream {
  public static void main(final String[] args) {

    Enumeration<AccountStatus> statuses = Collections
      .enumeration(Arrays.asList(
        AccountStatus.ACTIVE, 
        AccountStatus.INACTIVE));

    Stream<AccountStatus> stream = StreamSupport.stream(Spliterators
      .spliteratorUnknownSize(statuses.asIterator(), Spliterator.ORDERED),
      false);
    
    stream.forEach(System.out::println);
  }
}

enum AccountStatus {

  PURGED("P", "purged", -1), INACTIVE("I", "inactive", 0), ACTIVE("A", "active",
    1);

  private final String shortName;
  private final String fullName;
  private final int code;

  AccountStatus(final String shortName, final String fullName, final int code) {
    this.shortName = shortName;
    this.fullName = fullName;
    this.code = code;
  }

  public String getShortName() {
    return shortName;
  }

  public String getFullName() {
    return fullName;
  }

  public int getCode() {
    return code;
  }
}