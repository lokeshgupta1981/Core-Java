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
 * Examples for the tutorial "Java if-else Statement: else-if, Nested if and Examples".
 * https://howtodoinjava.com/java/flow-control/if-else-statement-in-java/
 */
public class IfElseStatement {
    static int discountPercent(double total) {
        if (total >= 100) {
            return 15;
        } else if (total >= 50) {
            return 5;
        } else {
            return 0;
        }
    }
    static int discountWrongOrder(double total) {
        if (total >= 50) {
            return 5;
        } else if (total >= 100) {
            return 15;                   // never reached
        } else {
            return 0;
        }
    }
    static String deliverySlot(boolean member, int hour) {
        if (member) {
            if (hour < 14) {
                return "today";
            } else {
                return "tomorrow";
            }
        } else {
            return "in 3 days";
        }
    }
    static String slotNoBraces(boolean member, int hour) {
        String slot = "in 3 days";
        if (member)
        if (hour < 14)
        slot = "today";
        else                              // belongs to if (hour < 14)
        slot = "tomorrow";
        return slot;
    }
    static boolean isValidCoupon(String code) {
        if (code != null && !code.isBlank() && code.startsWith("SAVE")) {
            return true;
        }
        return false;
    }
    static String describeDiscount(Object discount) {
        if (discount instanceof Integer percent && percent > 0) {
            return percent + "% off";
        } else if (discount instanceof String code) {
            return "coupon " + code;
        } else {
            return "no discount";
        }
    }
    static int couponLength(Object input) {
        if (!(input instanceof String code)) {
            return 0;
        }
        return code.length();               // code is in scope here
    }
    static String checkoutNested(List<String> cart, String address, boolean paid) {
        if (!cart.isEmpty()) {
            if (address != null) {
                if (paid) {
                    return "shipped";
                } else {
                    return "awaiting payment";
                }
            } else {
                return "missing address";
            }
        } else {
            return "empty cart";
        }
    }
    static String checkout(List<String> cart, String address, boolean paid) {
        if (cart.isEmpty()) {
            return "empty cart";
        }
        if (address == null) {
            return "missing address";
        }
        if (!paid) {
            return "awaiting payment";
        }
        return "shipped";
    }
    public static void main(String[] args) throws Exception {
        {
            double total = 42.0;
            double fee;
            if (total >= 50) {
                fee = 0.0;
            } else {
                fee = 4.99;
            }
            double payable = total + fee;   // 46.99
            show("payable", payable);
        }
        {
            double total = 30.0;
            double fee = 0.0;
            if (total < 50) {
                fee = 4.99;                 // runs, 30 is under 50
            }
            double payable = total + fee;   // 34.99
            show("payable", payable);
        }
        {
            Boolean giftWrap = null;                          // customer did not choose
            show("giftWrap", giftWrap);
            boolean wrap = Boolean.TRUE.equals(giftWrap);      // false, no exception
            show("wrap", wrap);
            try { boolean unsafe = giftWrap; show("unsafe", unsafe); } catch (Throwable _t) { System.out.println("unsafe -> " + _t); }
        }
        {
            int big = discountPercent(120);     // 15
            show("big", big);
            int medium = discountPercent(60);   // 5
            show("medium", medium);
            int small = discountPercent(20);    // 0
            show("small", small);
        }
        {
            int wrong = discountWrongOrder(120);   // 5, the 15 branch is dead code
            show("wrong", wrong);
        }
        {
            String early = deliverySlot(true, 10);    // "today"
            show("early", early);
            String late = deliverySlot(true, 16);     // "tomorrow"
            show("late", late);
            String guest = deliverySlot(false, 10);   // "in 3 days"
            show("guest", guest);
        }
        {
            String guestSlot = slotNoBraces(false, 10);   // "in 3 days", the else never ran
            show("guestSlot", guestSlot);
        }
        {
            boolean ok = isValidCoupon("SAVE10");   // true
            show("ok", ok);
            boolean blank = isValidCoupon("  ");    // false
            show("blank", blank);
            try { boolean none = isValidCoupon(null); show("none", none); } catch (Throwable _t) { System.out.println("none -> " + _t); }
        }
        {
            String percentText = describeDiscount(10);       // "10% off"
            show("percentText", percentText);
            String couponText = describeDiscount("SAVE5");   // "coupon SAVE5"
            show("couponText", couponText);
            String zeroText = describeDiscount(0);           // "no discount"
            show("zeroText", zeroText);
        }
        {
            int len = couponLength("SAVE10");   // 6
            show("len", len);
            int noLen = couponLength(42);       // 0
            show("noLen", noLen);
        }
        {
            String shipped = checkout(List.of("book"), "Pune", true);    // "shipped"
            show("shipped", shipped);
            String empty = checkout(List.of(), "Pune", true);            // "empty cart"
            show("empty", empty);
            String unpaid = checkout(List.of("book"), "Pune", false);    // "awaiting payment"
            show("unpaid", unpaid);
            String same = checkoutNested(List.of("book"), null, true);   // "missing address"
            show("same", same);
        }
        {
            double total = 75.0;
            String shipping = total >= 50 ? "free" : "standard";   // "free"
            show("shipping", shipping);
            int age = 18;
            boolean isAdult = age >= 18;                            // true, no ternary needed
            show("isAdult", isAdult);
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
