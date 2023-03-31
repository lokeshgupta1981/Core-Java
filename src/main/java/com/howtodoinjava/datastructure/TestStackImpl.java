package com.howtodoinjava.datastructure;

public class TestStackImpl {

  public static void main(String[] args) {
    Stack stack = new Stack(5);

    stack.push(1);
    stack.push(2);
    stack.push(3);
    stack.push(4);
    stack.push(5);

    System.out.println(stack);

    stack.push(6);

    stack.pop();

    System.out.println(stack);

    stack.push(6);

    System.out.println(stack);
  }
}
