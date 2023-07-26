package com.howtodoinjava.core.datatypes.decimalandhexadecimal.decimaltohex;

public class ExampleUsingLongClass {

    private static String usingToString(long number) {

        return Long.toString(number, 16);

    }

    private static String usingToHexString(long number) {

        return Long.toHexString(number);

    }

    public static void main(String[] args) {

        long number = 269;

        System.out.println(usingToString(number));

        System.out.println(usingToHexString(number));

    }

}
