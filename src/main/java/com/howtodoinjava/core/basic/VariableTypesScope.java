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
 * Examples for the tutorial "Java Variables: Types, Scope, var and Default Values".
 * https://howtodoinjava.com/java/basics/java-variables/
 */
public class VariableTypesScope {
    static class StepTracker {
        static int activeUsers = 0;         // class variable, one copy
        final String user;                  // instance variable
        int steps;                          // instance variable, starts at 0

        StepTracker(String user) {          // constructor parameter
            this.user = user;
            activeUsers++;
        }

        void walk(int count) {              // method parameter
            int before = steps;             // local variable
            steps = before + count;
        }
    }
    static class Limits {
        static final int MAX_STEPS_PER_DAY = 100_000;
    }
    public static void main(String[] args) throws Exception {
        {
            int steps = 8_000;                      // declare and initialize
            show("steps", steps);
            steps = steps + 500;                    // reassign
            int total = steps;                      // 8500
            show("total", total);
            var label = "Today";                    // type inferred as String
            show("label", label);
            final int dailyGoal = 10_000;           // cannot be reassigned
            show("dailyGoal", dailyGoal);
            double km = total / 1250.0;             // 6.8
            show("km", km);
            boolean reached = total >= dailyGoal;   // false
            show("reached", reached);
        }
        {
            int age = 30;                           // primitive type
            show("age", age);
            String city = "Pune";                   // reference type
            show("city", city);
            int[] scores = {72, 85, 90};            // array initializer uses braces
            show("scores", scores);
            int width = 4, height = 3;              // two variables, one statement
            show("width", width);
            int area = width * height;              // 12
            show("area", area);
        }
        {
            int small = 198;
            long wide = small;                      // 198, widening needs no cast
            show("wide", wide);
            byte narrow = (byte) small;             // -58, narrowing cuts the high bits
            show("narrow", narrow);
        }
        {
            StepTracker anna = new StepTracker("anna");
            StepTracker raj = new StepTracker("raj");
            anna.walk(1200);
            anna.walk(300);
            raj.walk(500);
            int annaSteps = anna.steps;             // 1500
            show("annaSteps", annaSteps);
            int rajSteps = raj.steps;               // 500
            show("rajSteps", rajSteps);
            int users = StepTracker.activeUsers;    // 2
            show("users", users);
        }
        {
            int dailyTotal = 0;
            for (int hour = 0; hour < 3; hour++) {
                int hourly = 400;                   // visible only inside the loop body
                dailyTotal += hourly;
            }
            int sum = dailyTotal;                   // 1200
            show("sum", sum);
        }
        {
            var readings = new ArrayList<Integer>(); // ArrayList<Integer>
            show("readings", readings);
            readings.add(1200);
            var count = readings.size();             // 1
            show("count", count);
        }
        {
            int total = List.of(1, 2, 3).stream()
                    .mapToInt(Integer::intValue)
                    .sum();                         // 6
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
