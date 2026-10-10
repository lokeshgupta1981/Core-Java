package com.howtodoinjava.core.examples;

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
import java.util.Scanner;

/**
 * Examples for the tutorial "Java Examples: Programs by Task, Add Two Integers and More".
 * https://howtodoinjava.com/java-examples/examples/
 */
public class ExamplesByTask {
    static int addWithoutPlus(int a, int b) {
        while (b != 0) {
            int carry = (a & b) << 1;
            a = a ^ b;
            b = carry;
        }
        return a;
    }
    public static void main(String[] args) throws Exception {
        {
            int apples = 10;
            int pears = 20;
            int total = apples + pears;                       // 30
            show("total", total);
            int wrapped = Integer.MAX_VALUE + 1;              // -2147483648, int overflow
            show("wrapped", wrapped);
            long widened = (long) Integer.MAX_VALUE + 1;      // 2147483648
            show("widened", widened);
            try { int checked = Math.addExact(apples, pears); show("checked", checked); } catch (Throwable _t) { System.out.println("checked -> " + _t); }
        }
        {
            int first = 15;
            int second = 27;
            int sum = first + second;                        // 42
            show("sum", sum);
            String message = "Sum: " + sum;                  // "Sum: 42"
            show("message", message);
            String wrong = "Sum: " + first + second;         // "Sum: 1527", string concatenation
            show("wrong", wrong);
        }
        {
            Scanner input = new Scanner("15 abc 27");          // new Scanner(System.in) in a real program
            show("input", input);
            int firstNumber = input.hasNextInt() ? input.nextInt() : 0;   // 15
            show("firstNumber", firstNumber);
            boolean secondValid = input.hasNextInt();                     // false, "abc" is not a number
            show("secondValid", secondValid);
            String rejected = input.next();                               // "abc"
            show("rejected", rejected);
            int secondNumber = input.hasNextInt() ? input.nextInt() : 0;  // 27
            show("secondNumber", secondNumber);
            int userSum = firstNumber + secondNumber;                     // 42
            show("userSum", userSum);
        }
        {
            int videoBytes = 1_610_612_736;                              // 1.5 GB
            show("videoBytes", videoBytes);
            int intTotal = videoBytes + videoBytes;                      // -1073741824, wrapped around
            show("intTotal", intTotal);
            long longTotal = (long) videoBytes + videoBytes;             // 3221225472
            show("longTotal", longTotal);
            try { int exactTotal = Math.addExact(videoBytes, videoBytes); show("exactTotal", exactTotal); } catch (Throwable _t) { System.out.println("exactTotal -> " + _t); }
        }
        {
            int bitSum = addWithoutPlus(15, 27);              // 42
            show("bitSum", bitSum);
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
