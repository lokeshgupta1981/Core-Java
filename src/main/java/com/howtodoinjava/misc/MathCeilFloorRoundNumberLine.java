package com.howtodoinjava.misc;

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
 * Examples for the tutorial "Java Math.ceil() vs floor() vs round() With Examples".
 * https://howtodoinjava.com/java/java-math-ceil-floor-round-methods/
 */
public class MathCeilFloorRoundNumberLine {
    static long safeRound(double value) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Not a finite number: " + value);
        }
        return Math.round(value);
    }
    public static void main(String[] args) throws Exception {
        {
            double up = Math.ceil(4.3);             // 5.0
            show("up", up);
            double down = Math.floor(4.9);          // 4.0
            show("down", down);
            long nearest = Math.round(4.5);         // 5
            show("nearest", nearest);
            long negativeTie = Math.round(-4.5);    // -4
            show("negativeTie", negativeTie);
            double negativeUp = Math.ceil(-4.3);    // -4.0
            show("negativeUp", negativeUp);
        }
        {
            double number = 4.3;
            double roundedUp = Math.ceil(number);   // 5.0
            show("roundedUp", roundedUp);
            double whole = Math.ceil(7.0);          // 7.0
            show("whole", whole);
            double tiny = Math.ceil(7.0001);        // 8.0
            show("tiny", tiny);
            double negative = Math.ceil(-4.3);      // -4.0
            show("negative", negative);
            int pages = (int) Math.ceil(4.3);       // 5
            show("pages", pages);
        }
        {
            double number = 4.9;
            double roundedDown = Math.floor(number);   // 4.0
            show("roundedDown", roundedDown);
            double negative = Math.floor(-4.3);        // -5.0
            show("negative", negative);
            int truncated = (int) -4.3;                // -4
            show("truncated", truncated);
        }
        {
            double number = 4.6;
            long rounded = Math.round(number);      // 5
            show("rounded", rounded);
        }
        {
            double number = 4.4;
            long rounded = Math.round(number);      // 4
            show("rounded", rounded);
        }
        {
            long fromDouble = Math.round(2.5);      // 3
            show("fromDouble", fromDouble);
            int fromFloat = Math.round(2.5f);       // 3
            show("fromFloat", fromFloat);
            long negativeTie = Math.round(-2.5);    // -2
            show("negativeTie", negativeTie);
            long belowTie = Math.round(-2.51);      // -3
            show("belowTie", belowTie);
            int cast = (int) Math.round(7.8);       // 8
            show("cast", cast);
        }
        {
            double value = -4.5;
            double c = Math.ceil(value);            // -4.0
            show("c", c);
            double f = Math.floor(value);           // -5.0
            show("f", f);
            long r = Math.round(value);             // -4
            show("r", r);
            double e = Math.rint(value);            // -4.0
            show("e", e);
            int t = (int) value;                    // -4
            show("t", t);
        }
        {
            double a = Math.rint(2.5);              // 2.0
            show("a", a);
            double b = Math.rint(3.5);              // 4.0
            show("b", b);
            double c = Math.rint(-2.5);             // -2.0
            show("c", c);
            long d = Math.round(2.5);               // 3
            show("d", d);
        }
        {
            int recipes = 41;
            int perPage = 10;
            double wrong = Math.ceil(recipes / perPage);            // 4.0
            show("wrong", wrong);
            double viaDouble = Math.ceil((double) recipes / perPage); // 5.0
            show("viaDouble", viaDouble);
            int pages = Math.ceilDiv(recipes, perPage);              // 5
            show("pages", pages);
        }
        {
            int plain = -7 / 2;                     // -3
            show("plain", plain);
            int floored = Math.floorDiv(-7, 2);     // -4
            show("floored", floored);
            int ceiled = Math.ceilDiv(-7, 2);       // -3
            show("ceiled", ceiled);
            int cartons = Math.floorDiv(45, 12);    // 3
            show("cartons", cartons);
        }
        {
            double nanCeil = Math.ceil(Double.NaN);               // NaN
            show("nanCeil", nanCeil);
            long nanRound = Math.round(Double.NaN);               // 0
            show("nanRound", nanRound);
            long hugeRound = Math.round(1e20);                    // 9223372036854775807
            show("hugeRound", hugeRound);
            long infRound = Math.round(Double.NEGATIVE_INFINITY); // -9223372036854775808
            show("infRound", infRound);
            double minusZero = Math.ceil(-0.5);                   // -0.0
            show("minusZero", minusZero);
        }
        {
            long ok = safeRound(31.4);                 // 31
            show("ok", ok);
            try { long bad = safeRound(0.0 / 0.0); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
        }
        {
            double rating = 4.26;
            long wholeStars = Math.round(rating);                 // 4
            show("wholeStars", wholeStars);
            double halfStars = Math.round(rating * 2) / 2.0;      // 4.5
            show("halfStars", halfStars);
            boolean badge = Math.floor(rating) >= 4;              // true
            show("badge", badge);
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
