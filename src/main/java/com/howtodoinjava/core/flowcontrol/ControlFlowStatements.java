package com.howtodoinjava.core.flowcontrol;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.util.regex.*;
import java.nio.charset.*;
import java.nio.*;
import java.security.*;
import java.lang.reflect.*;
import java.lang.invoke.*;

/**
 * Examples for the tutorial "Java Control Flow Statements: Decisions, Loops and Branching".
 * https://howtodoinjava.com/java/flow-control/control-flow-statements/
 */
public class ControlFlowStatements {

    public static void main(String[] args) throws Exception {
        {
            int[] orders = {120, 0, 75, 300, 40};
            int total = 0;
            for (int amount : orders) {             // loop
                if (amount == 0) {                  // decision
                    continue;                       // branch, skip unpaid orders
                }
                if (total + amount > 400) {
                    break;                          // branch, stop at the limit
                }
                total += amount;
            }
            int paid = total;                       // 195
            show("paid", paid);
        }
        {
            int age = 17;
            String ticket;
            if (age < 12) {
                ticket = "child";
            } else if (age < 18) {
                ticket = "teen";
            } else {
                ticket = "adult";
            }
            String chosen = ticket;                 // "teen"
            show("chosen", chosen);
        }
        {
            DayOfWeek day = DayOfWeek.SATURDAY;
            String kind = switch (day) {
                case SATURDAY, SUNDAY -> "weekend";
                default -> "weekday";
            };
            String result = kind;                   // "weekend"
            show("result", result);
        }
        {
            int attempts = 0;
            int seed = 3;
            do {
                seed = (seed * 3) % 10;
                attempts++;
            } while (seed != 1);
            int tries = attempts;                   // 3
            show("tries", tries);
        }
        {
            int[][] grid = {{3, 8}, {5, 42}, {9, 1}};
            int row = -1;
            outer:
            for (int r = 0; r < grid.length; r++) {
                for (int value : grid[r]) {
                    if (value == 42) {
                        row = r;
                        break outer;                // leaves both loops
                    }
                }
            }
            int foundRow = row;                     // 1
            show("foundRow", foundRow);
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
