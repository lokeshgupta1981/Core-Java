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
 * Examples for the tutorial "extends vs implements in Java: Differences With Examples".
 * https://howtodoinjava.com/java/oops/extends-vs-implements/
 */
public class ExtendsVsImplementsClassDiagram {
    static class ExpressShipment extends Shipment implements Trackable, Insurable {
        ExpressShipment(String id, double weightKg) { super(id, weightKg); }  // calls the Shipment constructor
        @Override double cost() { return super.cost() + 10; }                // reuses inherited code
        public String trackingId() { return "EX-" + id; }                      // required by Trackable
        public double insuredValue() { return 500; }                           // required by Insurable
    }
    static class Shipment {
        protected final String id;
        protected final double weightKg;

        Shipment(String id, double weightKg) {
            this.id = id;
            this.weightKg = weightKg;
        }

        double cost() {
            return 5 + weightKg * 2;
        }
    }
    static interface Trackable {
        String trackingId();

        default String trackingUrl() {
            return "https://track.example.com/" + trackingId();
        }
    }
    static interface Insurable {
        double insuredValue();
    }
    static interface PriorityParcel extends Trackable, Insurable {
        int priority();
    }
    static class Locker implements Trackable {
        private final int number;

        Locker(int number) {
            this.number = number;
        }

        @Override
        public String trackingId() {
            return "LK-" + number;
        }
    }
    static record DroneDelivery(String code) implements Trackable {
        public String trackingId() { return "DR-" + code; }
    }
    static interface Fragile {
        default String label() { return "handle with care"; }
    }
    static interface Refrigerated {
        default String label() { return "keep below 5 C"; }
    }
    static class VaccineBox implements Fragile, Refrigerated {
        @Override
        public String label() {
            return Fragile.super.label() + ", " + Refrigerated.super.label();
        }
    }
    static <T extends Shipment & Trackable> String summary(T parcel) {
        return parcel.trackingId() + " costs " + parcel.cost();
    }
    static interface CourierClient {
        String bookPickup(String parcelId);
    }
    static abstract class BaseCourierClient implements CourierClient {
        private int attempts;

        @Override
        public String bookPickup(String parcelId) {
            attempts++;
            return callApi(parcelId);
        }

        int attempts() { return attempts; }

        protected abstract String callApi(String parcelId);
    }
    static class FastCourierClient extends BaseCourierClient {
        @Override
        protected String callApi(String parcelId) {
            return "FAST-" + parcelId;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Shipment standard = new Shipment("P1", 2.0);
            double standardCost = standard.cost();      // 9.0
            show("standardCost", standardCost);
            Shipment express = new ExpressShipment("P2", 2.0);
            double expressCost = express.cost();        // 19.0, the override runs
            show("expressCost", expressCost);
        }
        {
            Trackable locker = new Locker(7);
            String lockerId = locker.trackingId();      // "LK-7"
            show("lockerId", lockerId);
            String url = locker.trackingUrl();          // "https://track.example.com/LK-7"
            show("url", url);
        }
        {
            Trackable drone = new DroneDelivery("9");
            String droneUrl = drone.trackingUrl();      // "https://track.example.com/DR-9"
            show("droneUrl", droneUrl);
        }
        {
            ExpressShipment parcel = new ExpressShipment("P3", 1.0);
            boolean isShipment = parcel instanceof Shipment;    // true
            show("isShipment", isShipment);
            boolean isTrackable = parcel instanceof Trackable;  // true
            show("isTrackable", isTrackable);
            boolean isInsurable = parcel instanceof Insurable;  // true
            show("isInsurable", isInsurable);
            String parcelId = parcel.trackingId();              // "EX-P3"
            show("parcelId", parcelId);
            double insured = parcel.insuredValue();             // 500.0
            show("insured", insured);
        }
        {
            String label = new VaccineBox().label();    // "handle with care, keep below 5 C"
            show("label", label);
        }
        {
            String summary = summary(new ExpressShipment("P4", 2.0));  // "EX-P4 costs 19.0"
            show("summary", summary);
        }
        {
            CourierClient client = new FastCourierClient();
            String booking = client.bookPickup("P5");   // "FAST-P5"
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
