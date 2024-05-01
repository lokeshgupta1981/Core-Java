package com.howtodoinjava.core.streams;

import java.util.function.Function;
import lombok.Data;

public class MethodReferenceVsLambdaExamples {

  public static void main(String[] args) {

    System.out.println("Creating method reference");
    Function<Person, String> methodRef = Person::getFullName;

    System.out.println("Creating lambda expression");
    Function<Person, String> lambdaRef = (person) -> person.getFullName();

    Person person = new Person("Lokesh", "Gupta");

    System.out.println("a");
    System.out.println(methodRef.apply(person));
    System.out.println(methodRef.apply(person));
    System.out.println(methodRef.apply(person));

    System.out.println("b");
    System.out.println(lambdaRef.apply(person));
    System.out.println(lambdaRef.apply(person));
    System.out.println(lambdaRef.apply(person));


  }

  @Data
  static class Person {
    private String firstName;
    private String lastName;

    public Person() {
      System.out.println("Default Constructor called.");
    }

    public Person(String firstName, String lastName) {
      System.out.println("Custom Constructor called.");
      this.firstName = firstName;
      this.lastName = lastName;
    }

    public String getFullName() {
      return STR."\{firstName} \{lastName}";
    }
  }
}

