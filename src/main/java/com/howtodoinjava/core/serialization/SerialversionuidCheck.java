package com.howtodoinjava.core.serialization;

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

/**
 * Examples for the tutorial "Java serialVersionUID: What It Is and When to Change It".
 * https://howtodoinjava.com/java/serialization/serialversionuid/
 */
public class SerialversionuidCheck {
    static class Invoice implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;   // version 1 of the serialized form

        private final String customer;
        private final int amount;

        Invoice(String customer, int amount) {
            this.customer = customer;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return "Invoice[customer=" + customer + ", amount=" + amount + "]";
        }
    }
    static class Receipt implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
    }
    static class Legacy implements Serializable {
        String code;
    }
    static record Point(int x, int y) implements Serializable {}
    static enum Status { OPEN, PAID }
    public static void main(String[] args) throws Exception {
        {
            Path file = Files.createTempFile("invoice", ".ser");
            try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file))) {
                out.writeObject(new Invoice("Lokesh", 250));
            }
            Object restored;
            try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file))) {
                restored = in.readObject();
            }
            String text = restored.toString();                                        // "Invoice[customer=Lokesh, amount=250]"
            show("text", text);
            long version = ObjectStreamClass.lookup(Invoice.class).getSerialVersionUID(); // 1
            show("version", version);
        }
        {
            long computed = ObjectStreamClass.lookup(Legacy.class).getSerialVersionUID();  // a hash of the class structure
            show("computed", computed);
        }
        {
            long recordUid = ObjectStreamClass.lookup(Point.class).getSerialVersionUID();  // 0
            show("recordUid", recordUid);
            long enumUid = ObjectStreamClass.lookup(Status.class).getSerialVersionUID();    // 0
            show("enumUid", enumUid);
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
