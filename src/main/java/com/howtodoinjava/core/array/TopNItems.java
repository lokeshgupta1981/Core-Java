package com.howtodoinjava.core.array;

import com.google.common.collect.MinMaxPriorityQueue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class TopNItems {
    public static void main(String[] args) {
        Integer[] items = {0, 10, 30, 2, 7, 5, 90, 76, 100, 45, 55};

        // 1
        // @formatter:off
        List<Integer> top3ItemsInList = Arrays.stream(items)
                .sorted(Collections.reverseOrder())
                .limit(3)
                .collect(Collectors.toList());
        System.out.println("Items are : " + top3ItemsInList);
        // @formatter:on

        // 2
        Arrays.sort(items, Collections.reverseOrder());
        Integer[] topThreeItems = Arrays.copyOfRange(items, 0, 3);
        System.out.println("Items are : " + Arrays.toString(topThreeItems));

        // 3 Without Sorting
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(3);
        for (Integer i : items) {
            priorityQueue.add(i);
            if (priorityQueue.size() > 3)
                priorityQueue.poll();
        }
        System.out.println("Items are : " + priorityQueue);

        // 4
        // @formatter:off
        MinMaxPriorityQueue<Integer> minMaxQueue =
                MinMaxPriorityQueue.orderedBy(Collections.reverseOrder())
                        .maximumSize(3)
                        .create();
        for (Integer i : items) {
            minMaxQueue.add(i);
        }
        System.out.println("Items are : " + minMaxQueue);
        // @formatter:on
    }
}
