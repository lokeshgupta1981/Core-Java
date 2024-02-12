package com.howtodoinjava.xml.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "employee")
@XmlAccessorType(XmlAccessType.FIELD)
public class Employee implements Serializable {

  private static final long serialVersionUID = 1L;

  //@XmlAttribute
  private Integer id;
  private String firstName;
  private String lastName;
  private String location;
  private Department department;

  public Employee() {
    super();
  }

  public Employee(int id, String fName, String lName, String location, Department department) {
    super();
    this.id = id;
    this.firstName = fName;
    this.lastName = lName;
    this.location = location;
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

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  @Override
  public String toString() {
    return STR."Employee{id=\{id}, firstName='\{firstName}\{'\''}, lastName='\{lastName}\{'\''}, location='\{location}\{'\''}, department=\{department}\{'}'}";
  }
}