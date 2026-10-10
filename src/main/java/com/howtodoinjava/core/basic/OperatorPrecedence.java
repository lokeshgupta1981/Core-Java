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
 * Examples for the tutorial "Operators in Java: Types, Precedence and Examples".
 * https://howtodoinjava.com/java/basics/operators-in-java/
 */
public class OperatorPrecedence {

    public static void main(String[] args) throws Exception {
        {
            int seats = 7;
            int rows = 2;
            int total = seats * rows + 1;                   // 15, arithmetic
            show("total", total);
            int perRow = seats / rows;                      // 3, integer division
            show("perRow", perRow);
            int left = seats % rows;                        // 1, remainder
            show("left", left);
            seats += 3;                                     // compound assignment
            int afterBooking = seats;                       // 10
            show("afterBooking", afterBooking);
            boolean full = seats >= 10;                     // true, relational
            show("full", full);
            boolean open = !full && rows > 0;               // false, logical
            show("open", open);
            String label = full ? "Sold out" : "Book now";  // "Sold out", conditional
            show("label", label);
            int doubled = rows << 1;                        // 4, shift
            show("doubled", doubled);
            Object ticket = "A12";
            boolean isText = ticket instanceof String;      // true, type comparison
            show("isText", isText);
        }
        {
            int fullCars = 7 / 3;                   // 2
            show("fullCars", fullCars);
            int leftOver = 7 % 3;                   // 1
            show("leftOver", leftOver);
            double share = 7 / 3.0;                 // 2.3333333333333335
            show("share", share);
            int negDiv = -7 / 2;                    // -3, rounds toward zero
            show("negDiv", negDiv);
            int negRem = -7 % 2;                    // -1, sign of the left operand
            show("negRem", negRem);
            int floorRem = Math.floorMod(-7, 2);    // 1, always non-negative here
            show("floorRem", floorRem);
        }
        {
            int noSeats = 0;
            try { int perSeat = 100 / noSeats; show("perSeat", perSeat); } catch (Throwable _t) { System.out.println("perSeat -> " + _t); }
            double ratio = 100.0 / noSeats;                    // Infinity
            show("ratio", ratio);
            int wrapped = Integer.MAX_VALUE + 1;               // -2147483648
            show("wrapped", wrapped);
            try { int checked = Math.addExact(Integer.MAX_VALUE, 1); show("checked", checked); } catch (Throwable _t) { System.out.println("checked -> " + _t); }
        }
        {
            int sold = 5;
            int before = sold++;                    // 5, sold is 6 afterwards
            show("before", before);
            int after = ++sold;                     // 7
            show("after", after);
            int current = sold;                     // 7
            show("current", current);
        }
        {
            int visits = 5;
            visits = visits++;
            int result = visits;                    // 5, not 6
            show("result", result);
        }
        {
            byte level = 10;
            level += 300;                           // compiles, implicit (byte) cast
            byte stored = level;                    // 54, 310 does not fit in a byte
            show("stored", stored);
            int price = 200;
            price -= 50;
            price *= 2;
            int finalPrice = price;                 // 300
            show("finalPrice", finalPrice);
        }
        {
            String movie = new String("Dune");
            boolean sameObject = movie == "Dune";       // false
            show("sameObject", sameObject);
            boolean sameText = movie.equals("Dune");    // true
            show("sameText", sameText);
            boolean money = 0.1 + 0.2 == 0.3;           // false, rounding error
            show("money", money);
            boolean close = Math.abs(0.1 + 0.2 - 0.3) < 1e-9;   // true
            show("close", close);
        }
        {
            String coupon = null;
            try { boolean hasCoupon = coupon != null && !coupon.isBlank(); show("hasCoupon", hasCoupon); } catch (Throwable _t) { System.out.println("hasCoupon -> " + _t); }
            try { boolean unsafe = coupon != null & !coupon.isBlank(); show("unsafe", unsafe); } catch (Throwable _t) { System.out.println("unsafe -> " + _t); }
        }
        {
            int imax = 0b001;                       // 1
            show("imax", imax);
            int dolby = 0b010;                      // 2
            show("dolby", dolby);
            int recliner = 0b100;                   // 4
            show("recliner", recliner);
            int hall = imax | recliner;             // 5, two features set
            show("hall", hall);
            boolean hasDolby = (hall & dolby) != 0; // false
            show("hasDolby", hasDolby);
            int withDolby = hall | dolby;           // 7
            show("withDolby", withDolby);
            int withoutImax = withDolby & ~imax;    // 6
            show("withoutImax", withoutImax);
        }
        {
            int shiftedLeft = 3 << 2;               // 12
            show("shiftedLeft", shiftedLeft);
            int signed = -16 >> 2;                  // -4
            show("signed", signed);
            int unsigned = -16 >>> 28;              // 15
            show("unsigned", unsigned);
            int inverted = ~5;                      // -6
            show("inverted", inverted);
        }
        {
            Object payment = "UPI-123";
            boolean isText = payment instanceof String;          // true
            show("isText", isText);
            int length = payment instanceof String s ? s.length() : 0;   // 7
            show("length", length);
            Object nothing = null;
            boolean nullCheck = nothing instanceof String;       // false
            show("nullCheck", nullCheck);
        }
        {
            int adults = 1;
            int kids = 2;
            String wrong = "Tickets: " + adults + kids;       // "Tickets: 12"
            show("wrong", wrong);
            String right = "Tickets: " + (adults + kids);     // "Tickets: 3"
            show("right", right);
            String first = adults + kids + " tickets";        // "3 tickets"
            show("first", first);
            String none = "Seat " + null;                     // "Seat null"
            show("none", none);
        }
        {
            int base = 2 + 3 * 4;                   // 14
            show("base", base);
            int grouped = (2 + 3) * 4;              // 20
            show("grouped", grouped);
            int chained = 20 - 5 - 3;               // 12, left to right
            show("chained", chained);
            boolean mixed = 5 > 3 && 2 > 4 || 1 < 2; // true, && before ||
            show("mixed", mixed);
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
