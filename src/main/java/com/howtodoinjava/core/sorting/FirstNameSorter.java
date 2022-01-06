package com.howtodoinjava.core.sorting;

import java.util.Comparator;

public class FirstNameSorter implements Comparator<User> {

    @Override
    public int compare(User o1, User o2) {
        return o1.firstName().compareTo(o2.firstName());
    }
}
