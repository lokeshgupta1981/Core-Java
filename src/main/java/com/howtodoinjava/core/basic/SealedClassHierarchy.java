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
 * Examples for the tutorial "Sealed Classes and Interfaces in Java (with Examples)".
 * https://howtodoinjava.com/java15/sealed-classes-interfaces/
 */
public class SealedClassHierarchy {
    static sealed interface PaymentResult permits Paid, Declined {}
    static record Paid(double amount) implements PaymentResult {}
    static record Declined(String reason) implements PaymentResult {}
    static String describe(PaymentResult result) {
        return switch (result) {
            case Paid p -> "Paid " + p.amount();
            case Declined d -> "Declined, " + d.reason();
        };
    }
    static sealed abstract class Pass permits DayPass, MonthPass, CustomPass {
        abstract double price();
    }
    static final class DayPass extends Pass {
        double price() { return 12; }
    }
    static sealed class MonthPass extends Pass permits StudentMonthPass {
        double price() { return 60; }
    }
    static final class StudentMonthPass extends MonthPass {
        double price() { return 40; }
    }
    static non-sealed class CustomPass extends Pass {
        double price() { return 100; }
    }
    static class CorporatePass extends CustomPass {
        double price() { return 80; }
    }
    // permits is inferred: both records are in the same file
    static sealed interface Discount {}
    static record Percent(int value) implements Discount {}
    static record Fixed(double amount) implements Discount {}
    static sealed interface Delivery permits Courier, Pickup {}
    static record Courier(String address, int days) implements Delivery {}
    static enum Pickup implements Delivery { STORE, LOCKER }
    static int etaDays(Delivery delivery) {
        return switch (delivery) {
            case Courier c -> c.days();
            case Pickup p -> 0;
        };
    }
    public static void main(String[] args) throws Exception {
        {
            String ok = describe(new Paid(25.0));                  // "Paid 25.0"
            show("ok", ok);
            String failed = describe(new Declined("card expired"));  // "Declined, card expired"
            show("failed", failed);
        }
        {
            double day = new DayPass().price();               // 12.0
            show("day", day);
            double student = new StudentMonthPass().price();   // 40.0
            show("student", student);
            double corporate = new CorporatePass().price();    // 80.0
            show("corporate", corporate);
        }
        {
            Delivery fast = new Courier("Main St 5", 1);
            Delivery locker = Pickup.LOCKER;
            boolean isRecord = fast.getClass().isRecord();     // true
            show("isRecord", isRecord);
        }
        {
            int courierEta = etaDays(new Courier("Main St 5", 2));    // 2
            show("courierEta", courierEta);
            int pickupEta = etaDays(Pickup.STORE);                   // 0
            show("pickupEta", pickupEta);
        }
        {
            boolean sealedType = PaymentResult.class.isSealed();                          // true
            show("sealedType", sealedType);
            int permittedCount = PaymentResult.class.getPermittedSubclasses().length;     // 2
            show("permittedCount", permittedCount);
            boolean openType = String.class.getPermittedSubclasses() == null;           // true
            show("openType", openType);
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
