package com.howtodoinjava.core.string;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;

public class ReverseWords {

  public static void main(String[] args) {
    String input = "alex brian charles";

    //1 - Stream
    String reversed = Arrays.stream(input.split(" "))
        .map(word -> new StringBuilder(word).reverse())
        .collect(Collectors.joining(" "));

    System.out.println(reversed);


    //2 - StringUtils
    reversed = StringUtils.reverseDelimited(StringUtils.reverse(input), ' ');
    System.out.println(reversed);
  }
}
