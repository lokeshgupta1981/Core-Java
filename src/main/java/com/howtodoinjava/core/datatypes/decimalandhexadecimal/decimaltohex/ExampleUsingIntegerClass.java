package com.howtodoinjava.core.datatypes.decimalandhexadecimal.decimaltohex;

public class ExampleUsingIntegerClass {

    private static String usingToString(int number) {

        return Integer.toString(number, 16);

    }

    private static String usingToHexString(int number) {

        return Integer.toHexString(number);

    }

    public static void main(String[] args) {

        int number = 269;

        System.out.println(usingToString(number));

        System.out.println(usingToHexString(number));
        
    }

}
