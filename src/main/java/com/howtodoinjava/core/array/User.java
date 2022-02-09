package com.howtodoinjava.core.array;

public class User implements Comparable<User> {

  public long id;
  public String firstName;
  public String lastName;

  public long getId() {
    return id;
  }

  public void setId(final long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(final String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(final String lastName) {
    this.lastName = lastName;
  }

  @Override
  public String toString() {
    return "Employee [id=" + id + ", firstName=" + firstName + ", lastName="
      + lastName + "]";
  }

  @Override
  public int compareTo(final User user) {
    if(user == null ) {
      return -1;
    } else {
      return (int)(this.id - user.id);
    }
  }
}
