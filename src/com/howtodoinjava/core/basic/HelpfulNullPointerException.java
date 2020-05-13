package com.howtodoinjava.core.basic;

public class HelpfulNullPointerException 
{
	public static void main(String[] args) 
	{
		Employee e = null;
		
		System.out.println(e.getName());
	}
}

class Employee {
	Long id;
	String name;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}