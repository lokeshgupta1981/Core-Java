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
 * Examples for the tutorial "Introduction to Java Programming Language (Java 25)".
 * https://howtodoinjava.com/java/basics/java-introduction/
 */
public class SourceBytecodeJvm {
    static public class Greeting {
        public static void main(String[] args) {
            System.out.println("Hello, Java");
        }
    }
    public static void main(String[] args) throws Exception {
        {
            int count = 42;                                     // 42, a primitive, not an object
            show("count", count);
            Integer boxed = count;                              // 42, autoboxed into an Integer object
            show("boxed", boxed);
            int feature = Runtime.version().feature();          // 25
            show("feature", feature);
            String vmName = System.getProperty("java.vm.name"); // "OpenJDK 64-Bit Server VM"
            show("vmName", vmName);
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
