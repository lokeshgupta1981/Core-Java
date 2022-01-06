package com.howtodoinjava.core.sorting;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ComparableExamples {
    public static void main(String[] args) {
        
        List<User> list = getUnsortedUsers();

        Collections.sort(list);
        
        System.out.println(list);

        List<User> sortedList = list.stream()
                                    .sorted()
                                    .collect(Collectors.toList());
        
        System.out.println(sortedList);
    }

    private static List<User> getUnsortedUsers() {
        return Arrays.asList(new User(1L, "A", "Q", Integer.valueOf(24)),
                new User(4L, "B", "P", Integer.valueOf(22)),
                new User(2L, "C", "O", Integer.valueOf(27)),
                new User(3L, "D", "N", Integer.valueOf(29)),
                new User(5L, "E", "M", Integer.valueOf(25)));
    }
}
