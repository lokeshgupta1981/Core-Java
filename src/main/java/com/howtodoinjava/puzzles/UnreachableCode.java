package com.howtodoinjava.puzzles;

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
 * Examples for the tutorial "Unreachable Code in Java: Errors, Dead Code and Fixes".
 * https://howtodoinjava.com/java/puzzles/dead-code-and-unreachable-code-in-java-puzzle/
 */
public class UnreachableCode {
    void pay() {
        if (DEBUG) {
            System.out.println("debug: card checked");   // never runs, compiles
        }
        System.out.println("paid");                      // paid
    }

    static final boolean DEBUG = false;
    static int nextTicket() {
        int ticket = 0;
        while (true) {
            ticket++;
            if (ticket > 3) {
                return ticket;
            }
        }
    }
    static void placeOrder() {
        try {
            System.out.println("saved");      // saved
            return;
        } finally {
            System.out.println("sent");       // sent
        }
    }
    public static void main(String[] args) throws Exception {
        {
            int attempts = 0;
            while (true) {
                attempts++;
                if (attempts == 3) {
                    break;
                }
            }
            int stoppedAfter = attempts;                      // 3
            show("stoppedAfter", stoppedAfter);
        }
        {
            int ticket = nextTicket();                       // 4
            show("ticket", ticket);
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
