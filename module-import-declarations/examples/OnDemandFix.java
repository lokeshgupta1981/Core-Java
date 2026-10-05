import module java.base;
import module java.desktop;
import java.util.*;           // a package import also wins over both module imports

public class OnDemandFix {
  public static void main(String[] args) {
    List<String> fruits = List.of("apple", "banana");
    System.out.println(fruits);
  }
}
