package com.howtodoinjava.core.datatypes.decimalandhexadecimal.hextodecimal;

public class ExampleUsingLongClass {

    private static long usingParseLong(String number) throws NumberFormatException {

        return Long.parseLong(number, 16);

    }

    private static long usingValueOf(String number) throws NumberFormatException {

        return Long.valueOf(number, 16);

    }

    private static long usingDecode(String number) throws NumberFormatException {

        return Long.decode("0X"+number);

    }

    public static void main(String[] args) {

        try{

            String hexadecimalNumber = "10D";

            System.out.println(usingParseLong(hexadecimalNumber));

            System.out.println(usingValueOf(hexadecimalNumber));

            System.out.println(usingDecode(hexadecimalNumber));

        } catch (NumberFormatException exception){

            System.out.println(exception.getMessage());

        }

    }

}
