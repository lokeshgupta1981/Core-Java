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
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Examples for the tutorial "EasyMock Tutorial: Mocking with EasyMock 5 and JUnit 5".
 * https://howtodoinjava.com/java/library/easymock-tutorial/
 */
public class EasymockRecordReplayVerify {
    static class Customer {
        private long id;
        private final String name;

        Customer(String name) {
            this.name = name;
        }

        long getId() { return id; }
        void setId(long id) { this.id = id; }
        String getName() { return name; }
    }
    static interface SequenceGenerator {
        long getNext();
    }
    static interface CustomerDao {
        Customer save(Customer customer);
        List<Customer> search(String name, int limit);
    }
    static interface WelcomeMailer {
        void sendWelcome(Customer customer);
    }
    static class CustomerService {
        private SequenceGenerator generator;
        private CustomerDao dao;
        private WelcomeMailer mailer;

        CustomerService() {
        }

        CustomerService(SequenceGenerator generator, CustomerDao dao, WelcomeMailer mailer) {
            this.generator = generator;
            this.dao = dao;
            this.mailer = mailer;
        }

        Customer register(Customer customer) {
            customer.setId(generator.getNext());
            Customer saved = dao.save(customer);
            mailer.sendWelcome(saved);
            return saved;
        }
    }
    static class LoyaltyService {
        int pointsFor(Customer customer) {
            throw new UnsupportedOperationException("calls the loyalty platform");
        }

        final String tier(int points) {
            return points > 100 ? "gold" : "basic";
        }
    }
    public static void main(String[] args) throws Exception {
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
