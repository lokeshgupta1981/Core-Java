package com.howtodoinjava.core.datetime;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class EstTimezone {


  public static void main(String[] args)
  {

    //1
    DateTimeFormatter globalFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mma z");
    DateTimeFormatter etFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mma 'ET'");

    ZonedDateTime currentISTime = ZonedDateTime.now(); // "Asia/Kolkata"
    ZonedDateTime currentETime = currentISTime
          .withZoneSameInstant(ZoneId.of("America/New_York")); //ET Time

    System.out.println(globalFormat.format(currentETime));
    System.out.println(etFormat.format(currentETime));

    //2
    SimpleDateFormat etDf = new SimpleDateFormat("MM/dd/yyyy 'at' hh:mma 'ET'");
    TimeZone etTimeZone = TimeZone.getTimeZone("America/New_York");
    etDf.setTimeZone( etTimeZone );

    Date currentDate = new Date();
    Calendar currentTime = Calendar.getInstance();

    //In ET Time
    System.out.println(etDf.format(currentDate.getTime()));
    System.out.println(etDf.format(currentTime.getTimeInMillis()));

  }
}
