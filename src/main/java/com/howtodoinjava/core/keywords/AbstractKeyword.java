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
 * Examples for the tutorial "Abstract Class in Java".
 * https://howtodoinjava.com/java/keywords/abstract-keyword/
 */
public class AbstractKeyword {
    static abstract class Payment {
        protected final double amount;

        Payment(double amount) {
            if (amount <= 0) {
                throw new IllegalArgumentException("amount must be positive");
            }
            this.amount = amount;
        }

        abstract double fee();               // each payment type decides

        double total() {
            return amount + fee();
        }
    }

    static class CardPayment extends Payment {
        CardPayment(double amount) {
            super(amount);
        }

        @Override
        double fee() {
            return amount * 0.02;
        }
    }
    static abstract class Report {
        final String render() {             // template method
            return header() + "\n" + body();
        }

        String header() {
            return "== " + title() + " ==";
        }

        abstract String title();

        abstract String body();
    }

    static class SalesReport extends Report {
        String title() {
            return "Sales";
        }

        String body() {
            return "Total 1200";
        }
    }
    static sealed abstract class Account permits Savings, Checking {
        abstract double rate();
    }

    static final class Savings extends Account {
        double rate() {
            return 0.04;
        }
    }

    static final class Checking extends Account {
        double rate() {
            return 0.0;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Payment card = new CardPayment(100);
            double total = card.total();            // 102.0
            show("total", total);
        }
        {
            Payment voucher = new Payment(20) {
                @Override
                double fee() {
                    return 0;
                }
            };
            double voucherTotal = voucher.total();  // 20.0
            show("voucherTotal", voucherTotal);
        }
        {
            String text = new SalesReport().render();   // "== Sales ==\nTotal 1200"
            show("text", text);
        }
        {
            Account account = new Savings();
            String kind = switch (account) {
                case Savings s -> "savings at " + s.rate();
                case Checking c -> "checking";
            };
            String result = kind;                   // "savings at 0.04"
            show("result", result);
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
