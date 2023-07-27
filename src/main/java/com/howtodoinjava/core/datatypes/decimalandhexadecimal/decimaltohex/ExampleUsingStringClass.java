package com.howtodoinjava.core.datatypes.decimalandhexadecimal.decimaltohex;

public class ExampleUsingStringClass {

    private static String usingFormat(int number) {

        return String.format("%x", number);

    }

    private static String usingFormatMinimum4Digits(int number) {

        return String.format("%04x", number);

    }

    public static void main(String[] args) {

        int number = 269;

        System.out.println(usingFormat(number));

        System.out.println(usingFormatMinimum4Digits(number));

    }

}
