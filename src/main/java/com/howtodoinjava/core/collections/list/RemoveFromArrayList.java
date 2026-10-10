package com.howtodoinjava.core.collections.list;

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
 * Examples for the tutorial "Remove Element from ArrayList in Java: remove() and removeIf()".
 * https://howtodoinjava.com/java/collections/arraylist/remove-element-from-arraylist/
 */
public class RemoveFromArrayList {
    static record Guest(String name, int seat) {}
    static void removeForward(List<String> list, String value) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(value)) {
                list.remove(i);                   // the next element moves into i and is skipped
            }
        }
    }
    static record Rsvp(String name, boolean attending, int plusOnes) {}
    static void dropAlexInLoop(List<String> guests) {
        for (String g : guests) {
            if (g.equals("Alex")) {
                guests.remove(g);
            }
        }
    }
    static record Booking(String guest, int seat, boolean paid, int minutesOld) {}
    static int releaseUnpaid(List<Booking> bookings) {
        int before = bookings.size();
        bookings.removeIf(b -> !b.paid() && b.minutesOld() > 15);
        return before - bookings.size();
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> guests = new ArrayList<>(List.of("Alex", "Mia", "Sam", "Mia", "Zoe", "Tom"));
            String first = guests.remove(0);                          // "Alex", guests = [Mia, Sam, Mia, Zoe, Tom]
            show("first", first);
            boolean once = guests.remove("Mia");                      // true, guests = [Sam, Mia, Zoe, Tom]
            show("once", once);
            boolean all = guests.removeAll(List.of("Mia"));           // true, guests = [Sam, Zoe, Tom]
            show("all", all);
            boolean match = guests.removeIf(g -> g.startsWith("Z"));  // true, guests = [Sam, Tom]
            show("match", match);
            boolean keep = guests.retainAll(List.of("Sam", "Lea"));   // true, guests = [Sam]
            show("keep", keep);
        }
        {
            List<String> guests = new ArrayList<>(List.of("Alex", "Mia", "Sam", "Zoe"));
            String removed = guests.remove(1);            // "Mia", guests = [Alex, Sam, Zoe]
            show("removed", removed);
            int size = guests.size();                     // 3
            show("size", size);
            try { String bad = guests.remove(10); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
        }
        {
            List<String> waitlist = new ArrayList<>(List.of("Alex", "Mia", "Sam"));
            String next = waitlist.removeFirst();         // "Alex", waitlist = [Mia, Sam]
            show("next", next);
            String lastIn = waitlist.removeLast();        // "Sam", waitlist = [Mia]
            show("lastIn", lastIn);

            List<String> empty = new ArrayList<>();
            try { String none = empty.removeLast(); show("none", none); } catch (Throwable _t) { System.out.println("none -> " + _t); }
        }
        {
            List<String> guests = new ArrayList<>(List.of("Mia", "Sam", "Mia", "Zoe"));
            boolean first = guests.remove("Mia");         // true, guests = [Sam, Mia, Zoe]
            show("first", first);
            boolean second = guests.remove("Mia");        // true, guests = [Sam, Zoe]
            show("second", second);
            boolean third = guests.remove("Mia");         // false, guests = [Sam, Zoe]
            show("third", third);
        }
        {
            List<Guest> table = new ArrayList<>(List.of(new Guest("Mia", 4), new Guest("Sam", 7)));
            boolean left = table.remove(new Guest("Sam", 7));   // true, table = [Guest[name=Mia, seat=4]]
            show("left", left);
        }
        {
            List<Integer> seats = new ArrayList<>(List.of(3, 7, 12, 1));
            Integer atIndex = seats.remove(1);                    // 7 (index 1), seats = [3, 12, 1]
            show("atIndex", atIndex);
            boolean byValue = seats.remove(Integer.valueOf(1));   // true (value 1), seats = [3, 12]
            show("byValue", byValue);
            try { Integer wrong = seats.remove(12); show("wrong", wrong); } catch (Throwable _t) { System.out.println("wrong -> " + _t); }
        }
        {
            List<String> guests = new ArrayList<>(List.of("Mia", "Sam", "Mia", "Zoe", "Mia"));
            boolean viaIf = guests.removeIf("Mia"::equals);                     // true, guests = [Sam, Zoe]
            show("viaIf", viaIf);

            List<String> again = new ArrayList<>(List.of("Mia", "Sam", "Mia"));
            boolean viaAll = again.removeAll(Collections.singleton("Mia"));     // true, again = [Sam]
            show("viaAll", viaAll);
        }
        {
            List<String> guests = List.of("Mia", "Sam", "Mia", "Zoe");
            List<String> withoutMia = guests.stream().filter(g -> !g.equals("Mia")).toList();   // [Sam, Zoe]
            show("withoutMia", withoutMia);
        }
        {
            List<String> names = new ArrayList<>(List.of("Mia", "Mia", "Sam"));
            removeForward(names, "Mia");
            String result = names.toString();             // "[Mia, Sam]"
            show("result", result);
        }
        {
            List<String> guests = new ArrayList<>(List.of("Alex", "Mia", "Sam", "Mia", "Zoe"));
            boolean changed = guests.removeAll(List.of("Mia", "Zoe", "Lea"));   // true, guests = [Alex, Sam]
            show("changed", changed);
            boolean same = guests.removeAll(List.of("Lea"));                     // false, guests = [Alex, Sam]
            show("same", same);
            try { boolean nullArg = guests.removeAll(null); show("nullArg", nullArg); } catch (Throwable _t) { System.out.println("nullArg -> " + _t); }
        }
        {
            List<Integer> seats = new ArrayList<>(List.of(4, 15, 8, 23, 42));
            boolean odd = seats.removeIf(s -> s % 2 != 0);        // true, seats = [4, 8, 42]
            show("odd", odd);

            List<String> imported = new ArrayList<>(Arrays.asList("Mia", null, "Sam", null));
            boolean cleaned = imported.removeIf(Objects::isNull); // true, imported = [Mia, Sam]
            show("cleaned", cleaned);
        }
        {
            List<Rsvp> replies = new ArrayList<>(List.of(new Rsvp("Mia", true, 1), new Rsvp("Sam", false, 0), new Rsvp("Zoe", true, 3)));
            Predicate<Rsvp> declined = r -> !r.attending();
            Predicate<Rsvp> tooMany = r -> r.plusOnes() > 2;
            boolean dropped = replies.removeIf(declined.or(tooMany));   // true, replies = [Rsvp[name=Mia, attending=true, plusOnes=1]]
            show("dropped", dropped);
        }
        {
            List<String> invited = new ArrayList<>(List.of("Alex", "Mia", "Sam", "Zoe"));
            List<String> checkedIn = List.of("Zoe", "Mia", "Lea");
            boolean changed = invited.retainAll(checkedIn);       // true, invited = [Mia, Zoe]
            show("changed", changed);
            boolean again = invited.retainAll(checkedIn);         // false, invited = [Mia, Zoe]
            show("again", again);
        }
        {
            List<String> guests = new ArrayList<>(List.of("Alex", "Mia", "Sam"));
            try { dropAlexInLoop(guests);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            List<String> guests = new ArrayList<>(List.of("Alex", "Mia", "Sam", "Ann"));
            Iterator<String> it = guests.iterator();
            while (it.hasNext()) {
                String g = it.next();
                if (g.startsWith("A")) {
                    it.remove();
                }
            }
            String result = guests.toString();            // "[Mia, Sam]"
            show("result", result);
        }
        {
            List<Booking> bookings = new ArrayList<>(List.of(new Booking("Mia", 4, true, 30), new Booking("Sam", 7, false, 20), new Booking("Zoe", 9, false, 5)));
            int released = releaseUnpaid(bookings);       // 1, Sam's seat 7 is free again
            show("released", released);
            int stillHeld = bookings.size();              // 2
            show("stillHeld", stillHeld);
        }
        {
            List<String> fixed = Arrays.asList("Mia", "Sam");
            try { boolean failed = fixed.remove("Mia"); show("failed", failed); } catch (Throwable _t) { System.out.println("failed -> " + _t); }
            List<String> copy = new ArrayList<>(fixed);
            boolean worked = copy.remove("Mia");                          // true, copy = [Sam]
            show("worked", worked);
        }
        {
            List<String> names = List.of("Mia", "Sam", "Mia", "Zoe");
            List<String> unique = new ArrayList<>(new LinkedHashSet<>(names));   // [Mia, Sam, Zoe]
            show("unique", unique);
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
