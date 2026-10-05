package com.howtodoinjava.iae;

/**
 * Run without arguments to see the stack trace of an unknown enum name.
 * Run with a size name, e.g. "small", to see the safe lookup.
 */
public class SizeDemo {

  public static void main(String[] args) {
    if (args.length > 0) {
      Size size = Size.parse(args[0]).orElse(Size.MEDIUM);
      System.out.println("Size: " + size);
      return;
    }
    Size size = Size.valueOf("XL");
    System.out.println("Size: " + size);
  }
}
