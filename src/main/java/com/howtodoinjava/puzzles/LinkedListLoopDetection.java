package com.howtodoinjava.puzzles;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.util.regex.*;
import java.nio.charset.*;
import java.nio.*;
import java.security.*;
import java.lang.reflect.*;
import java.lang.invoke.*;

/**
 * Examples for the tutorial "Detect a Loop in a Linked List in Java with Floyd's Algorithm".
 * https://howtodoinjava.com/java/puzzles/how-to-detect-infinite-loop-in-linkedlist-in-java-with-example/
 */
public class LinkedListLoopDetection {
    static class Node {
        final int value;
        Node next;

        Node(int value) {
            this.value = value;
        }
    }
    static Node listOf(int... values) {
        Node dummy = new Node(0);
        Node tail = dummy;
        for (int v : values) {
            tail.next = new Node(v);
            tail = tail.next;
        }
        return dummy.next;
    }
    static Node nodeAt(Node head, int index) {
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current;
    }
    static boolean hasCycle(Node head) {
        Node slow = head;
        Node fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;                 // one step
            fast = fast.next.next;            // two steps
            if (slow == fast) {
                return true;                  // same node object
            }
        }
        return false;                         // fast reached the end
    }
    static Node meetingPoint(Node head) {
        Node slow = head;
        Node fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                return slow;
            }
        }
        return null;                          // no loop
    }
    static Node loopStart(Node head) {
        Node meet = meetingPoint(head);
        if (meet == null) {
            return null;
        }
        Node p = head;
        while (p != meet) {
            p = p.next;
            meet = meet.next;
        }
        return p;
    }
    static int loopLength(Node head) {
        Node meet = meetingPoint(head);
        if (meet == null) {
            return 0;
        }
        int length = 1;
        for (Node p = meet.next; p != meet; p = p.next) {
            length++;
        }
        return length;
    }
    static void removeLoop(Node head) {
        Node start = loopStart(head);
        if (start == null) {
            return;
        }
        Node last = start;
        while (last.next != start) {
            last = last.next;
        }
        last.next = null;                     // break the loop
    }
    static Node firstRepeatedNode(Node head) {
        Set<Node> visited = Collections.newSetFromMap(new IdentityHashMap<>());
        for (Node p = head; p != null; p = p.next) {
            if (!visited.add(p)) {
                return p;                     // seen before, so the loop starts here
            }
        }
        return null;
    }
    static boolean hasManagerCycle(Map<String, String> managerOf, String employee) {
        String slow = employee;
        String fast = employee;
        while (fast != null && managerOf.get(fast) != null) {
            slow = managerOf.get(slow);
            fast = managerOf.get(managerOf.get(fast));
            if (slow != null && slow.equals(fast)) {
                return true;
            }
        }
        return false;
    }
    public static void main(String[] args) throws Exception {
        {
            Node head = listOf(1, 2, 3, 4, 5);
            boolean before = hasCycle(head);             // false
            show("before", before);
            nodeAt(head, 4).next = nodeAt(head, 1);      // node 5 points back to node 2
            boolean after = hasCycle(head);              // true
            show("after", after);
        }
        {
            boolean empty = hasCycle(null);                     // false
            show("empty", empty);
            Node one = listOf(7);
            boolean single = hasCycle(one);                      // false
            show("single", single);
            one.next = one;
            boolean selfLoop = hasCycle(one);                    // true
            show("selfLoop", selfLoop);
            boolean repeated = hasCycle(listOf(1, 2, 1, 2));     // false
            show("repeated", repeated);
        }
        {
            Node looped = listOf(1, 2, 3, 4, 5);
            nodeAt(looped, 4).next = nodeAt(looped, 1);
            int meetValue = meetingPoint(looped).value;          // 5
            show("meetValue", meetValue);
            int startValue = loopStart(looped).value;            // 2
            show("startValue", startValue);
        }
        {
            Node broken = listOf(1, 2, 3, 4, 5);
            nodeAt(broken, 4).next = nodeAt(broken, 1);
            int length = loopLength(broken);                     // 4
            show("length", length);
            removeLoop(broken);
            boolean fixed = hasCycle(broken);                    // false
            show("fixed", fixed);
        }
        {
            Node withSet = listOf(1, 2, 3);
            nodeAt(withSet, 2).next = withSet;
            int setStart = firstRepeatedNode(withSet).value;     // 1
            show("setStart", setStart);
        }
        {
            Map<String, String> badImport = Map.of("ann", "bob", "bob", "cat", "cat", "bob");
            boolean loops = hasManagerCycle(badImport, "ann");                       // true
            show("loops", loops);
            Map<String, String> goodImport = Map.of("ann", "bob", "bob", "cat");
            boolean healthy = hasManagerCycle(goodImport, "ann");                      // false
            show("healthy", healthy);
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
