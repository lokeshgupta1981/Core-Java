package com.howtodoinjava.core.datetime;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeFormatterWithTimeZone 
{
	private static final String DT_PATTERN = "dd/MM/yyyy HH:mm z";
	
	public static void main(String[] args) 
	{
		DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern(DT_PATTERN);
		
		ZonedDateTime zdt = ZonedDateTime.now();
		
		//Formatting
		
		String localizedDateTime = dtFormatter.format(zdt);
		System.out.println(localizedDateTime);
		
		String localizedDateTimeInUTC = dtFormatter.format(zdt.withZoneSameInstant(ZoneId.of("UTC-4")));
		System.out.println(localizedDateTimeInUTC);
		
		//Parsing
		
		ZonedDateTime localizedDateTimeParsed = ZonedDateTime.parse(localizedDateTime, dtFormatter);
		System.out.println(localizedDateTimeParsed);
		System.out.println(localizedDateTimeParsed.getOffset().getTotalSeconds());
	}
}
