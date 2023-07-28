package com.howtodoinjava.core.datatypes.decimalandhexadecimal.hextodecimal;

import java.math.BigDecimal;

public class ExampleUsingRawMethod {

    public static BigDecimal convertToDecimal(String hexadecimalString) {

        String tokens = "0123456789ABCDEF";

        hexadecimalString = hexadecimalString.toUpperCase();

        BigDecimal result = new BigDecimal(0);

        BigDecimal multiplicand = new BigDecimal(16);

        for (int i = 0; i < hexadecimalString.length(); i++) {

            int n = tokens.indexOf(hexadecimalString.charAt(i));

            result = result.multiply(multiplicand).add(new BigDecimal(n));


        }

        return result;

    }

    public static void main(String[] args) {

        System.out.println(convertToDecimal("10D"));

    }

}
