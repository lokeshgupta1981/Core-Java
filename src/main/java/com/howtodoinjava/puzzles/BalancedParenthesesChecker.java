package com.howtodoinjava.puzzles;

import java.util.Stack;

public class BalancedParenthesesChecker {

  public static void main(String[] args) {
    String expression1 = "{[ a; ( b; ) c; ]}";
    String expression2 = "{[ a; ( b; ) c; }]"; //incorrectly matched in last two characters

    checkBalancedParentheses(expression1);  //true
    checkBalancedParentheses(expression2);  //false
  }

  public static boolean checkBalancedParentheses(String expression) {
    Stack<Character> stack = new Stack<>();

    for (char ch : expression.toCharArray()) {
      if (ch == '(' || ch == '{' || ch == '[') {
        stack.push(ch);
      } else if (ch == ')' || ch == '}' || ch == ']') {

        if (stack.isEmpty()) {
          return false;
        }

        char top = stack.pop();
        if ((ch == ')' && top != '(') ||
            (ch == '}' && top != '{') ||
            (ch == ']' && top != '[')) {
          return false;
        }
      }
    }
    return stack.isEmpty();
  }
}
