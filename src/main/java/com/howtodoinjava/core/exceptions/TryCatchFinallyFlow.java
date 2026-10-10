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
import java.time.format.DateTimeParseException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Examples for the tutorial "Java try catch finally: Flow, Rules and Examples".
 * https://howtodoinjava.com/java/exception-handling/try-catch-finally/
 */
public class TryCatchFinallyFlow {
    static String reserve(String nightsText) {
        List<String> steps = new ArrayList<>();
        try {
            int nights = Integer.parseInt(nightsText);
            steps.add("booked " + nights);
        } catch (NumberFormatException e) {
            steps.add("catch");
        } finally {
            steps.add("finally");
        }
        return String.join(" > ", steps);
    }
    static String parseBooking(String date, String nights) {
        try {
            LocalDate checkIn = LocalDate.parse(date);
            int count = Integer.parseInt(nights);
            return checkIn + " for " + count + " nights";
        } catch (DateTimeParseException e) {
            return "bad date: " + date;
        } catch (NumberFormatException e) {
            return "bad nights: " + nights;
        }
    }
    static String validate(String date, String nights) {
        try {
            LocalDate.parse(date);
            Integer.parseInt(nights);
            return "ok";
        } catch (DateTimeParseException | NumberFormatException e) {
            return "invalid input";
        }
    }
    static int nightsCharged() {
        int nights = 2;
        try {
            return nights;                        // 2 is saved here
        } finally {
            nights = 5;                           // does not change the result
        }
    }
    @SuppressWarnings("finally")
    static String confirm() {
        try {
            throw new IllegalStateException("payment declined");
        } finally {
            return "confirmed";                   // hides the exception
        }
    }
    public static void main(String[] args) throws Exception {
        {
            String valid = reserve("3");               // "booked 3 > finally"
            show("valid", valid);
            String invalid = reserve("three");         // "catch > finally"
            show("invalid", invalid);
        }
        {
            String booking = parseBooking("2026-12-24", "2");   // "2026-12-24 for 2 nights"
            show("booking", booking);
            String badDate = parseBooking("24/12/2026", "2");   // "bad date: 24/12/2026"
            show("badDate", badDate);
            String badCount = parseBooking("2026-12-24", "x");  // "bad nights: x"
            show("badCount", badCount);
        }
        {
            String check = validate("2026-12-24", "x");     // "invalid input"
            show("check", check);
        }
        {
            int charged = nightsCharged();              // 2
            show("charged", charged);
        }
        {
            String status = confirm();                 // "confirmed", the exception is lost
            show("status", status);
        }
        {
            String firstGuest;
            try (BufferedReader reader = new BufferedReader(new StringReader("Lokesh\nAlex"))) {
                firstGuest = reader.readLine();
            } catch (IOException e) {
                firstGuest = "unknown";
            }
            String guest = firstGuest;                    // "Lokesh"
            show("guest", guest);
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
