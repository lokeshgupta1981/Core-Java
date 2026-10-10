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
import java.util.regex.*;

/**
 * Examples for the tutorial "IllegalStateException: No match found in Java Regex (Fixed)".
 * https://howtodoinjava.com/java/exception-handling/solved-java-lang-illegalstateexception-no-match-found-while-using-regex-named-groups/
 */
public class MatcherGroupNoMatchFoundState {
    static Optional<LocalTime> parseSlot(String text) {
        Matcher slot = Pattern.compile("(?<hour>\\d{2}):(?<minute>\\d{2})").matcher(text.strip());
        if (!slot.matches()) {
            return Optional.empty();
        }
        int hour = Integer.parseInt(slot.group("hour"));
        int minute = Integer.parseInt(slot.group("minute"));
        return hour < 24 && minute < 60 ? Optional.of(LocalTime.of(hour, minute)) : Optional.empty();
    }
    public static void main(String[] args) throws Exception {
        {
            Pattern time = Pattern.compile("(?<hour>\\d{2}):(?<minute>\\d{2})");
            Matcher matcher = time.matcher("18:45");
            try { String early = matcher.group("hour"); show("early", early); } catch (Throwable _t) { System.out.println("early -> " + _t); }
            boolean found = matcher.matches();                        // true
            show("found", found);
            String hour = found ? matcher.group("hour") : "none";     // "18"
            show("hour", hour);
        }
        {
            Pattern time = Pattern.compile("(?<hour>\\d{2}):(?<minute>\\d{2})");
            Matcher bad = time.matcher("6:45 pm");
            boolean badMatch = bad.matches();                         // false, and the next line ignores it
            show("badMatch", badMatch);
            try { String badHour = bad.group("hour"); show("badHour", badHour); } catch (Throwable _t) { System.out.println("badHour -> " + _t); }
        }
        {
            Optional<LocalTime> evening = parseSlot("18:45");        // Optional[18:45]
            show("evening", evening);
            Optional<LocalTime> invalid = parseSlot("6:45 pm");       // Optional.empty
            show("invalid", invalid);
            Optional<LocalTime> outOfRange = parseSlot("25:10");      // Optional.empty
            show("outOfRange", outOfRange);
        }
        {
            Pattern time = Pattern.compile("(?<hour>\\d{2}):(?<minute>\\d{2})");
            Matcher slots = time.matcher("Lunch 12:30, dinner 19:15");
            List<String> hours = new ArrayList<>();
            while (slots.find()) {
                hours.add(slots.group("hour"));
            }
            List<String> foundHours = hours;                          // [12, 19]
            show("foundHours", foundHours);
        }
        {
            Pattern time = Pattern.compile("(?<hour>\\d{2}):(?<minute>\\d{2})");
            boolean whole = time.matcher("Delivery at 18:45").matches();     // false
            show("whole", whole);
            boolean prefix = time.matcher("18:45 today").lookingAt();         // true
            show("prefix", prefix);
            boolean anywhere = time.matcher("Delivery at 18:45").find();      // true
            show("anywhere", anywhere);
        }
        {
            Pattern time = Pattern.compile("(?<hour>\\d{2}):(?<minute>\\d{2})");
            Matcher named = time.matcher("18:45");
            boolean ok = named.matches();                             // true
            show("ok", ok);
            try { String typo = named.group("hours"); show("typo", typo); } catch (Throwable _t) { System.out.println("typo -> " + _t); }
        }
        {
            Pattern optional = Pattern.compile("(?<hour>\\d{2}):(?<minute>\\d{2})(?<suffix> ?[AP]M)?");
            Matcher noSuffix = optional.matcher("18:45");
            boolean matched = noSuffix.matches();                     // true
            show("matched", matched);
            String suffix = noSuffix.group("suffix");                 // null
            show("suffix", suffix);
        }
        {
            try { Pattern underscore = Pattern.compile("(?<delivery_hour>\\d{2})"); show("underscore", underscore); } catch (Throwable _t) { System.out.println("underscore -> " + _t); }
        }
        {
            Pattern time = Pattern.compile("(?<hour>\\d{2}):(?<minute>\\d{2})");
            List<String> hourList = time.matcher("12:30, 19:15").results().map(r -> r.group("hour")).toList();   // [12, 19]
            show("hourList", hourList);
            Matcher check = time.matcher("noon");
            boolean tried = check.find();                             // false
            show("tried", tried);
            boolean safeToRead = check.hasMatch();                    // false
            show("safeToRead", safeToRead);
            Map<String, Integer> names = time.matcher("").namedGroups();   // {minute=2, hour=1} in any order
            show("names", names);
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
