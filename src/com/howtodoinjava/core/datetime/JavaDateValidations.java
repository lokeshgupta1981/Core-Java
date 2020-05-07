package com.howtodoinjava.core.datetime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Date;

public class JavaDateValidations 
{
	public static void main(String[] args) 
	{
		String dateFormat = "MM-dd-yyyy";
		String dateString = "05-26-0020";
		
		Date parsedDate = validateAndParseDateJava7(dateString, dateFormat);
		System.out.println(parsedDate);
		
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateFormat)
											.withResolverStyle(ResolverStyle.STRICT);
		LocalDate parsedLocalDate = validateAndParseDateJava8(dateString, dateFormatter);
		System.out.println(parsedLocalDate);
	}
	
	//Java 8 - Use DateTimeFormatter (thread-safe)
	public static LocalDate validateAndParseDateJava8(String dateStr, DateTimeFormatter dateFormatter) {
		LocalDate date = null;
        try {
        	date = LocalDate.parse(dateStr, dateFormatter);
        } catch (DateTimeParseException e) {
        	e.printStackTrace();
        }
        return date;
    }
	
	//Java 7 - Use SimpleDateFormat (not thread-safe)
	public static Date validateAndParseDateJava7(String dateString, String dateFormat) {
		Date date = null;
        DateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
        	e.printStackTrace();
        }
        return date;
    }
}
