package com.howtodoinjava.puzzles;

public class CheckAllAlphabetsAlgorithms {

  public static void main(String[] args) {
    System.out.println(checkAllCharsUsingRegex("qwertyuioplkjhgfdsAzxcvbnm"));
    System.out.println(checkAllCharsUsingRegex("123"));
    System.out.println(checkAllCharsUsingRegex("ejuxggfsts"));
    System.out.println(checkAllCharsUsingRegex("wyyga"));
  }

  public static boolean checkAllCharsUsingRegex(String input) {

    //If input length is less than 26 then it can never be complete
    if (input.length() < 26) {
      return false;
    }
    //Even a single character is missing, return false
    String regex = "(?i)(?=.*a)(?=.*b)(?=.*c)(?=.*d)(?=.*e)(?=.*f)"
        + "(?=.*g)(?=.*h)(?=.*i)(?=.*j)(?=.*k)(?=.*l)(?=.*m)(?=.*n)"
        + "(?=.*o)(?=.*p)(?=.*q)(?=.*r)(?=.*s)(?=.*t)(?=.*u)(?=.*v)"
        + "(?=.*w)(?=.*x)(?=.*y)(?=.*z).*";

    if (input.matches(regex)) {
      return true;
    }
    return false;
  }

  public static boolean checkAllAlphabets(String input) {
    //If input length is less than 26 then it can never be complete
    if (input.length() < 26) {
      return false;
    }

    //Even a single character is missing, return false
    for (char ch = 'A'; ch <= 'Z'; ch++) {
      if (input.indexOf(ch) < 0 && input.indexOf((char) (ch + 32)) < 0) {
        return false;
      }
    }
    return true;
  }
}
