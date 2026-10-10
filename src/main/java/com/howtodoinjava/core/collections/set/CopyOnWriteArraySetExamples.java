package com.howtodoinjava.core.collections.set;

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
 * Examples for the tutorial "Java CopyOnWriteArraySet: Thread-Safe Set With Examples".
 * https://howtodoinjava.com/java/collections/java-copyonwritearrayset/
 */
public class CopyOnWriteArraySetExamples {
    static record Member(String name) {}
    static class ChatRoom {
        private final Set<Member> members = new CopyOnWriteArraySet<>();

        boolean join(Member m) {
            return members.add(m);
        }

        boolean leave(Member m) {
            return members.remove(m);
        }

        List<String> broadcast(String text) {
            return members.stream()
                    .map(m -> m.name() + " <- " + text)
                    .toList();
        }
    }
    public static void main(String[] args) throws Exception {
        {
            CopyOnWriteArraySet<String> members = new CopyOnWriteArraySet<>();
            boolean added = members.add("alice");                // true
            show("added", added);
            boolean addedAgain = members.add("alice");           // false, duplicate
            show("addedAgain", addedAgain);
            members.add("bob");
            boolean hasBob = members.contains("bob");            // true, linear scan
            show("hasBob", hasBob);
            boolean removed = members.remove("alice");           // true, copies the array
            show("removed", removed);
            Set<String> current = members;                       // [bob]
            show("current", current);
        }
        {
            CopyOnWriteArraySet<String> empty = new CopyOnWriteArraySet<>();                             // []
            show("empty", empty);
            CopyOnWriteArraySet<String> flags = new CopyOnWriteArraySet<>(List.of("dark", "beta", "dark")); // [dark, beta]
            show("flags", flags);
        }
        {
            CopyOnWriteArraySet<String> channels = new CopyOnWriteArraySet<>(List.of("email"));
            boolean changed = channels.addAll(List.of("sms", "email", "push")); // true
            show("changed", changed);
            Set<String> afterAdd = channels;                                    // [email, sms, push]
            show("afterAdd", afterAdd);
            boolean dropped = channels.removeIf(c -> c.startsWith("s"));        // true
            show("dropped", dropped);
            Set<String> afterRemove = channels;                                 // [email, push]
            show("afterRemove", afterRemove);
            int size = channels.size();                                         // 2
            show("size", size);
        }
        {
            CopyOnWriteArraySet<Integer> ids = new CopyOnWriteArraySet<>(List.of(1, 2, 3));
            Iterator<Integer> before = ids.iterator();
            Thread writer = new Thread(() -> {
                ids.add(4);
                ids.remove(1);
            });
            writer.start();
            writer.join();
            List<Integer> seen = new ArrayList<>();
            before.forEachRemaining(seen::add);
            List<Integer> snapshot = seen;                       // [1, 2, 3]
            show("snapshot", snapshot);
            Set<Integer> latest = ids;                           // [2, 3, 4]
            show("latest", latest);
        }
        {
            CopyOnWriteArraySet<Integer> codes = new CopyOnWriteArraySet<>(List.of(7, 8));
            Iterator<Integer> codeIt = codes.iterator();
            Integer firstCode = codeIt.next();                   // 7
            show("firstCode", firstCode);
            try { codeIt.remove();  } catch (Throwable _t) { System.out.println("-> " + _t); }
            boolean gone = codes.remove(firstCode);              // true
            show("gone", gone);
        }
        {
            ChatRoom room = new ChatRoom();
            boolean joined = room.join(new Member("ana"));       // true
            show("joined", joined);
            boolean joinedTwice = room.join(new Member("ana"));  // false
            show("joinedTwice", joinedTwice);
            room.join(new Member("raj"));
            List<String> sent = room.broadcast("hi");            // [ana <- hi, raj <- hi]
            show("sent", sent);
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
