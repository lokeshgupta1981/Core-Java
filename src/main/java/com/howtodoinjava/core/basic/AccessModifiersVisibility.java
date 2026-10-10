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

/**
 * Examples for the tutorial "Java Access Modifiers Explained With Examples and Modules".
 * https://howtodoinjava.com/java/oops/java-access-modifiers/
 */
public class AccessModifiersVisibility {
    static public class LoyaltyCard {
        public static final int MAX_POINTS = 10_000;    // public: every class
        protected int points;                           // protected: package + subclasses
        String ownerEmail;                              // no modifier: same package only
        private final List<Integer> history = new ArrayList<>();  // private: this class only

        public void addPoints(int amount) {
            points = Math.min(points + amount, MAX_POINTS);
            history.add(amount);
        }

        public int visits() { return history.size(); }
    }
    static public class Coupon {
        private final int percent;

        private Coupon(int percent) { this.percent = percent; }

        public static Coupon of(int percent) {
            if (percent < 1 || percent > 50) {
                throw new IllegalArgumentException("percent must be 1-50");
            }
            return new Coupon(percent);
        }

        public int percent() { return percent; }
    }
    static class PointsCalculator {
        static int pointsFor(double amount) {
            return (int) Math.floor(amount);    // one point per full dollar
        }
    }
    static public class GoldCard extends LoyaltyCard {
        @Override
        public void addPoints(int amount) {
            super.addPoints(amount * 2);        // double points for gold members
        }

        int balance() {
            return points;                      // allowed: inherited protected field
        }

        int balanceOf(GoldCard other) {
            return other.points;                // allowed: reference of the subclass type
        }
    }
    public static void main(String[] args) throws Exception {
        {
            LoyaltyCard card = new LoyaltyCard();
            card.addPoints(120);
            card.addPoints(80);
            int visits = card.visits();                 // 2
            show("visits", visits);
            int points = card.points;                   // 200, allowed in the same package
            show("points", points);
        }
        {
            Coupon tenOff = Coupon.of(10);
            int percent = tenOff.percent();             // 10
            show("percent", percent);
            try { Coupon tooMuch = Coupon.of(90); show("tooMuch", tooMuch); } catch (Throwable _t) { System.out.println("tooMuch -> " + _t); }
        }
        {
            int earned = PointsCalculator.pointsFor(7.80);   // 7
            show("earned", earned);
        }
        {
            GoldCard gold = new GoldCard();
            gold.addPoints(50);
            int goldBalance = gold.balance();           // 100
            show("goldBalance", goldBalance);
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
