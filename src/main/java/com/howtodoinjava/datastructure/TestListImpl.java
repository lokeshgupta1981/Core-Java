package com.howtodoinjava.datastructure;

public class TestListImpl {

  public static void main(String[] args) {
    CustomList<String> namesList = new CustomList<>();

    namesList.add("Lokesh");
    namesList.add("Alex");

    System.out.println(namesList);  //[Lokesh,Alex]

    System.out.println(namesList.get(0)); //Lokesh

    namesList.remove(1);

    System.out.println(namesList);  //[Lokesh]
  }
}
