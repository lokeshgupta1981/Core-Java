package com.howtodoinjava.core.streams.distinct;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DistinctPrimitivesAndStrings 
{
    public static void main(String[] args) 
    {
        Collection<String> list = Arrays.asList("A", "B", "C", "D", "A", "B", "C");

        // Get collection without duplicate i.e. distinct only
        List<String> distinctElements = list.stream()
            .distinct()
            .collect(Collectors.toList());

        // Let's verify distinct elements
        System.out.println(distinctElements);
    }
}
