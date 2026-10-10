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

/**
 * Examples for the tutorial "Java Is Pass-by-Value, Not Pass-by-Reference: Examples".
 * https://howtodoinjava.com/java/basics/java-is-pass-by-value-lets-see-how/
 */
public class PassByValueReferenceCopy {
    static void addBonus(int points) {
        points = points + 5;
    }
    static class Player {
        private String name;

        Player(String name) {
            this.name = name;
        }

        String name() {
            return name;
        }

        void setName(String name) {
            this.name = name;
        }
    }
    static void rename(Player p) {
        p.setName("Annie");
    }
    static void replace(Player p) {
        p = new Player("Bob");
        p.setName("Bobby");
    }
    static void swap(Player a, Player b) {
        Player temp = a;
        a = b;
        b = temp;
    }
    static void swapAt(Player[] team, int i, int j) {
        Player temp = team[i];
        team[i] = team[j];
        team[j] = temp;
    }
    static void shout(String text) {
        text = text + "!";
    }
    static void shout(StringBuilder text) {
        text.append("!");
    }
    static List<Integer> topThree(List<Integer> scores) {
        scores.sort(Comparator.reverseOrder());
        return scores.subList(0, Math.min(3, scores.size()));
    }
    static List<Integer> topThreeSafe(List<Integer> scores) {
        return scores.stream()
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .toList();
    }
    static int withBonus(int points) {
        return points + 5;
    }
    static record Result(int points, String rank) {}
    static Result finish(int points) {
        int total = points + 5;
        return new Result(total, total >= 15 ? "gold" : "silver");
    }
    public static void main(String[] args) throws Exception {
        {
            int points = 10;
            addBonus(points);
            int afterBonus = points;              // 10, the method changed its own copy
            show("afterBonus", afterBonus);
            Player anna = new Player("Anna");
            rename(anna);
            String renamed = anna.name();         // "Annie", the method changed the shared object
            show("renamed", renamed);
            replace(anna);
            String stillAnnie = anna.name();      // "Annie", the new object stayed inside the method
            show("stillAnnie", stillAnnie);
        }
        {
            Player left = new Player("Lee");
            Player right = new Player("Ria");
            swap(left, right);
            String leftName = left.name();          // "Lee"
            show("leftName", leftName);
            String rightName = right.name();        // "Ria"
            show("rightName", rightName);
        }
        {
            Player[] team = {new Player("Lee"), new Player("Ria")};
            swapAt(team, 0, 1);
            String firstPlace = team[0].name();      // "Ria"
            show("firstPlace", firstPlace);
        }
        {
            String greeting = "Hi";
            shout(greeting);
            String plain = greeting;                   // "Hi"
            show("plain", plain);
            StringBuilder builder = new StringBuilder("Hi");
            shout(builder);
            String built = builder.toString();         // "Hi!"
            show("built", built);
        }
        {
            List<Integer> arrivals = new ArrayList<>(List.of(40, 90, 10, 70));
            List<Integer> best = topThree(arrivals);
            String bestNow = best.toString();          // "[90, 70, 40]"
            show("bestNow", bestNow);
            String arrivalNow = arrivals.toString();   // "[90, 70, 40, 10]", caller's order is gone
            show("arrivalNow", arrivalNow);
        }
        {
            List<Integer> arrivals2 = new ArrayList<>(List.of(40, 90, 10, 70));
            List<Integer> best2 = topThreeSafe(arrivals2);
            String best2Now = best2.toString();          // "[90, 70, 40]"
            show("best2Now", best2Now);
            String arrival2Now = arrivals2.toString();   // "[40, 90, 10, 70]"
            show("arrival2Now", arrival2Now);
        }
        {
            int score = 10;
            score = withBonus(score);
            int newScore = score;                         // 15
            show("newScore", newScore);
        }
        {
            Result result = finish(10);
            int finalPoints = result.points();            // 15
            show("finalPoints", finalPoints);
            String rank = result.rank();                  // "gold"
            show("rank", rank);
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
