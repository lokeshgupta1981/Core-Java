package com.howtodoinjava.core.array;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import org.apache.commons.lang3.SerializationUtils;
import java.util.regex.*;
import java.nio.charset.*;
import java.nio.*;
import java.security.*;
import java.lang.reflect.*;
import java.lang.invoke.*;

/**
 * Examples for the tutorial "Copy an Array in Java: Shallow Copy vs Deep Copy".
 * https://howtodoinjava.com/java/array/java-array-clone-shallow-copy/
 */
public class ArrayCopy {
    static class Player {
        String name;
        int score;
        Player(String name, int score) {
            this.name = name;
            this.score = score;
        }
        Player(Player other) {
            this(other.name, other.score);
        }
        @Override
        public String toString() {
            return name + "=" + score;
        }
    }
    static class Seat implements Serializable {
        String row;
        Seat(String row) {
            this.row = row;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            int[] scores = {90, 75, 82};
            int[] cloned = scores.clone();                     // [90, 75, 82]
            show("cloned", cloned);
            int[] longer = Arrays.copyOf(scores, 5);           // [90, 75, 82, 0, 0]
            show("longer", longer);
            int[] target = new int[3];
            System.arraycopy(scores, 0, target, 0, 3);
            int[] copied = target;                             // [90, 75, 82]
            show("copied", copied);
            boolean separate = cloned != scores;               // true, a new array
            show("separate", separate);
        }
        {
            int[] scores = {90, 75, 82};
            int[] alias = scores;
            alias[0] = 0;
            int[] original = scores;                 // [0, 75, 82], changed through alias
            show("original", original);
        }
        {
            Player[] board = {new Player("ana", 10), new Player("ben", 20)};
            Player[] snapshot = board.clone();
            board[0] = new Player("cy", 5);            // replaces a slot in board only
            board[1].score = 99;                       // changes the shared Player
            Player[] now = board;                      // [cy=5, ben=99]
            show("now", now);
            Player[] saved = snapshot;                 // [ana=10, ben=99]
            show("saved", saved);
        }
        {
            String[] tags = {"java", "sql", "git"};
            String[] same = Arrays.copyOf(tags, tags.length);       // [java, sql, git]
            show("same", same);
            String[] firstTwo = Arrays.copyOf(tags, 2);             // [java, sql]
            show("firstTwo", firstTwo);
            String[] padded = Arrays.copyOf(tags, 4);               // [java, sql, git, null]
            show("padded", padded);
            String[] middle = Arrays.copyOfRange(tags, 1, 3);       // [sql, git]
            show("middle", middle);
        }
        {
            // arraycopy(source, sourceStart, destination, destinationStart, count)
            int[] lastWeek = {5, 6, 7};
            int[] month = new int[6];
            System.arraycopy(lastWeek, 0, month, 3, 3);
            int[] merged = month;                              // [0, 0, 0, 5, 6, 7]
            show("merged", merged);
        }
        {
            int[] queue = {1, 2, 3, 4, 0};
            System.arraycopy(queue, 1, queue, 2, 3);           // shift right from index 1
            queue[1] = 9;
            int[] inserted = queue;                            // [1, 9, 2, 3, 4]
            show("inserted", inserted);
        }
        {
            int[] small = new int[2];
            try { System.arraycopy(new int[] {1, 2, 3}, 0, small, 0, 3);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            Player[] board = {new Player("ana", 10), new Player("ben", 20)};
            Player[] backup = Arrays.stream(board).map(Player::new).toArray(Player[]::new);
            board[1].score = 99;
            Player[] live = board;                     // [ana=10, ben=99]
            show("live", live);
            Player[] kept = backup;                    // [ana=10, ben=20]
            show("kept", kept);
        }
        {
            int[][] grid = {{1, 2}, {3, 4}};
            int[][] shallow = grid.clone();
            shallow[0][0] = 99;
            int[][] original = grid;                    // [[99, 2], [3, 4]], row shared
            show("original", original);
        }
        {
            int[][] grid = {{1, 2}, {3, 4}};
            int[][] deep = Arrays.stream(grid).map(int[]::clone).toArray(int[][]::new);
            deep[0][0] = 99;
            int[][] untouched = grid;                   // [[1, 2], [3, 4]]
            show("untouched", untouched);
            int[][] changed = deep;                     // [[99, 2], [3, 4]]
            show("changed", changed);
        }
        {
            Seat[] hall = {new Seat("A"), new Seat("B")};
            Seat[] copy = SerializationUtils.clone(hall);
            hall[0].row = "Z";
            String copiedRow = copy[0].row;             // "A"
            show("copiedRow", copiedRow);
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
