package com.howtodoinjava.core.datatypes.decimalandhexadecimal.decimaltohex;

import java.math.BigInteger;

public class ExampleUsingBigIntegerClass {

    public static void main(String[] args) {

        BigInteger decimal = new BigInteger("269");

        System.out.println(decimal.toString(16));

    }

}
