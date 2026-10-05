import module java.se;        // every package of the Java SE API, java.base included
import java.util.List;        // java.se includes java.desktop, so List needs this line

public class WholeApi {
  public static void main(String[] args) {
    List<String> fruits = List.of("apple", "banana");
    LocalDate day = LocalDate.of(2026, 10, 5);
    Color color = Color.RED;
    System.out.println(fruits + " " + day + " " + color.getRed());
  }
}
