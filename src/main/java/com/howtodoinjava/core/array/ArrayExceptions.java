package com.howtodoinjava.core.array;

public class ArrayExceptions {
  public static void main(final String[] args) 
  {
    //1
    Number[] numbers = new Number[3];
    numbers[0] = Integer.valueOf(10);  // Works Fine.
    
    //2
    Integer[] intArray = { 1, 2, 3, 4 };
    Number[] numArray = intArray;
    numArray[0] = 10;  // Works Fine.
    
    double value = 10.01;
    
    if(intArray.getClass().getComponentType() 
        == ((Object)value).getClass()) {
      numArray[0] = value; 
    } else {
      System.out.println("Incompitable type");
    }
    
   
  }
}
