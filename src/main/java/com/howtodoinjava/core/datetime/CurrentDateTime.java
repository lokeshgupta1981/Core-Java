package com.howtodoinjava.core.datetime;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class CurrentDateTime 
{
	public static void main(String[] args) 
	{
		//Java 8 or later
		
		LocalDate today = LocalDate.now();
		
		System.out.println(today);
		
		LocalTime now = LocalTime.now();
		
		System.out.println(now);
		
		LocalDateTime instance = LocalDateTime.now();
		
		System.out.println(instance);
		
		DateTimeFormatter formatter 
			= DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm");
		
		System.out.println(formatter.format(instance));
		
		//Java 7 or earlier
		
		Date date = new Date();
		
		System.out.println(date);
		
		Calendar cal = Calendar.getInstance();
		
		System.out.println(cal.toString());
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm");  
		
	    System.out.println(sdf.format(date));  
	    System.out.println(sdf.format(cal.getTime()));  
	}
}
