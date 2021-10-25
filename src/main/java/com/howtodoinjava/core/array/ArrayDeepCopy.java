package com.howtodoinjava.core.array;

import java.io.Serializable;

import org.apache.commons.lang3.SerializationUtils;

public class ArrayDeepCopy 
{
	public static void main(String[] args)
    {
        Employee[] empArr = new Employee[2];    //Original array
 
        empArr[0] = new Employee(100, "Lokesh", "Gupta", new Department(1, "HR"));
        empArr[1] = new Employee(200, "Pankaj", "Kumar", new Department(2, "Finance"));
         
        Employee[] copiedArray = SerializationUtils.clone(empArr);  //Deep copied array
         
        //Change state of original array
        empArr[0].setFirstName("Unknown");
        empArr[0].getDepartment().setName("Unknown");
         
        //Verify the change in original array - CHANGED
        System.out.println(empArr[0].getFirstName());                   //Unknown
        System.out.println(empArr[0].getDepartment().getName());        //Unknown
 
        //Verify the change in deep copied array - UNCHANGED
        System.out.println(copiedArray[0].getFirstName());              //Lokesh
        System.out.println(copiedArray[0].getDepartment().getName());   //HR
    }
}

class Employee implements Serializable {
	 
    private static final long serialVersionUID = 1L;
     
    private Integer id;
    private String firstName;
    private String lastName;
    private Department department;
 
    public Employee(int id, String fName, String lName, Department department) {
        super();
        this.id = id;
        this.firstName = fName;
        this.lastName = lName;
        this.department = department;
    }
 
    public Integer getId() {
        return id;
    }
 
    public void setId(Integer id) {
        this.id = id;
    }
 
    public String getFirstName() {
        return firstName;
    }
 
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
 
    public String getLastName() {
        return lastName;
    }
 
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
 
    public Department getDepartment() {
        return department;
    }
 
    public void setDepartment(Department department) {
        this.department = department;
    }
 
    @Override
    public String toString() {
        return "Employee [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + "]";
    }
}

class Department implements Serializable {
    
    private static final long serialVersionUID = 1L;
     
    Integer id;
    String name;
     
    public Department(Integer id, String name) {
        super();
        this.id = id;
        this.name = name;
    }
     
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "Department [id=" + id + ", name=" + name + "]";
    }
}