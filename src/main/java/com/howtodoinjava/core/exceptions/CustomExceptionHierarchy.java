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
import java.net.SocketTimeoutException;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Examples for the tutorial "Custom Exceptions in Java: Best Practices and Examples".
 * https://howtodoinjava.com/java/exception-handling/best-practices-for-for-exception-handling/
 */
public class CustomExceptionHierarchy {
    static class SoldOutException extends RuntimeException {
        SoldOutException(String event) {
            super("sold out: " + event);
        }
    }
    static class BookingException extends RuntimeException {
        @Serial
        private static final long serialVersionUID = 1L;

        BookingException(String message) {
            super(message);
        }

        BookingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    static class PaymentFailedException extends RuntimeException {
        PaymentFailedException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    static void charge(String order) throws SocketTimeoutException {
        throw new SocketTimeoutException("Read timed out");
    }

    static void checkout(String order) {
        try {
            charge(order);
        } catch (SocketTimeoutException e) {
            throw new PaymentFailedException("Payment for " + order + " failed", e);
        }
    }
    static abstract sealed class TicketException extends RuntimeException
    permits SeatTakenException, TicketPaymentException {
        TicketException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    static final class SeatTakenException extends TicketException {
        private final String seat;

        SeatTakenException(String seat) {
            super("seat already taken: " + seat, null);
            this.seat = seat;
        }

        String seat() {
            return seat;
        }
    }
    static final class TicketPaymentException extends TicketException {
        TicketPaymentException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    static int httpStatus(TicketException e) {
        return switch (e) {
            case SeatTakenException taken -> 409;
            case TicketPaymentException payment -> 402;
        };
    }
    static class SeatMap {
        private final Set<String> sold = new HashSet<>(Set.of("A1"));

        void reserve(String seat) {
            if (!sold.add(seat)) {
                throw new SeatTakenException(seat);
            }
        }
    }
    public static void main(String[] args) throws Exception {
        {
            SoldOutException soldOut = new SoldOutException("jazz night");
            String message = soldOut.getMessage();              // "sold out: jazz night"
            show("message", message);
            boolean unchecked = soldOut instanceof RuntimeException;   // true
            show("unchecked", unchecked);
        }
        {
            BookingException plain = new BookingException("event cancelled");
            String text = plain.getMessage();                   // "event cancelled"
            show("text", text);
            Throwable none = plain.getCause();                  // null
            show("none", none);
        }
        {
            PaymentFailedException failure = null;
            try {
                checkout("concert");
            } catch (PaymentFailedException e) {
                failure = e;
            }
            String reason = failure.getMessage();               // "Payment for concert failed"
            show("reason", reason);
            String root = failure.getCause().getMessage();      // "Read timed out"
            show("root", root);
        }
        {
            int conflict = httpStatus(new SeatTakenException("A1"));                    // 409
            show("conflict", conflict);
            int declined = httpStatus(new TicketPaymentException("card declined", null)); // 402
            show("declined", declined);
        }
        {
            SeatTakenException taken = new SeatTakenException("A1");
            String seat = taken.seat();                         // "A1"
            show("seat", seat);
            String detail = taken.getMessage();                 // "seat already taken: A1"
            show("detail", detail);
        }
        {
            SeatMap seats = new SeatMap();
            SeatTakenException thrown = assertThrows(SeatTakenException.class, () -> seats.reserve("A1"));
            String takenSeat = thrown.seat();                   // "A1"
            show("takenSeat", takenSeat);
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
