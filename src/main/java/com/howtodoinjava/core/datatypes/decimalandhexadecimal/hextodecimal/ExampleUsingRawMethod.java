package com.howtodoinjava.core.datatypes.decimalandhexadecimal.hextodecimal;

public class ExampleUsingRawMethod {

    private static int rawMethod(String hexadecimalString) {

        String tokens = "0123456789ABCDEF";

        hexadecimalString = hexadecimalString.toUpperCase();

        int result = 0;

        for (int i = 0; i < hexadecimalString.length(); i++) {

            int n = tokens.indexOf(hexadecimalString.charAt(i));

            result = result * 16 + n;

        }

        return result;

    }

    public static void main(String[] args) {

        System.out.println(rawMethod("10D"));

    }

}
