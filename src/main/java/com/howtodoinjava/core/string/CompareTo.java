package com.howtodoinjava.core.string;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

public class CompareTo {
  public static void main(String[] args) {

    String name = "alex";

    //same string
    assertThat(name.compareTo("alex"), equalTo(0));

    //Different cases
    assertThat(name.compareTo("Alex"), greaterThan(0));
    assertThat(name.compareTo("ALEX"), greaterThan(0));

    //Different strings
    assertThat(name.compareTo("alexa"), lessThan(0));
    assertThat(name.compareTo("ale"), greaterThan(0));
  }
}
