package com.howtodoinjava.core.basic;

public class AssertExample {

  public static void main(String[] args) {
    Person person = new Person(1L, "Lokesh", 40);

    //Passes successfully
    assert person.age() >= 18 : "Age is less than 18";

    Person person1 = new Person(1L, "Lokesh", 14);

    //Failed
    assert person1.age() >= 18 : "Age is less than 18";

    System.out.println("Done");
  }
}

record Person(Long id, String name, int age) {
}