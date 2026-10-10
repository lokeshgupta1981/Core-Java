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
 * Examples for the tutorial "Ternary Operator in Java: Syntax, Examples and Pitfalls".
 * https://howtodoinjava.com/java/basics/ternary-operator/
 */
public class TernaryOperatorFlow {
    static class Cover {
        static int loads = 0;

        static String load(String name) {
            loads++;
            return name + ".jpg";
        }
    }
    static String badge(boolean premium) {
        return premium ? "HD" : "SD";
    }
    static int skipsPerHour(String plan) {
        return plan.equals("free") ? 6
        : plan.equals("student") ? 12
        : Integer.MAX_VALUE;
    }
    static int skipsPerHourSwitch(String plan) {
        return switch (plan) {
            case "free" -> 6;
            case "student" -> 12;
            default -> Integer.MAX_VALUE;
        };
    }
    public static void main(String[] args) throws Exception {
        {
            int songs = 3;
            String label = songs + (songs == 1 ? " song" : " songs");   // "3 songs"
            show("label", label);
            String title = null;
            String shown = title != null ? title : "Untitled";          // "Untitled"
            show("shown", shown);
            int volume = 120;
            int capped = volume > 100 ? 100 : volume;                   // 100
            show("capped", capped);
        }
        {
            boolean cached = true;
            String image = cached ? "cache/cover.jpg" : Cover.load("cover");   // "cache/cover.jpg"
            show("image", image);
            int loadCalls = Cover.loads;                                        // 0, load() never ran
            show("loadCalls", loadCalls);
        }
        {
            int tracks = 1;
            String text;
            if (tracks == 1) {
                text = "1 track";
            } else {
                text = tracks + " tracks";
            }
            String fromIf = text;                   // "1 track"
            show("fromIf", fromIf);
        }
        {
            int tracks = 1;
            String text = tracks == 1 ? "1 track" : tracks + " tracks";   // "1 track"
            show("text", text);
        }
        {
            String quality = badge(true);                                   // "HD"
            show("quality", quality);
            String message = String.format("Playing in %s", badge(false));  // "Playing in SD"
            show("message", message);
        }
        {
            Object mixed = true ? 1 : 2.0;          // 1.0, int promoted to double
            show("mixed", mixed);
            int count = 0;
            Object letter = true ? 'A' : 0;         // A, constant 0 fits in char
            show("letter", letter);
            Object code = true ? 'A' : count;       // 65, char promoted to int
            show("code", code);
            Object either = true ? "text" : 42;     // "text", common type is Object
            show("either", either);
        }
        {
            Map<String, Integer> plays = new HashMap<>();
            boolean tracked = true;
            try { Integer count = tracked ? plays.get("intro") : 0; show("count", count); } catch (Throwable _t) { System.out.println("count -> " + _t); }
            Integer boxed = tracked ? plays.get("intro") : Integer.valueOf(0);   // null
            show("boxed", boxed);
            int playsOrZero = plays.getOrDefault("intro", 0);                    // 0
            show("playsOrZero", playsOrZero);
        }
        {
            int free = skipsPerHour("free");        // 6
            show("free", free);
            int student = skipsPerHour("student");  // 12
            show("student", student);
            int family = skipsPerHour("family");    // 2147483647
            show("family", family);
        }
        {
            int sameResult = skipsPerHourSwitch("student");   // 12
            show("sameResult", sameResult);
        }
        {
            int queued = 4;
            String status = "Queue: " + (queued > 0 ? "busy" : "empty");   // "Queue: busy"
            show("status", status);
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
