package com.howtodoinjava.core.sorting;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ComparatorExamples {
    public static void main(String[] args) {
        
        List<User> list = getUnsortedUsers();
        
        Comparator<User> firstNameSorter = (o1, o2) -> o1.firstName().compareTo(o2.firstName());
        Comparator<User> lastNameSorter = (o1, o2) -> o1.lastName().compareTo(o2.lastName());
        Comparator<User> fullNameSorter = firstNameSorter.thenComparing(lastNameSorter);
        Comparator<User> reverseSorter = firstNameSorter.reversed();
        
        Collections.sort(list, firstNameSorter);
        
        List<User> sortedList = list.stream()
                .sorted(firstNameSorter)
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
