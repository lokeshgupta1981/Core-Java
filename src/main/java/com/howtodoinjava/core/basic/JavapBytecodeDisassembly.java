package com.howtodoinjava.core.basic;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;
import java.util.function.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.time.*;
import java.math.*;

/**
 * Examples for the tutorial "javap: View Bytecode of a Java Class File".
 * https://howtodoinjava.com/java/basics/how-to-view-generate-bytecode-for-a-java-class-file/
 */
public class JavapBytecodeDisassembly {
    static public class ShippingFee {

        private static final int FREE_ABOVE = 5000;

        public int fee(int orderCents) {
            if (orderCents >= FREE_ABOVE) {
                return 0;
            }
            return 499;
        }

        public String label(String city) {
            return "Ship to " + city;
        }
    }
    static public class Discounts {

        public IntUnaryOperator halfOff() {
            return cents -> cents / 2;
        }
    }
    public static void main(String[] args) throws Exception {
    }

    static void show(String name, Object value) {
        String text = value instanceof int[] a ? Arrays.toString(a)
        : value instanceof long[] a ? Arrays.toString(a)
        : value instanceof double[] a ? Arrays.toString(a)
        : value instanceof Object[] a ? Arrays.deepToString(a)
        : value instanceof String str ? "\"" + str + "\""
        : String.valueOf(value);
        System.out.println(name + " = " + text);
    }
}
