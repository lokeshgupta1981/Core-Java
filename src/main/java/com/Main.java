package com;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

  static Logger log = LoggerFactory.getLogger(Main.class);

  /*public static void main(String[] args) {
    *//*log.debug("Sample debug message");
    log.info("Sample info message");
    log.error("Sample error message");*//*

    int[] arr1 = new int[]{10, 25, 63, 98, 74};
    System.out.println(Arrays.toString(arr1));

    List list = Arrays.asList(arr1);

    Collections.reverse(list);
    list.stream().forEach(System.out::print);
    System.out.println(Arrays.toString(arr1));
  }*/

  public static void main(String[] asrg){
    Name n = null;
    n.m1();
  }
}

class Name{
  static void m1(){
    System.out.println("Name class m1");
  }
}
