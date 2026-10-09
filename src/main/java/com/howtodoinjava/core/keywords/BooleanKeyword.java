package com.howtodoinjava.core.keywords;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;


/**
 * Examples for the tutorial "Java boolean Keyword".
 * https://howtodoinjava.com/java/keywords/java-boolean/
 */
public class BooleanKeyword {
    static class Settings {
        boolean darkMode;                   // field, starts as false
    }
    public static void main(String[] args) throws Exception {
        {
            int age = 20;
            boolean isAdult = age >= 18;            // true
            show("isAdult", isAdult);
            boolean hasTicket = false;
            boolean canEnter = isAdult && hasTicket;   // false
            show("canEnter", canEnter);
        }
        {
            boolean dark = new Settings().darkMode;  // false
            show("dark", dark);
            boolean[] seats = new boolean[3];
            String seatsText = Arrays.toString(seats);   // "[false, false, false]"
            show("seatsText", seatsText);
        }
        {
            String name = null;
            boolean valid = name != null && !name.isEmpty();   // false, no exception
            show("valid", valid);
            boolean onlyOne = true ^ false;                     // true
            show("onlyOne", onlyOne);
            String label = valid ? "ok" : "missing";            // "missing"
            show("label", label);
        }
        {
            List<Boolean> answers = List.of(true, false, true);
            long yesCount = answers.stream().filter(b -> b).count();   // 2
            show("yesCount", yesCount);
            Boolean cached = Boolean.valueOf(true);
            boolean same = cached == Boolean.TRUE;                     // true
            show("same", same);
            int order = Boolean.compare(false, true);                  // -1
            show("order", order);
        }
        {
            Boolean newsletter = null;              // user has not answered
            show("newsletter", newsletter);
            boolean subscribed = Boolean.TRUE.equals(newsletter);   // false, safe
            show("subscribed", subscribed);
            try { boolean crash = newsletter; show("crash", crash); } catch (Throwable _t) { System.out.println("crash -> " + _t); }
        }
        {
            boolean upper = Boolean.parseBoolean("TRUE");   // true
            show("upper", upper);
            boolean yes = Boolean.parseBoolean("yes");      // false
            show("yes", yes);
            boolean none = Boolean.parseBoolean(null);      // false
            show("none", none);
            String text = String.valueOf(true);             // "true"
            show("text", text);
        }
        {
            System.setProperty("feature.beta", "true");
            boolean beta = Boolean.getBoolean("feature.beta");   // true
            show("beta", beta);
            boolean trap = Boolean.getBoolean("true");           // false
            show("trap", trap);
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
