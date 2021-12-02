package com.howtodoinjava.core.basic;

public class StringPoolExample 
{
    public static void main(String[] args) 
    {
        String a = "howtodoinjava";
        String b = "howtodoinjava";
        
        String c = new String("howtodoinjava");
        String d = c.intern();
        
        System.out.println(a == b);     //true
        System.out.println(b == c);     //false
        
        System.out.println(a == c);     //false
        System.out.println(a == d);     //true
    }
}
