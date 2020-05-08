package com.howtodoinjava.core.datetime;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateFormatting 
{
	public static void main(String[] args) 
	{
		String dateTimeString = formatLocalDateTime(LocalDateTime.now()); 
		System.out.println(dateTimeString);  //2020-05-08 23:17:22 PM
		
		String dateString = formatLocalDate(LocalDate.now()); 
		System.out.println(dateString);  //2020-05-08
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP_PATTERN);
		String formattedDate = sdf.format(date);
		System.out.println(formattedDate);
	}

	//Format LocalDateTime to String
	
	public static final String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss a"; 
	public static final DateTimeFormatter LDT_FOMATTER = DateTimeFormatter.ofPattern(TIMESTAMP_PATTERN);

	private static String formatLocalDateTime(LocalDateTime ldt)
	{
		return LDT_FOMATTER.format(ldt);
	}
	
	//Format LocalDate to String

	public static final String DATE_PATTERN = "yyyy-MM-dd"; 
	public static final DateTimeFormatter LD_FOMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
	
	private static String formatLocalDate(LocalDate ld)
	{
		return LD_FOMATTER.format(ld);
	}
}
