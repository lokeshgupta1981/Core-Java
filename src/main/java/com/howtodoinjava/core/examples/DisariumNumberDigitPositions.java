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

/**
 * Examples for the tutorial "Disarium Number in Java: Program, Examples and Full List".
 * https://howtodoinjava.com/java-examples/disarium-number/
 */
public class DisariumNumberDigitPositions {
    static long power(int digit, int exponent) {
        long result = 1;
        for (int i = 0; i < exponent; i++) {
            result *= digit;
        }
        return result;
    }
    static boolean isDisarium(long originalNumber) {
        if (originalNumber < 0) {
            return false;
        }
        // Total number of digits = position of the rightmost digit
        int position = Long.toString(originalNumber).length();
        long sumOfDigits = 0;
        long tempNum = originalNumber;
        while (tempNum != 0) {
            int currentDigit = (int) (tempNum % 10);              // rightmost digit
            sumOfDigits += power(currentDigit, position--);       // power by position
            tempNum = tempNum / 10;
        }
        // The number is disarium when the sum equals the original number
        return sumOfDigits == originalNumber;
    }
    static boolean isDisarium(BigInteger number) {
        String digits = number.toString();
        BigInteger sum = BigInteger.ZERO;
        for (int i = 0; i < digits.length(); i++) {
            BigInteger digit = BigInteger.valueOf(digits.charAt(i) - '0');
            sum = sum.add(digit.pow(i + 1));
        }
        return sum.equals(number);
    }
    static boolean isArmstrong(long n) {
        int digitCount = Long.toString(n).length();
        long sum = 0;
        for (long t = n; t != 0; t /= 10) {
            sum += power((int) (t % 10), digitCount);
        }
        return sum == n;
    }
    public static void main(String[] args) throws Exception {
        {
            String digits = "135";
            int sum = IntStream.range(0, digits.length()).map(i -> (int) Math.pow(digits.charAt(i) - '0', i + 1)).sum();   // 135
            show("sum", sum);
            boolean disarium = sum == Integer.parseInt(digits);                                                          // true
            show("disarium", disarium);
        }
        {
            boolean n134 = isDisarium(134);        // false
            show("n134", n134);
            boolean n135 = isDisarium(135);        // true
            show("n135", n135);
            boolean n136 = isDisarium(136);        // false
            show("n136", n136);
            boolean n2427 = isDisarium(2427);      // true
            show("n2427", n2427);
            boolean n2646798 = isDisarium(2_646_798);   // true
            show("n2646798", n2646798);
            boolean negative = isDisarium(-135);   // false
            show("negative", negative);
        }
        {
            List<Long> upTo1000 = LongStream.rangeClosed(1, 1_000).filter(n -> isDisarium(n)).boxed().toList();            // [1, 2, 3, 4, 5, 6, 7, 8, 9, 89, 135, 175, 518, 598]
            show("upTo1000", upTo1000);
            List<Long> upTo3Million = LongStream.rangeClosed(1_000, 3_000_000).filter(n -> isDisarium(n)).boxed().toList();  // [1306, 1676, 2427, 2646798]
            show("upTo3Million", upTo3Million);
        }
        {
            boolean largest = isDisarium(new BigInteger("12157692622039623539"));   // true
            show("largest", largest);
            boolean neighbour = isDisarium(new BigInteger("12157692622039623538")); // false
            show("neighbour", neighbour);
        }
        {
            boolean a153 = isArmstrong(153);     // true
            show("a153", a153);
            boolean d153 = isDisarium(153);      // false
            show("d153", d153);
            boolean a135 = isArmstrong(135);     // false
            show("a135", a135);
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
