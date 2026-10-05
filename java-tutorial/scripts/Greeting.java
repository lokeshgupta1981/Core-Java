void main() {
    String input = IO.readln("Your name: ");
    String name = (input == null || input.isBlank()) ? "stranger" : input.strip();

    List<String> topics = List.of("basics", "OOP", "collections");
    IO.println("Hello, " + name + "! Start with " + topics.getFirst() + ".");
}
