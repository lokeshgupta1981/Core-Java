package com.howtodoinjava.core.datatypes.decimalandhexadecimal.hextodecimal;

public class FloatingPointHexToDecimal {

    public static void main(String[] args) {

        String doubleHex = "0x1.4d70a3d70a3d7p8";

        String floatHex = "0x1.4d70a4p8";

        System.out.println(Float.parseFloat(floatHex));

        System.out.println(Double.parseDouble(doubleHex));

    }

}
