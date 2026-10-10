package com.howtodoinjava.core.exceptions;

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
import java.net.URL;

/**
 * Examples for the tutorial "IncompatibleClassChangeError: Implementing class (Fixed)".
 * https://howtodoinjava.com/java/exception-handling/solved-java-lang-incompatibleclasschangeerror-implementing-class/
 */
public class IncompatibleclasschangeerrorImplementingClass {
    static public interface PriceRule {
        int apply(int cents);
    }
    static public class HalfPrice implements PriceRule {
        public int apply(int cents) {
            return cents / 2;
        }
    }
    static public class Greeter {
        public String hi() {
            return "hi";
        }
    }
    public static void main(String[] args) throws Exception {
        {
            PriceRule rule = new HalfPrice();
            int price = rule.apply(900);                   // 450 with version 1.0 on the classpath
            show("price", price);
        }
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
