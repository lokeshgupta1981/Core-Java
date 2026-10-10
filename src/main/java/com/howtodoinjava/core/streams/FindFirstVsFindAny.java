package com.howtodoinjava.core.streams;

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

import java.time.format.*;

/**
 * Examples for the tutorial "Java Stream findFirst() vs findAny(): Differences and Examples".
 * https://howtodoinjava.com/java8/stream-findfirst-findany/
 */
public class FindFirstVsFindAny {
    static record Agent(String name, int openTickets) {}
    static record Ticket(int id, int minutesWaiting) {}
    public static void main(String[] args) throws Exception {
        {
            List<String> cities = List.of("Oslo", "Lima", "Rome", "Kyiv");
            Optional<String> first = cities.parallelStream().findFirst();                                 // Optional[Oslo], on every run
            show("first", first);
            Optional<String> any = cities.parallelStream().findAny();                                     // one of the four cities, can change between runs
            show("any", any);
            Optional<String> firstWithR = cities.stream().filter(c -> c.startsWith("R")).findFirst();     // Optional[Rome]
            show("firstWithR", firstWithR);
        }
        {
            List<String> cities = List.of("Oslo", "Lima", "Rome", "Kyiv");
            Optional<String> head = cities.stream().findFirst();                                       // Optional[Oslo]
            show("head", head);
            Optional<String> firstEndingA = cities.stream().filter(c -> c.endsWith("a")).findFirst();  // Optional[Lima]
            show("firstEndingA", firstEndingA);
            Optional<String> firstLong = cities.stream().filter(c -> c.length() > 4).findFirst();      // Optional.empty
            show("firstLong", firstLong);
        }
        {
            List<String> cities = List.of("Oslo", "Lima", "Rome", "Kyiv");
            String name = cities.stream().filter(c -> c.length() > 4).findFirst().orElse("none");    // "none"
            show("name", name);
            String rome = cities.stream().filter(c -> c.startsWith("R")).findFirst().orElseThrow();   // "Rome"
            show("rome", rome);
            try { String zurich = cities.stream().filter(c -> c.startsWith("Z")).findFirst().orElseThrow(); show("zurich", zurich); } catch (Throwable _t) { System.out.println("zurich -> " + _t); }
        }
        {
            List<String> cities = List.of("Oslo", "Lima", "Rome", "Kyiv");
            Set<String> citySet = new HashSet<>(cities);
            Optional<String> fromSet = citySet.stream().findFirst();                 // some city, depends on hash order
            show("fromSet", fromSet);
            Optional<String> alphabetical = citySet.stream().sorted().findFirst();   // Optional[Kyiv]
            show("alphabetical", alphabetical);
            String treeFirst = new TreeSet<>(cities).first();                        // "Kyiv"
            show("treeFirst", treeFirst);
        }
        {
            List<String> cities = List.of("Oslo", "Lima", "Rome", "Kyiv");
            String head = cities.getFirst();                                // "Oslo"
            show("head", head);
            List<String> none = List.of();
            Optional<String> safeHead = none.stream().findFirst();         // Optional.empty
            show("safeHead", safeHead);
            try { String noHead = none.getFirst(); show("noHead", noHead); } catch (Throwable _t) { System.out.println("noHead -> " + _t); }
        }
        {
            List<String> cities = List.of("Oslo", "Lima", "Rome", "Kyiv");
            Optional<String> seqAny = cities.stream().findAny();                                      // Optional[Oslo] on JDK 25, not guaranteed
            show("seqAny", seqAny);
            Optional<String> parAny = cities.parallelStream().filter(c -> c.length() == 4).findAny();  // any of the four cities
            show("parAny", parAny);
            Optional<String> nothing = Stream.<String>empty().findAny();                              // Optional.empty
            show("nothing", nothing);
        }
        {
            List<String> names = Arrays.asList(null, "Lima", "Rome");
            try { Optional<String> boom = names.stream().findFirst(); show("boom", boom); } catch (Throwable _t) { System.out.println("boom -> " + _t); }
            Optional<String> safe = names.stream().filter(Objects::nonNull).findFirst();      // Optional[Lima]
            show("safe", safe);
        }
        {
            List<Agent> agents = List.of(new Agent("Ana", 3), new Agent("Ben", 0), new Agent("Cleo", 0));
            Optional<Agent> idle = agents.parallelStream().filter(a -> a.openTickets() == 0).findAny();   // Ben or Cleo
            show("idle", idle);
            String assignee = idle.map(Agent::name).orElse("queue");                                    // "Ben" or "Cleo"
            show("assignee", assignee);
            List<Ticket> tickets = List.of(new Ticket(7, 12), new Ticket(8, 45), new Ticket(9, 30));
            Optional<Ticket> oldest = tickets.stream().max(Comparator.comparingInt(Ticket::minutesWaiting));   // Optional[Ticket[id=8, minutesWaiting=45]]
            show("oldest", oldest);
            Optional<Ticket> firstOverLimit = tickets.stream().filter(t -> t.minutesWaiting() > 20).findFirst();   // Optional[Ticket[id=8, minutesWaiting=45]]
            show("firstOverLimit", firstOverLimit);
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
