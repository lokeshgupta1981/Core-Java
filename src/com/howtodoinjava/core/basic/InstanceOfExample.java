package com.howtodoinjava.core.basic;

public class InstanceOfExample 
{
	public static void main(String[] args) 
	{
		Customer customer = null;	//get this value from some method
		
		String customerName = "";
		
		//Old approach
		
		if(customer instanceof PersonalCustomer)
		{
			PersonalCustomer pCustomer = (PersonalCustomer) customer;	//Redundant casting
			customerName = String.join(" ", pCustomer.getFirstName(), 
									pCustomer.getMiddleName(), 
									pCustomer.getLastName());
		}
		else if(customer instanceof BusinessCustomer)
		{
			BusinessCustomer bCustomer = (BusinessCustomer) customer;	//Redundant casting
			customerName = bCustomer.getLegalName();
		}
		
		//New approach
		
		if(customer instanceof PersonalCustomer pCustomer 
				&& pCustomer.getId() > 0)
		{
			customerName = String.join(" ", pCustomer.getFirstName(), 
									pCustomer.getMiddleName(), 
									pCustomer.getLastName());
		}
		else if(customer instanceof BusinessCustomer bCustomer)
		{
			customerName = bCustomer.getLegalName();
		}
	}
}
