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
 * Examples for the tutorial "Java Custom Serialization with readObject() and writeObject()".
 * https://howtodoinjava.com/java/serialization/custom-serialization-readobject-writeobject/
 */
public class CustomSerializationWriteobjectReadobject {
    static class Session implements Serializable {
        private String user = "lokesh";
        private transient long loginTime;            // not written by default

        long loginTime() { return loginTime; }
        void login(long time) { loginTime = time; }

        @Serial
        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();                // writes user
            out.writeLong(loginTime);                // extra data
        }

        @Serial
        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();                  // reads user
            loginTime = in.readLong();               // same order as written
        }
    }
    static class User implements Serializable {

        @Serial
        private static final long serialVersionUID = 7829136421241571165L;

        private transient String firstName;
        private transient String lastName;
        private transient int accountNumber;
        private transient LocalDate dateOpened;

        User(String firstName, String lastName, int accountNumber, LocalDate dateOpened) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.accountNumber = accountNumber;
            this.dateOpened = dateOpened;
        }

        String getFirstName() { return firstName; }
        String getLastName() { return lastName; }
        int getAccountNumber() { return accountNumber; }
        LocalDate getDateOpened() { return dateOpened; }

        @Serial
        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();
            out.writeUTF(firstName);
            out.writeUTF(lastName);
            out.writeInt(accountNumber);
            out.writeLong(dateOpened.toEpochDay());
        }

        @Serial
        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            firstName = in.readUTF();
            lastName = in.readUTF();
            accountNumber = in.readInt();
            dateOpened = LocalDate.ofEpochDay(in.readLong());
        }
    }
    static class Account implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        private final String owner;
        private final List<String> tags;
        private final int balance;

        Account(String owner, List<String> tags, int balance) {
            if (balance < 0) throw new IllegalArgumentException("negative balance");
            this.owner = Objects.requireNonNull(owner);
            this.tags = List.copyOf(tags);
            this.balance = balance;
        }

        int balance() { return balance; }

        @Serial
        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            if (owner == null || tags == null || balance < 0) {
                throw new InvalidObjectException("invalid account: " + owner + ", " + balance);
            }
        }
    }
    static byte[] toBytes(Object obj) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
            out.writeObject(obj);
        }
        return bytes.toByteArray();
    }

    static Object fromBytes(byte[] data) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return in.readObject();
        }
    }
    static class AppSettings implements Serializable {
        static final AppSettings INSTANCE = new AppSettings();

        private AppSettings() {}

        @Serial
        private Object readResolve() {
            return INSTANCE;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            User user = new User("Lokesh", "Gupta", 102825, LocalDate.of(2017, 5, 24));
            Path file = Files.createTempFile("user", ".ser");
            try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file))) {
                out.writeObject(user);
            }
            User restored;
            try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file))) {
                restored = (User) in.readObject();
            }
            String name = restored.getFirstName() + " " + restored.getLastName();  // "Lokesh Gupta"
            show("name", name);
            int account = restored.getAccountNumber();                              // 102825
            show("account", account);
            LocalDate opened = restored.getDateOpened();                            // 2017-05-24
            show("opened", opened);
        }
        {
            byte[] valid = toBytes(new Account("lokesh", List.of("gold"), 500));
            int copyBalance = ((Account) fromBytes(valid)).balance();      // 500
            show("copyBalance", copyBalance);
            byte[] edited = valid.clone();
            for (int i = 0; i < edited.length - 3; i++) {
                if (edited[i] == 0 && edited[i + 1] == 0 && edited[i + 2] == 1 && edited[i + 3] == (byte) 0xF4) {
                    Arrays.fill(edited, i, i + 4, (byte) 0xFF);
                }
            }
            try { Object broken = fromBytes(edited); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
        }
        {
            Object copy = fromBytes(toBytes(AppSettings.INSTANCE));
            boolean sameInstance = copy == AppSettings.INSTANCE;   // true
            show("sameInstance", sameInstance);
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
