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
import java.sql.SQLException;

/**
 * Examples for the tutorial "Pattern Matching for instanceof in Java (Java 16 to 25)".
 * https://howtodoinjava.com/java/basics/pattern-matching-instanceof/
 */
public class InstanceofPatternVariableScope {
    static public sealed interface Payment permits CardPayment, BankTransfer, Voucher {
    }
    static public record CardPayment(String last4, double amount) implements Payment {
    }
    static public record BankTransfer(String iban, double amount) implements Payment {
    }
    static public record Voucher(String code) implements Payment {
    }
    static String describeOld(Payment payment) {
        if (payment instanceof CardPayment) {
            CardPayment card = (CardPayment) payment;
            return "Card ending " + card.last4();
        } else if (payment instanceof BankTransfer) {
            BankTransfer transfer = (BankTransfer) payment;
            return "Transfer from " + transfer.iban();
        }
        return "Other payment";
    }
    static String describe(Payment payment) {
        if (payment instanceof CardPayment card) {
            return "Card ending " + card.last4();
        } else if (payment instanceof BankTransfer transfer) {
            return "Transfer from " + transfer.iban();
        }
        return "Other payment";
    }
    static String normalize(Object input) {
        if (input instanceof String text) {
            text = text.strip();
            return text.toLowerCase();
        }
        if (input instanceof final Integer number) {
            return "#" + number;
        }
        return "";
    }
    static double amountOf(Object item) {
        if (!(item instanceof CardPayment card)) {
            return 0;
        }
        return card.amount();
    }
    static public final class Money {
        private final long cents;
        private final String currency;

        public Money(long cents, String currency) {
            this.cents = cents;
            this.currency = currency;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Money other
                    && cents == other.cents
                    && currency.equals(other.currency);
        }

        @Override
        public int hashCode() {
            return Objects.hash(cents, currency);
        }
    }
    static String signUpError(RuntimeException ex) {
        if (ex.getCause() instanceof SQLException sql && "23505".equals(sql.getSQLState())) {
            return "Email already registered";
        }
        return "Sign-up failed, please try again";
    }
    static double fee(Payment payment) {
        return switch (payment) {
            case CardPayment card -> card.amount() * 0.02;
            case BankTransfer transfer -> 0.5;
            case Voucher voucher -> 0.0;
        };
    }
    public static void main(String[] args) throws Exception {
        {
            Object value = "Hello";
            int length = value instanceof String text ? text.length() : 0;   // 5
            show("length", length);
            Object number = 42;
            int none = number instanceof String text ? text.length() : 0;    // 0
            show("none", none);
        }
        {
            String card = describe(new CardPayment("4242", 25.0));        // "Card ending 4242"
            show("card", card);
            String bank = describe(new BankTransfer("DE89", 120.0));      // "Transfer from DE89"
            show("bank", bank);
            String other = describe(new Voucher("WELCOME"));              // "Other payment"
            show("other", other);
        }
        {
            Object nothing = null;
            boolean matched = nothing instanceof String text;   // false
            show("matched", matched);
        }
        {
            String code = normalize("  ABC ");      // "abc"
            show("code", code);
            String id = normalize(7);               // "#7"
            show("id", id);
        }
        {
            Collection<String> names = List.of("Anna", "Lokesh");
            boolean isList = names instanceof List<String> list;   // true
            show("isList", isList);
        }
        {
            String greeting = "Hi";
            boolean present = greeting instanceof String s;    // true
            show("present", present);
        }
        {
            Object input = "admin";
            boolean longName = input instanceof String name && name.length() > 3;   // true
            show("longName", longName);
        }
        {
            double paid = amountOf(new CardPayment("4242", 25.0));   // 25.0
            show("paid", paid);
            double skipped = amountOf("not a payment");             // 0.0
            show("skipped", skipped);
        }
        {
            boolean same = new Money(500, "EUR").equals(new Money(500, "EUR"));   // true
            show("same", same);
            boolean differs = new Money(500, "EUR").equals(new Money(500, "USD")); // false
            show("differs", differs);
            boolean withNull = new Money(500, "EUR").equals(null);                // false
            show("withNull", withNull);
        }
        {
            String duplicate = signUpError(new RuntimeException(new SQLException("duplicate key", "23505")));   // "Email already registered"
            show("duplicate", duplicate);
            String timeout = signUpError(new RuntimeException("timeout"));   // "Sign-up failed, please try again"
            show("timeout", timeout);
        }
        {
            Payment payment = new CardPayment("4242", 25.0);
            String label = payment instanceof CardPayment(String last4, double amount) && amount > 20
            ? "Large card payment " + last4
            : "Other";
            String result = label;                  // "Large card payment 4242"
            show("result", result);
        }
        {
            double cardFee = fee(new CardPayment("4242", 25.0));      // 0.5
            show("cardFee", cardFee);
            double voucherFee = fee(new Voucher("WELCOME"));          // 0.0
            show("voucherFee", voucherFee);
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
