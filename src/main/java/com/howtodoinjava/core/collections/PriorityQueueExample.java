package com.howtodoinjava.core.collections;

import java.util.PriorityQueue;

public class PriorityQueueExample 
{
    public static void main(String[] args) 
    {
        // Creating a priority queue
        PriorityQueue<Integer> numbers = new PriorityQueue<>();

        // Using the add() method
        numbers.add(3);
        numbers.add(2);
        System.out.println("PriorityQueue: " + numbers);

        // Using the offer() method
        numbers.offer(1);
        System.out.println("PriorityQueue: " + numbers);
        
        //Retrieve the items
        System.out.println("Item: " + numbers.poll());
        System.out.println("Item: " + numbers.poll());
        System.out.println("Item: " + numbers.poll());
    }
}
