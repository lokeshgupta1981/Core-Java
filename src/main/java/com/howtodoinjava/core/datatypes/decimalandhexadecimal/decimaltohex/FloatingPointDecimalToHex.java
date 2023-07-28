package com.howtodoinjava.core.datatypes.decimalandhexadecimal.decimaltohex;

public class FloatingPointDecimalToHex {

    public static void main(String[] args) {

        double number = 333.44d;

        String hex = Double.toHexString(number);

        System.out.println(hex);

        float number1 = 333.44f;

        hex = Float.toHexString(number1);

        System.out.println(hex);

    }

}
