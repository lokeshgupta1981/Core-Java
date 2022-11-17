package com.howtodoinjava.core.basic;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;

public class InstanceOfOperatorExample {

  public static void main(String[] args) {
    HashMap map = new HashMap();

    Assertions.assertTrue(map instanceof Map);
    Assertions.assertTrue(map instanceof AbstractMap);

    Assertions.assertFalse(map instanceof List);

    map = null;
    Assertions.assertFalse(map instanceof Map);

    if(map instanceof Map) {
      //some action
    }

    int[] intArr = new int[0];
    Assertions.assertTrue(intArr instanceof Object);
    Assertions.assertTrue(intArr instanceof int[]);

    Integer[] integerArr = new Integer[0];
    Assertions.assertTrue(integerArr instanceof Object);
    Assertions.assertTrue(integerArr instanceof Object[]);
    Assertions.assertTrue(integerArr instanceof Integer[]);
    Assertions.assertTrue(integerArr instanceof Number[]);

    List<String> list = new ArrayList<>();

    if(list instanceof LinkedList) {
      LinkedList<String> linkedList = (LinkedList) list;
      //other actions
    } else if(list instanceof LinkedList) {
      ArrayList<String> arrayList = (ArrayList) list;
      //other actions
    }

    if(list instanceof LinkedList linkedList) {

      //other actions
    } else if(list instanceof ArrayList arrayList) {

      //other actions
    }

    Customer customer = null;
    String customerName;

    if(customer instanceof PersonalCustomer pc)
    {
      customerName = String.join(" ", pc.getFirstName(), pc.getMiddleName(), pc.getLastName());
    }
    else if(customer instanceof BusinessCustomer bc)
    {
      customerName = bc.getLegalName();
    }

    Object obj = new Object();
    if (obj instanceof String s) {
      System.out.println(s);
    } else if (obj instanceof Number n) {
      //System.out.println(s);
    }
    //System.out.println(s);

    if (!(obj instanceof String s)) {
      //System.out.println(s);
    }

    if (obj instanceof String s && s.startsWith("a")) {
      System.out.println(s);
    }

    /*if (obj instanceof String s || s.startsWith("a")) {
      //System.out.println(s);
    }*/

  }
}
