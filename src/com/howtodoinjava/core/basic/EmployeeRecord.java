package com.howtodoinjava.core.basic;

import java.io.Serializable;

public record EmployeeRecord (
		Long id, 
		String firstName, 
		String lastName, 
		String email, 
		int age) implements Serializable 
{
	public EmployeeRecord
	{
		if(age < 18)
		{
			throw new IllegalArgumentException(
	                "You cannot hire a minor person as employee");
		}
	}
}
