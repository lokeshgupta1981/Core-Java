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
 * Examples for the tutorial "Java Hello World Program: Java 25 and Classic Versions".
 * https://howtodoinjava.com/java/basics/java-hello-world-program/
 */
public class HelloWorldCompileRun {
    static public class HelloWorld {
        public static void main(String[] args) {
            System.out.println("Hello, World!");
        }
    }
    public static void main(String[] args) throws Exception {
        {
            String name = "World";
            String plain = "Hello, " + name + "!";                  // "Hello, World!"
            show("plain", plain);
            String formatted = String.format("Hello, %s!", name);   // "Hello, World!"
            show("formatted", formatted);
            int letters = name.length();                            // 5
            show("letters", letters);
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
