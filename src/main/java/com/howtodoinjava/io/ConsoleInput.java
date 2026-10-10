package com.howtodoinjava.io;

import java.nio.*;
import java.nio.channels.*;
import java.util.zip.*;
import java.util.regex.*;
import java.net.*;
import java.text.*;
import java.nio.file.attribute.*;
import java.security.*;
import java.nio.charset.*;
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
 * Examples for the tutorial "Read Input From Console in Java: Scanner, IO.readln, Console".
 * https://howtodoinjava.com/java/io/read-input-from-console/
 */
public class ConsoleInput {
    static OptionalInt askNumber(Scanner in, PrintStream out, String prompt, int min, int max) {
        out.print(prompt);
        while (in.hasNextLine()) {
            String answer = in.nextLine().strip();
            try {
                int value = Integer.parseInt(answer);
                if (value >= min && value <= max) {
                    return OptionalInt.of(value);
                }
            } catch (NumberFormatException e) {
                // not a number, fall through and ask again
            }
            out.print("Please enter a whole number from " + min + " to " + max + ": ");
        }
        return OptionalInt.empty();                      // input ended
    }
    static Optional<String> loginName() {
        Console console = System.console();
        if (console == null) {
            return Optional.empty();                     // no terminal, use Scanner instead
        }
        String user = console.readLine("User: ");
        char[] password = console.readPassword("Password for %s: ", user);
        try {
            // check the password here
            return Optional.ofNullable(user);
        } finally {
            Arrays.fill(password, ' ');                  // remove the password from memory
        }
    }
    static record Booking(String guest, int nights) {}
    static Optional<Booking> askBooking(Scanner in, PrintStream out) {
        out.print("Guest name: ");
        if (!in.hasNextLine()) {
            return Optional.empty();
        }
        String guest = in.nextLine().strip();
        OptionalInt nights = askNumber(in, out, "Nights: ", 1, 30);
        return nights.isPresent() ? Optional.of(new Booking(guest, nights.getAsInt())) : Optional.empty();
    }
    public static void main(String[] args) throws Exception {
        {
            Scanner scanner = new Scanner("Lokesh\n37\n");     // new Scanner(System.in) in a real program
            show("scanner", scanner);
            String name = scanner.nextLine();                    // "Lokesh"
            show("name", name);
            int age = scanner.nextInt();                         // 37
            show("age", age);
        }
        {
            Scanner scanner = new Scanner("Lokesh Gupta\n3 12.5\n");
            String first = scanner.next();                       // "Lokesh"
            show("first", first);
            String rest = scanner.nextLine();                    // " Gupta"
            show("rest", rest);
            int seats = scanner.nextInt();                       // 3
            show("seats", seats);
            double price = scanner.nextDouble();                 // 12.5
            show("price", price);
        }
        {
            Scanner scanner = new Scanner("37\nLokesh\n");
            int age = scanner.nextInt();                         // 37
            show("age", age);
            String name = scanner.nextLine();                    // "", the rest of the line after 37
            show("name", name);
        }
        {
            Scanner scanner = new Scanner("37\nLokesh\n");
            int age = Integer.parseInt(scanner.nextLine().strip());   // 37
            show("age", age);
            String name = scanner.nextLine();                         // "Lokesh"
            show("name", name);
        }
        {
            Scanner scanner = new Scanner("ten\n");
            try { int age = scanner.nextInt(); show("age", age); } catch (Throwable _t) { System.out.println("age -> " + _t); }
        }
        {
            Scanner scanner = new Scanner("ten\n10.5\n42\n");
            while (scanner.hasNext() && !scanner.hasNextInt()) {
                scanner.nextLine();                              // discard the bad line
            }
            OptionalInt age = scanner.hasNextInt() ? OptionalInt.of(scanner.nextInt()) : OptionalInt.empty();  // OptionalInt[42]
            show("age", age);
        }
        {
            Scanner typed = new Scanner("ten\n10.5\n-3\n42\n");
            PrintStream screen = new PrintStream(OutputStream.nullOutputStream());
            OptionalInt age = askNumber(typed, screen, "Your age: ", 0, 130);      // OptionalInt[42]
            show("age", age);
            OptionalInt none = askNumber(new Scanner(""), screen, "Your age: ", 0, 130);  // OptionalInt.empty
            show("none", none);
        }
        {
            InputStream in = new ByteArrayInputStream("3 5 8\n".getBytes());  // System.in in a real program
            show("in", in);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line = reader.readLine();                                 // "3 5 8"
            show("line", line);
            int sum = Arrays.stream(line.strip().split("\\s+")).mapToInt(Integer::parseInt).sum();  // 16
            show("sum", sum);
        }
        {
            Scanner clerk = new Scanner("Maria\nzero\n3\n");       // new Scanner(System.in) in main()
            show("clerk", clerk);
            PrintStream screen = new PrintStream(OutputStream.nullOutputStream());
            Optional<Booking> booking = askBooking(clerk, screen);     // Optional[Booking[guest=Maria, nights=3]]
            show("booking", booking);
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
