package com.howtodoinjava.core.datetime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

public class ChangingTimeZones {
  public static void main(String[] args) throws ParseException {

    //1
    ZonedDateTime instant = ZonedDateTime.now();
    System.out.println(instant);

    ZonedDateTime instantInUTC = instant.withZoneSameInstant(ZoneId.of("UTC"));
    System.out.println(instantInUTC);

    //2

    OffsetDateTime now = OffsetDateTime.now();
    System.out.println(now);

    OffsetDateTime nowInUTC = now.withOffsetSameInstant(ZoneOffset.of( "-00" +
        ":00" ));
    System.out.println(instantInUTC);


    //3
    SimpleDateFormat FORMATTER = new SimpleDateFormat("MM/dd/yyyy 'at' hh:mma z");

    //In Default Timezone
    Date currentDate = new Date();

    //Date in current timezone
    System.out.println(FORMATTER.format(currentDate));

    //In UTC Timezone
    TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
    FORMATTER.setTimeZone(utcTimeZone);
    String sDateInUTC = FORMATTER.format(currentDate);
    System.out.println(sDateInUTC);
  }
}
