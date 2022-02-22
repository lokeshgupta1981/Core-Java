package com.howtodoinjava.core.datetime;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class XmlGregorianCalendarExamples {
  private final static String TIMESTAMP_PATTERN
      = "MM/dd/yyyy hh:mm a z";
  private final static DateTimeFormatter FORMATTER
      = DateTimeFormatter.ofPattern(TIMESTAMP_PATTERN);

  public static void main(String[] args) throws DatatypeConfigurationException {
    //Create XMLGregorianCalendar
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime(new Date());
    XMLGregorianCalendar xCal = DatatypeFactory.newInstance()
        .newXMLGregorianCalendar(cal);

    XMLGregorianCalendar xmlGregorianCalendar =
        DatatypeFactory.newInstance()
            .newXMLGregorianCalendar(LocalDate.now().toString());

    System.out.println(xCal);

    //XMLGregorianCalendar -> Date
    Date date = xCal.toGregorianCalendar().getTime();
    System.out.println(date);

    //Format
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a z");
    String formattedString = df.format(date);
    System.out.println(formattedString);

    //Timezone adjust
    TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");
    df.setTimeZone(gmtTimeZone);
    System.out.println(df.format(date));

    //ZonedDateTime
    ZonedDateTime zdt = xCal.toGregorianCalendar().toZonedDateTime();
    System.out.println( FORMATTER.format(zdt) );

    //SimpleDateFormat
    DateFormat dateFormat = new SimpleDateFormat(TIMESTAMP_PATTERN);
    formattedString =
        dateFormat.format(xCal.toGregorianCalendar().getTime());
    System.out.println(formattedString);
  }
}
