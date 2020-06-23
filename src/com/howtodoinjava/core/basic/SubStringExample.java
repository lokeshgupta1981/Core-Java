package com.howtodoinjava.core.basic;

public class SubStringExample {
	public static void main(String[] args) {

		System.out.println("0123456789".substring(0)); // 0123456789
		System.out.println("0123456789".substring(1)); // 123456789
		System.out.println("0123456789".substring(9)); // 9
		System.out.println("0123456789".substring(10)); // empty string
		//System.out.println("0123456789".substring(11)); // StringIndexOutOfBoundsException: begin 11, end 10, length 10

		System.out.println("0123456789".substring(1, 5)); // 1234
		System.out.println("0123456789".substring(0, 10)); // 0123456789
		System.out.println("0123456789".substring(0, 0)); // empty string
		System.out.println("0123456789".substring(5, 5)); // empty string
		System.out.println("0123456789".substring(10, 10)); // empty string
		System.out.println("0123456789".substring(9, 10)); // 9
		//System.out.println("0123456789".substring(9, 11)); // StringIndexOutOfBoundsException: begin 9, end 11, length 10

		String str = "The blog name is howtodoinjava.com";
		
		System.out.println(str.substring(3, 8));
	}
}
