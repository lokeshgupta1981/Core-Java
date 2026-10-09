package com.howtodoinjava.java25.flowcontrol;

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
 * Examples for the tutorial "Java switch Statement: Fall-Through, null and Patterns".
 * https://howtodoinjava.com/java/flow-control/switch-statement-in-java/
 */
public class SwitchStatement {
    static enum OrderStatus { NEW, PAID, SHIPPED, DELIVERED, CANCELLED }
    static String nextStep(OrderStatus status) {
        String step;
        switch (status) {
            case NEW:
            step = "wait for payment";
            break;
            case PAID:
            step = "pack the items";
            break;
            case SHIPPED:
            step = "track the parcel";
            break;
            default:
            step = "nothing to do";
        }
        return step;
    }
    static String nextStepBuggy(OrderStatus status) {
        String step = "";
        switch (status) {
            case NEW:
            step = "wait for payment";      // no break, falls through
            case PAID:
            step = "pack the items";
            break;
            default:
            step = "nothing to do";
        }
        return step;
    }
    static List<String> features(String plan) {
        List<String> list = new ArrayList<>();
        switch (plan) {
            case "premium":
            list.add("4K");                 // falls through
            case "standard":
            list.add("HD");                 // falls through
            case "basic":
            list.add("SD");
            break;
            default:
            throw new IllegalArgumentException("Unknown plan: " + plan);
        }
        return list;
    }
    static int shippingDays(String country) {
        switch (country) {
            case "IN":
            return 3;
            case "US", "CA":
            return 7;
            default:
            return 14;
        }
    }
    static String nextStepArrow(OrderStatus status) {
        String step;
        switch (status) {
            case NEW -> step = "wait for payment";
            case PAID -> step = "pack the items";
            case SHIPPED -> {
                String carrier = "DHL";
                step = "track the parcel with " + carrier;
            }
            default -> step = "nothing to do";
        }
        return step;
    }
    static String statusLabel(String status) {
        String text;
        switch (status) {
            case null -> text = "no status";
            case "PAID", "SHIPPED" -> text = "in progress";
            default -> text = "other";
        }
        return text;
    }
    static sealed interface Payment permits Card, Wallet, Cash {}
    static record Card(String last4) implements Payment {}
    static record Wallet(String provider) implements Payment {}
    static record Cash() implements Payment {}
    static String receipt(Payment payment) {
        String line;
        switch (payment) {
            case Card c when c.last4().equals("0000") -> line = "test card";
            case Card c -> line = "card ending " + c.last4();
            case Wallet(String provider) -> line = "paid with " + provider;
            case Cash _ -> line = "cash";
        }
        return line;
    }
    static String nextStepExpression(OrderStatus status) {
        return switch (status) {
            case NEW -> "wait for payment";
            case PAID -> "pack the items";
            case SHIPPED -> "track the parcel";
            case DELIVERED, CANCELLED -> "nothing to do";
        };
    }
    public static void main(String[] args) throws Exception {
        {
            int statusCode = 404;
            switch (statusCode) {
                case 200:
                System.out.println("OK");
                break;
                case 404:
                System.out.println("Not Found");      // printed
                break;
                default:
                System.out.println("Unexpected status");
            }
        }
        {
            String paid = nextStep(OrderStatus.PAID);            // "pack the items"
            show("paid", paid);
            String done = nextStep(OrderStatus.DELIVERED);       // "nothing to do"
            show("done", done);
        }
        {
            String wrong = nextStepBuggy(OrderStatus.NEW);   // "pack the items", the NEW value is overwritten
            show("wrong", wrong);
        }
        {
            List<String> premium = features("premium");   // [4K, HD, SD]
            show("premium", premium);
            List<String> basic = features("basic");       // [SD]
            show("basic", basic);
            try { List<String> gold = features("gold"); show("gold", gold); } catch (Throwable _t) { System.out.println("gold -> " + _t); }
        }
        {
            int india = shippingDays("IN");                                      // 3
            show("india", india);
            int lower = shippingDays("in");                                      // 14, case-sensitive
            show("lower", lower);
            int fixed = shippingDays(" in ".strip().toUpperCase(Locale.ROOT));   // 3
            show("fixed", fixed);
        }
        {
            String shipped = nextStepArrow(OrderStatus.SHIPPED);   // "track the parcel with DHL"
            show("shipped", shipped);
        }
        {
            String missing = statusLabel(null);     // "no status"
            show("missing", missing);
            String progress = statusLabel("PAID");  // "in progress"
            show("progress", progress);
            try { int days = shippingDays(null); show("days", days); } catch (Throwable _t) { System.out.println("days -> " + _t); }
        }
        {
            String card = receipt(new Card("4242"));      // "card ending 4242"
            show("card", card);
            String test = receipt(new Card("0000"));      // "test card"
            show("test", test);
            String wallet = receipt(new Wallet("UPI"));   // "paid with UPI"
            show("wallet", wallet);
        }
        {
            String step = nextStepExpression(OrderStatus.NEW);   // "wait for payment"
            show("step", step);
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
