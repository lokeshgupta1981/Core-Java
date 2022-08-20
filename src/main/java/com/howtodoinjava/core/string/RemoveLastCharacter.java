package com.howtodoinjava.core.string;

import com.google.common.base.CharMatcher;
import org.apache.commons.lang3.StringUtils;

public class RemoveLastCharacter {
  public static void main(String[] args) {

    String string = "abcdE";

    String newString = StringUtils.chop(string);
    System.out.println(newString);

    newString = StringUtils.removeEndIgnoreCase(string, "E");
    System.out.println(newString);

    newString = trimLastChar(string);
    System.out.println(newString);

    newString = removeLastChar(string);
    System.out.println(newString);

    newString = deleteLastChar(string);
    System.out.println(newString);

    System.out.println(StringUtils.chop(null));
    System.out.println(StringUtils.chop(""));
    System.out.println(StringUtils.chop("a"));
    System.out.println(StringUtils.chop("abcd"));
  }

  public static String removeLastChar(String s) {
    return (s == null || s.length() == 0)
        ? null
        : (s.substring(0, s.length() - 1));
  }

  public static String deleteLastChar(String s) {
    return (s == null || s.length() == 0)
        ? null
        : new StringBuilder(s).deleteCharAt(s.length() - 1).toString();
  }

  public static String trimLastChar(String s) {
    return CharMatcher.is('E').trimTrailingFrom(s);
  }
}
