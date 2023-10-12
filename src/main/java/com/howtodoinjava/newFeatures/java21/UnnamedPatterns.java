package com.howtodoinjava.newFeatures.java21;

import java.time.LocalDate;

public class UnnamedPatterns {

  public static void main(String[] args) {
    Department department = new Department(1L, "HR", true);
    Employee employee = new Employee(101L, "John Doe", LocalDate.of(1990, 5, 15), true, department);

    //With unused pattern variables
    if (employee instanceof Employee(Long id, String name, LocalDate dateOfBirth, Boolean status,
                                     Department(Long depId, String depName, Boolean depStatus))) {

      System.out.printf("Employee %d works in department %s.", id, depName);
    }

    if (department instanceof Department(_, String name, _)) {
      System.out.println("Department Name is : " + name);  // Prints 'Department Name is : HR'
    }

    if (employee instanceof Employee(Long id, _, _, _, Department(_, String depName, _))) {
      System.out.printf("Employee %d works in department %s.", id, depName);
    }

    if (employee instanceof Employee(Long id, _, _, _, _)) {
      System.out.println("Employee Id is : " + id);  // Prints 'Employee Id is : 101'
    }
  }
}

record Department(Long id, String name, Boolean status) {
  // Constructor and methods (if any)
}

record Employee(Long id, String name, LocalDate dateOfBirth, Boolean status, Department department) {
  // Constructor and methods (if any)
}
