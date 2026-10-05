import module java.base;
import module java.desktop;

public class AmbiguousList {
  public static void main(String[] args) {
    List<String> fruits = List.of("apple", "banana");
    System.out.println(fruits);
  }
}
