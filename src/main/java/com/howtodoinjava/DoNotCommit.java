package com.howtodoinjava;

public class DoNotCommit {
public static void main(String[] args) {
    int oldCapacity = 16000; 
    int newCapacity = oldCapacity + (oldCapacity >> 1); 
    
    System.out.println(newCapacity);
}
}
