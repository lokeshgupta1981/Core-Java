package com.howtodoinjava.cloning;

import lombok.Data;

public class DeepCloningExample {

  public static void main(String[] args) throws CloneNotSupportedException {
    Department hr = new Department(1, "Human Resource");

    Employee original = new Employee(1, "Admin", hr);
    Employee cloned = (Employee) original.clone();

    //Let change the department name in cloned object and we will verify in original object
    cloned.getDepartment().setName("Finance");

    System.out.println(original.getDepartment().getName());
    System.out.println(cloned.getDepartment().getName());
  }
}

@Data
class Employee implements Cloneable {

  private int employeeId;
  private String employeeName;
  private Department department;

  public Employee(int id, String name, Department dept) {
    this.employeeId = id;
    this.employeeName = name;
    this.department = dept;
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    Employee cloned = (Employee) super.clone();
    cloned.setDepartment((Department) cloned.getDepartment().clone());
    return cloned;
  }
}

@Data
class Department implements Cloneable {

  private int id;
  private String name;

  public Department(int id, String name) {
    this.id = id;
    this.name = name;
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}