package com.howtodoinjava.core.string;

import java.lang.reflect.Field;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;

public class StringToInt {

  public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

    //Integer.parseInt()
    Assertions.assertEquals(1001, Integer.parseInt("1001"));
    Assertions.assertEquals(513, Integer.parseInt("1001", 8));
    Assertions.assertEquals(1001, Integer.parseInt("amount=1001", 7, 11, 10));

    Assertions.assertThrows(NumberFormatException.class, ()->{
      Integer.parseInt(null);
    });

    Assertions.assertThrows(NumberFormatException.class, ()->{
      Integer.parseInt("abc");
    });


    //Integer.valueOf()
    Assertions.assertEquals(1001, Integer.valueOf("1001"));
    Assertions.assertEquals(513, Integer.valueOf("1001", 8));

    Assertions.assertThrows(NumberFormatException.class, ()->{
      Integer.valueOf("abc");
    });

    //Integer.decode()
    Assertions.assertEquals(100, Integer.decode("+100"));
    Assertions.assertEquals(64, Integer.decode("+0100"));
    Assertions.assertEquals(256, Integer.decode("+0x100"));

    String mainString = "i_love_java";
    String subString = mainString.substring(7);

    System.out.println(mainString);
    System.out.println(subString);

    //Lets see what's inside mainString
    Field innerCharArray = String.class.getDeclaredField("value");
    innerCharArray.setAccessible(true);
    char[] chars = (char[]) innerCharArray.get(mainString);
    System.out.println(Arrays.toString(chars));

    //Now peek inside subString
    chars = (char[]) innerCharArray.get(subString);
    System.out.println(Arrays.toString(chars));
  }
}
