package com.howtodoinjava.core.datetime;

	import java.time.DayOfWeek;
	import java.time.LocalDate;
	import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
	import java.util.Optional;
	import java.util.function.Predicate;
	import java.util.stream.Stream;
	
	public class BusinessDaysExamples 
	{
		public static void main(String[] args) 
		{
			LocalDate today = LocalDate.of(2020, 5, 5);
			
			System.out.println(addBusinessDays(today, 8, Optional.empty()));
			
			//Add one holiday for testing
			List<LocalDate> holidays = new ArrayList<>();
			holidays.add(LocalDate.of(2020, 5, 11));
			holidays.add(LocalDate.of(2020, 5, 1));
			
			System.out.println(addBusinessDays(today, 8, Optional.of(holidays)));
			
			System.out.println(subtractBusinessDays(today, 8, Optional.empty()));
			System.out.println(subtractBusinessDays(today, 8, Optional.of(holidays)));
			
			System.out.println(countBusinessDaysBetween(today, today.plusDays(20), Optional.empty()));
			System.out.println(countBusinessDaysBetween(today, today.plusDays(20), Optional.of(holidays)));
		}
		
		private static LocalDate addBusinessDays(LocalDate localDate, int days, Optional<List<LocalDate>> holidays)
		{
			if(localDate == null || days <= 0 || holidays == null)
			{
				throw new IllegalArgumentException("Invalid method argument(s) "
						+ "to addBusinessDays("+localDate+","+days+","+holidays+")");
			}
			
			Predicate<LocalDate> isHoliday = date -> holidays.isPresent() ? holidays.get().contains(date) : false;
			
			Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY 
													|| date.getDayOfWeek() == DayOfWeek.SUNDAY;
			
			LocalDate result = localDate;
			while (days > 0) {
		        result = result.plusDays(1);
		        if (isHoliday.or(isWeekend).negate().test(result)) {
		            days--;
		        }
		    }
			return result;
		}
		
		private static LocalDate subtractBusinessDays(LocalDate localDate, int days, Optional<List<LocalDate>> holidays)
		{
			if(localDate == null || days <= 0 || holidays == null)
			{
				throw new IllegalArgumentException("Invalid method argument(s) "
						+ "to subtractBusinessDays("+localDate+","+days+","+holidays+")");
			}
			
			Predicate<LocalDate> isHoliday = date -> holidays.isPresent() ? holidays.get().contains(date) : false;
			
			Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY 
													|| date.getDayOfWeek() == DayOfWeek.SUNDAY;
			
			LocalDate result = localDate;
			while (days >= 0) {
		        result = result.minusDays(1);
		        if (isHoliday.or(isWeekend).negate().test(result)) {
		            days--;
		        }
		    }
			return result;
		}
		
		private static long countBusinessDaysBetween(LocalDate startDate, LocalDate endDate, Optional<List<LocalDate>> holidays)
		{
			if(startDate == null || endDate == null || holidays == null)
			{
				throw new IllegalArgumentException("Invalid method argument(s) to countBusinessDaysBetween("+startDate+","+endDate+","+holidays+")");
			}
			
			Predicate<LocalDate> isHoliday = date -> holidays.isPresent() ? holidays.get().contains(date) : false;
			
			Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY 
													|| date.getDayOfWeek() == DayOfWeek.SUNDAY;
			
			long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
			
			long businessDays = Stream.iterate(startDate, date -> date.plusDays(1))
				    					.limit(daysBetween)
				    					.filter(isHoliday.or(isWeekend).negate())
				    					.count();
			return businessDays;
		}
	}
